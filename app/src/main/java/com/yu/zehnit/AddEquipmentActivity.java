package com.yu.zehnit;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;
import com.yu.zehnit.tools.SharedPreferencesUtils;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import cn.wandersnail.ble.Connection;
import cn.wandersnail.ble.ConnectionConfiguration;
import cn.wandersnail.ble.Device;
import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.ble.EventObserver;
import cn.wandersnail.ble.Request;
import cn.wandersnail.ble.RequestBuilder;
import cn.wandersnail.ble.RequestBuilderFactory;
import cn.wandersnail.ble.WriteCharacteristicBuilder;
import cn.wandersnail.ble.WriteOptions;
import cn.wandersnail.ble.callback.ReadCharacteristicCallback;
import cn.wandersnail.ble.callback.ScanListener;
import cn.wandersnail.commons.util.StringUtils;
import cn.wandersnail.widget.dialog.DefaultAlertDialog;

/**
 * 添加设备
 * 实现EventObserver接口定义观察者
 */
public class AddEquipmentActivity extends BaseActivity implements EventObserver {

    private static final String TAG = "dxx";

    private Toolbar toolbar;
    private Button btnAdd;
    private EditText textSn;
    private ProgressDialog progressDialog;

    private Connection connection;
    private Device mDevice;


    private boolean findDevice = false;
    private Message message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);

        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true)
                .fitsSystemWindows(true).init();

        // 状态栏返回
        toolbar = findViewById(R.id.toolbar_add);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd = findViewById(R.id.button_add);
        btnAdd.setEnabled(false);

        textSn = findViewById(R.id.editText_sn);

        textSn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnAdd.setEnabled(textSn.getText().length() != 0);
            }
        });

        // 添加监听
        EasyBLE.getInstance().addScanListener(scanListener);

        // 注册观察者
        EasyBLE.getInstance().registerObserver(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasGPS()) {
                    progressDialog = new ProgressDialog(AddEquipmentActivity.this);
                    progressDialog.setMessage(getString(R.string.scan_device));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    message = new Message();
                    message.what = 11;
                    handler.sendMessageDelayed(message, 12000);
                    EasyBLE.getInstance().startScan();
                } else {
                    // 提示请求位置服务
                    tipDialog(getString(R.string.no_gps), getString(R.string.turn_on));
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyBLE.getInstance().removeScanListener(scanListener);
        EasyBLE.getInstance().unregisterObserver(this);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 11 && (!findDevice)){
                progressDialog.dismiss();
                tipDialog(getString(R.string.can_not_find_device), getString(R.string.ok));
            }
        }
    };


    @Override
    public void onConnectionStateChanged(@NonNull Device device) {
        switch (device.getConnectionState()) {
            case SCANNING_FOR_RECONNECTION:
                break;
            case CONNECTING:
                Log.d(TAG, "AddEquipmentActivity onConnectionStateChanged: 正在连接...");
                break;
            case CONNECTED:
                Log.d(TAG, "AddEquipmentActivity onConnectionStateChanged: 已连接");
                break;
            case SERVICE_DISCOVERING:
                Log.d(TAG, "AddEquipmentActivity onConnectionStateChanged: 正在发现服务...");
                break;
            case SERVICE_DISCOVERED:
                Log.d(TAG, "AddEquipmentActivity onConnectionStateChanged: 已发现服务");
                progressDialog.setMessage(getString(R.string.connect_device));
                byte[] data = new byte[]{(byte) 0xC0, 0x01, 0x00, 0x00, 0x00, (byte) 0xC0};
                writeCharacteristic(data);
//                List<BluetoothGattService> services = connection.getGatt().getServices();
//                boolean flag = false;
//                for (BluetoothGattService service : services) {
//                    Log.d(TAG, "--------- AddEquipmentActivity onConnectionStateChanged: service uuid : ---------" + service.getUuid().toString());
//                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
//                    for (BluetoothGattCharacteristic characteristic : characteristics) {
//                        Log.d(TAG, "AddEquipmentActivity onConnectionStateChanged: characteristic uuid : " + characteristic.getUuid().toString());
//                        if (connection.hasProperty(service.getUuid(), characteristic.getUuid(), BluetoothGattCharacteristic.PROPERTY_WRITE)) {
//                            sUuid = service.getUuid();
//                            cUuid = characteristic.getUuid();
//                            writeCharacteristic(sUuid, cUuid);
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if (flag) break;
//                }
                break;
            case DISCONNECTED:
                break;

        }
    }

    @Override
    public void onCharacteristicWrite(@NonNull Request request, @NonNull byte[] value) {
        Log.d(TAG, "AddEquipmentActivity onCharacteristicWrite: 成功写入特征值：" + StringUtils.toHex(value));
        // 写入成功后读取返回的特征值
        readCharacteristic();
    }

    @Override
    public void onCharacteristicRead(@NonNull Request request, @NonNull byte[] value) {
        Log.e(TAG, "AddEquipmentActivity onCharacteristicWrite: 读取到特征值：" + StringUtils.toHex(value));

        // 获得返回的特征值的有效字段（SN码）
        byte[] validValue = new byte[8];
        for (int i = 0; i < validValue.length; i++) {
            validValue[i] = value[i + 5];
        }

        // 根据SN码添加设备
        addEqp(validValue);
        
    }

    /**
     * 提示信息框
     * @param msg 提示信息
     * @param btnText 按钮上的文字
     */
    private void tipDialog(String msg, String btnText) {
        DefaultAlertDialog dialog = new DefaultAlertDialog(AddEquipmentActivity.this);
        dialog.setTitle(getString(R.string.tip));
        dialog.setMessage(msg);
        dialog.setTitleBackgroundColor(-1);
        // 点击对话框以外的区域是否让对话框消失
        dialog.setCancelable(false);


        if (msg.equals(getString(R.string.connection_succeeded))) {
            //设置正面按钮
            dialog.setPositiveButton(btnText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else if (msg.equals(getString(R.string.wrong_SN))){
            dialog.setPositiveButton(btnText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EasyBLE.getInstance().disconnectAllConnections();
                    findDevice = false;
                    handler.removeMessages(message.what);
                }
            });
        } else if (msg.equals(getString(R.string.no_gps))){
            dialog.setPositiveButton(btnText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                }
            });
        } else {
            dialog.setPositiveButton(btnText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        dialog.show();                              //显示对话框
    }

    private void readCharacteristic(){
        RequestBuilder<ReadCharacteristicCallback> builder = new RequestBuilderFactory().getReadCharacteristicBuilder(MyApplication.SRVC_UUID, MyApplication.CHAR_UUID);
        builder.setTag(UUID.randomUUID().toString());
        builder.setPriority(Integer.MAX_VALUE);//设置请求优先级
        builder.build().execute(connection);
    }

    private void writeCharacteristic(byte[] data) {
        WriteCharacteristicBuilder builder = new RequestBuilderFactory().getWriteCharacteristicBuilder(MyApplication.SRVC_UUID,
                MyApplication.CHAR_UUID, data);
        //根据需要设置写入配置
        int writeType = connection.hasProperty(MyApplication.SRVC_UUID, MyApplication.CHAR_UUID,
                BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) ?
                BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE : BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT;
        builder.setWriteOptions(new WriteOptions.Builder()
                .setPackageSize(connection.getMtu() - 3)
                .setPackageWriteDelayMillis(5)
                .setRequestWriteDelayMillis(10)
                .setWaitWriteResult(true)
                .setWriteType(writeType)
                .build());
        //不设置回调，使用观察者模式接收结果
        builder.build().execute(connection);
    }


    private void addEqp(byte[] validValue) {

        if (StringUtils.toHex(validValue).replace(" ", "").equals(textSn.getText().toString())) {
            progressDialog.dismiss();
            //todo: 断开上一个设备的连接（若有的话），并将连接状态置为未连接
            saveEqp();
            tipDialog(getString(R.string.connection_succeeded), getString(R.string.ok));
        } else {
            progressDialog.dismiss();
            tipDialog(getString(R.string.wrong_SN), getString(R.string.try_again));
        }
    }

    private void saveEqp() {
        // 记录设备数量
        SharedPreferencesUtils.setFileName("info");
        int count = (int) SharedPreferencesUtils.getParam(AddEquipmentActivity.this, "eqpNum", 0);
        SharedPreferencesUtils.setParam(AddEquipmentActivity.this, "eqpNum", count + 1);
//        SharedPreferences pref = getSharedPreferences("eqpNum", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        int count = pref.getInt("num", 0);
//        editor.putInt("num", count + 1);
//        editor.apply();

        // 记录设备信息
        String fileName = "eqp" + (count + 1);
        SharedPreferencesUtils.setFileName(fileName);
        SharedPreferencesUtils.setParam(AddEquipmentActivity.this, "name", mDevice.getName());
        SharedPreferencesUtils.setParam(AddEquipmentActivity.this, "address", mDevice.getAddress());
//        pref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
//        editor = pref.edit();
//        editor.putString("name", mDevice.getName());
//        editor.putString("address", mDevice.getAddress());
//        editor.apply();

    }


    private boolean hasGPS() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gpsEnabled;
    }


    private ScanListener scanListener = new ScanListener() {
        @Override
        public void onScanStart() {
            Log.d(TAG, "onScanStart: 开始搜索");
        }

        @Override
        public void onScanStop() {
            Log.d(TAG, "onScanStop: 搜索结束");
        }

        /**
         * 搜索到BLE设备
         *
         * @param device           搜索到的设备
         * @param isConnectedBySys 是否已被系统蓝牙连接上
         */
        @Override
        public void onScanResult(@NonNull Device device, boolean isConnectedBySys) {
            //搜索结果
            Log.d(TAG, "onScanResult: 名称：" + device.getName() + "   地址：" + device.getAddress());
            if (device.getName().equals("VertiBand")) {
                findDevice = true;
                mDevice = device;
                // 找到设备停止扫描
                EasyBLE.getInstance().stopScan();

                //连接配置
                ConnectionConfiguration config = new ConnectionConfiguration();
                config.setConnectTimeoutMillis(10000);
                config.setRequestTimeoutMillis(1000);
                // 进行连接
                connection = EasyBLE.getInstance().connect(mDevice, config);
            }

        }

        @Override
        public void onScanError(int errorCode, @NotNull String errorMsg) {
            switch(errorCode) {
                case ScanListener.ERROR_LACK_LOCATION_PERMISSION://缺少定位权限
                    break;
                case ScanListener.ERROR_LOCATION_SERVICE_CLOSED://位置服务未开启
                    break;
                case ScanListener.ERROR_SCAN_FAILED://搜索失败
                    break;
            }
        }
    };

}