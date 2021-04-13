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
import com.yu.zehnit.tools.Equipment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import cn.wandersnail.ble.ConnectionConfiguration;
import cn.wandersnail.ble.Device;
import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.ble.EventObserver;

public class MainActivity extends BaseActivity implements EventObserver {


    private static final String TAG = "dxx";
    private BluetoothAdapter bluetoothAdapter;

    private final static int REQUEST_ENABLE_BT = 1;
    private int count = 0;

    // Time out
    private static final long DEVICE_CONNECT_TIMEOUT = 20000;

    private boolean isScanning = false;
    private boolean isDeviceConnected = false, isConnecting = false;
    private Handler handler;

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
//        EasyBLE.getInstance().registerObserver(this);


        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "设备不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        requestBluetoothEnable();

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

//    @Override
//    public void onConnectionStateChanged(@NonNull Device device) {
//        Log.d(TAG, "onConnectionStateChanged: " +device.getConnectionState());
//        Equipment equipment;
//        switch (device.getConnectionState()) {
//            case DISCONNECTED:
//                progressDialog.dismiss();
//                tipDialog("连接失败，请确定设备已打开");
////                equipment = eqpList.get(0);
////                equipment.setText("离线");
////                adapter.notifyDataSetChanged();
//                break;
//            case SERVICE_DISCOVERED:
//                progressDialog.dismiss();
//                tipDialog("连接成功");
////                equipment = eqpList.get(0);
////                equipment.setText("在线");
////                adapter.notifyDataSetChanged();
//                break;
//        }
//    }
//
//    private void showProgressDialog(){
//        progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setMessage("正在连接设备...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//    }
//
//    private void tipDialog(String msg){
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("提示");
//        builder.setMessage(msg);
//        // 点击对话框以外的区域是否让对话框消失
//        builder.setCancelable(false);
//
//        if (msg.equals("连接成功")) {
//            Message message = new Message();
//            message.what = 1;
//            handler.sendMessageDelayed(message, 1000);
//        } else {
//            builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if (!address.equals("N/A")) {
//                        progressDialog.show();
//                        connectEqp();
//                    }
//                }
//            });
//        }
//        dialog = builder.create();      //创建AlertDialog对象
//        dialog.show();
//    }
//
//    private void connectEqp() {
//        ConnectionConfiguration config = new ConnectionConfiguration();
//        config.setConnectTimeoutMillis(10000);
//        config.setRequestTimeoutMillis(1000);
//        config.setAutoReconnect(false);
//        if (!address.equals("N/A")) {
//            EasyBLE.getInstance().connect(address, config);
//        }
//    }

    private void requestBluetoothEnable() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Log.d(TAG, "MainActivity onCreate: 请求蓝牙");
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

}