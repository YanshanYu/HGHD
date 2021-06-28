package com.yu.zehnit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.yu.zehnit.tools.EqpAdapter;
import com.yu.zehnit.tools.Equipment;
import com.yu.zehnit.tools.SharedPreferencesUtils;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Arrays;
import java.util.List;

import cn.wandersnail.ble.Connection;
import cn.wandersnail.ble.ConnectionConfiguration;
import cn.wandersnail.ble.Device;
import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.ble.EventObserver;
import cn.wandersnail.ble.Request;
import cn.wandersnail.commons.util.StringUtils;
import cn.wandersnail.widget.dialog.DefaultAlertDialog;

public class MainActivity extends BaseActivity implements EventObserver {


    private static final String TAG = "dxx";

    private final static int REQUEST_ENABLE_BT = 1;
    private int eqpNum;
    private String address;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_control, R.id.navigation_rehabilitation,R.id.navigation_info)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // 切换fragment
        NavigationUI.setupWithNavController(navView, navController);

        // 设置状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true)
                .fitsSystemWindows(true).init();


        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "设备不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }


        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        requestBluetoothEnable();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册观察者
        EasyBLE.getInstance().registerObserver(this);

        Log.d(TAG, "activity onResume: " + MyApplication.getInstance().isConnected());
        if (bluetoothIsOn() && (!MyApplication.getInstance().isConnected())) {
            updateEqp();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Main");
        // 取消注册观察者
        EasyBLE.getInstance().unregisterObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyBLE.getInstance().release();
        MyApplication.getInstance().setConnected(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        EasyBLE.getInstance().release();
//        MyApplication.getInstance().setConnected(false);
        ActivityController.finishAll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            ActivityController.finishAll();
        }

    }

    private boolean bluetoothIsOn () {
        return EasyBLE.getInstance().isBluetoothOn();
    }


    private void updateEqp() {
        SharedPreferencesUtils.setFileName("info");
        eqpNum = (int) SharedPreferencesUtils.getParam(MainActivity.this, "eqpNum", 0);
//        SharedPreferences pref = getSharedPreferences("eqpNum", Context.MODE_PRIVATE);
//        eqpNum = pref.getInt("num", 0);
        Log.d(TAG, "updateEqp: 主页设备数量" + eqpNum);
        String fileName;
        if (bluetoothIsOn() && eqpNum != 0) {
            List<Equipment> eqpList = MyApplication.getInstance().getEqpList();
            eqpList.clear();
            for (int i = 1; i <= eqpNum; i++) {
                fileName = "eqp" + i;
                SharedPreferencesUtils.setFileName(fileName);
                String name = (String) SharedPreferencesUtils.getParam(MainActivity.this, "name", "N/A");
                address = (String) SharedPreferencesUtils.getParam(MainActivity.this, "address", "N/A");

//                pref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
//                String name = pref.getString("name", "N/A");
//                address = pref.getString("address" , "N/A");
                Equipment eqp;
                // 初始化/更新时只连接最后一个设备
                if (i == eqpNum) {
                    // 获取连接状态
                    Connection connection = EasyBLE.getInstance().getConnection(address);

                    if (connection == null) {
                        eqp = new Equipment(0, name, address, R.mipmap.ic_launcher_round, getString(R.string.offline));
                        // 进行连接，并显示UI
                        showProgressDialog();
                        connectEqp();
                    } else {
                        MyApplication.getInstance().setConnected(true);
                        eqp = new Equipment(0, name, address, R.mipmap.ic_launcher_round, getString(R.string.online));
                    }
                } else {
                    eqp = new Equipment(0, name, address, R.mipmap.ic_launcher_round, getString(R.string.offline));
                }
                eqpList.add(eqp);
            }
            Equipment addEqp = new Equipment(1);
            eqpList.add(addEqp);
            // 更新全局设备列表
            MyApplication.getInstance().setEqpList(eqpList);
            // 更新全局设备适配器
            EqpAdapter adapter = new EqpAdapter(eqpList);
            MyApplication.getInstance().setAdapter(adapter);
        }
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.connect_device));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void connectEqp() {
        if (bluetoothIsOn()) {
            ConnectionConfiguration config = new ConnectionConfiguration();
            config.setConnectTimeoutMillis(10000);
            config.setRequestTimeoutMillis(1000);
            // 不自动重连
            config.setAutoReconnect(false);
            EasyBLE.getInstance().connect(address, config);
        } else {
            tipDialog(getString(R.string.bt_is_off));
        }
    }

    @Override
    public void onConnectionStateChanged(@NonNull Device device) {
        Log.d(TAG, "MainActivity onConnectionStateChanged: " +device.getConnectionState());
        // 获取最后一个设备以便更新状态
        Equipment equipment = MyApplication.getInstance().getEqpList().get(0);
        // 获取设备适配器监听数据变化
        EqpAdapter adapter = MyApplication.getInstance().getAdapter();
        switch (device.getConnectionState()) {
            case DISCONNECTED:
                MyApplication.getInstance().setConnected(false);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                EasyBLE.getInstance().releaseAllConnections();
                tipDialog(getString(R.string.connection_failed));
                equipment.setText(getString(R.string.offline));
                adapter.notifyDataSetChanged();
                break;
            case SERVICE_DISCOVERED:
                MyApplication.getInstance().setConnected(true);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                tipDialog(getString(R.string.connection_succeeded));
                equipment.setText(getString(R.string.online));
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void tipDialog(String msg){
        DefaultAlertDialog dialog = new DefaultAlertDialog(MainActivity.this);
        dialog.setTitle(getString(R.string.tip));
        dialog.setMessage(msg);
        // 点击对话框以外的区域是否让对话框消失
        dialog.setCancelable(false);
        dialog.setTitleBackgroundColor(-1);

        if (msg.equals(getString(R.string.connection_succeeded))) {
            dialog.setAutoDismiss(true);
            dialog.setAutoDismissDelayMillis(800);
        } else if (msg.equals(getString(R.string.connection_failed))){
            dialog.setPositiveButton(getString(R.string.try_again), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    connectEqp();
                }
            });
        } else {
            dialog.setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.dismiss();
                    requestBluetoothEnable();
                }
            });
        }
        dialog.show();
    }


    private void requestBluetoothEnable() {
        if (!bluetoothIsOn()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onCharacteristicWrite(@NonNull Request request, @NonNull byte[] value) {

        Log.d(TAG, "onCharacteristicWrite: 写入特征值：" + Arrays.toString(value));
    }

}