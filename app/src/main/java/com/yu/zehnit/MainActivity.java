package com.yu.zehnit;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.yu.zehnit.tools.EqpAdapter;
import com.yu.zehnit.tools.Equipment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import cn.wandersnail.ble.Connection;
import cn.wandersnail.ble.ConnectionConfiguration;
import cn.wandersnail.ble.Device;
import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.ble.EventObserver;

public class MainActivity extends BaseActivity implements EventObserver {


    private static final String TAG = "dxx";

    private final static int REQUEST_ENABLE_BT = 1;
    private int eqpNum;
    private String address;

    private boolean isDeviceConnected = false;

    private ProgressDialog progressDialog;
    private AlertDialog dialog;


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

        // 注册观察者
        EasyBLE.getInstance().registerObserver(this);


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
        Log.d(TAG, "activity onResume: ");

        if (bluetoothIsOn()) {
            updateEqp();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EasyBLE.getInstance().release();
        ActivityController.finishAll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "MainActivity onActivityResult, requestCode : " + requestCode + ", resultCode : " + resultCode);

        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            ActivityController.finishAll();
        }

    }

    private boolean bluetoothIsOn () {
        return EasyBLE.getInstance().isBluetoothOn();
    }

    private void updateEqp() {
        SharedPreferences pref = getSharedPreferences("eqpNum", Context.MODE_PRIVATE);
        eqpNum = pref.getInt("num", 0);
        String fileName;
        if (bluetoothIsOn() && eqpNum != 0) {
            List<Equipment> eqpList = MyApplication.getInstance().getEqpList();
            eqpList.clear();
            for (int i = 1; i <= eqpNum; i++) {
                fileName = "eqp" + i;
                pref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
                String name = pref.getString("name", "N/A");
                address = pref.getString("address" , "N/A");
                Equipment eqp;
                // 初始化/更新时只连接最后一个设备
                if (i == eqpNum) {
                    // 获取连接状态
                    Connection connection = EasyBLE.getInstance().getConnection(address);

                    if (connection == null) {
                        eqp = new Equipment(0, name, address, R.mipmap.ic_launcher_round, "离线");
                        // 进行连接，并显示UI
                        showProgressDialog();
                        connectEqp();
                    } else {
                        eqp = new Equipment(0, name, address, R.mipmap.ic_launcher_round, "在线");
                    }
                } else {
                    eqp = new Equipment(0, name, address, R.mipmap.ic_launcher_round, "离线");
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
        progressDialog.setMessage("正在连接设备...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void connectEqp() {
        if (bluetoothIsOn()) {
            ConnectionConfiguration config = new ConnectionConfiguration();
            config.setConnectTimeoutMillis(10000);
            config.setRequestTimeoutMillis(1000);
            config.setAutoReconnect(false);
            EasyBLE.getInstance().connect(address, config);
        } else {
            tipDialog("手机蓝牙未打开，请打开后重试");
        }
    }

    @Override
    public void onConnectionStateChanged(@NonNull Device device) {
        Log.d(TAG, "onConnectionStateChanged: " +device.getConnectionState());
        // 获取最后一个设备以便更新状态
        Equipment equipment = MyApplication.getInstance().getEqpList().get(0);
        // 获取设备适配器监听数据变化
        EqpAdapter adapter = MyApplication.getInstance().getAdapter();
        switch (device.getConnectionState()) {
            case DISCONNECTED:
                progressDialog.dismiss();
                EasyBLE.getInstance().releaseAllConnections();
                tipDialog("连接失败，请确定设备已打开");
                equipment.setText("离线");
                adapter.notifyDataSetChanged();
                break;
            case SERVICE_DISCOVERED:
                progressDialog.dismiss();
                tipDialog("连接成功");
                equipment.setText("在线");
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void tipDialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setMessage(msg);
        // 点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);

        if (msg.equals("连接成功")) {
            Message message = new Message();
            message.what = 1;
            handler.sendMessageDelayed(message, 800);
        } else if (msg.equals("连接失败，请确定设备已打开")){
            builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        progressDialog.show();
                        connectEqp();
                }
            });
        } else {
            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressDialog.dismiss();
                    requestBluetoothEnable();
                }
            });
        }
        dialog = builder.create();      //创建AlertDialog对象
        dialog.show();
    }


    private void requestBluetoothEnable() {
        if (!bluetoothIsOn()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Log.d(TAG, "MainActivity onCreate: 请求蓝牙");
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1){
                dialog.dismiss();
            }
        }
    };

}