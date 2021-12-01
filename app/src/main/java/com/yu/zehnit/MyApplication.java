package com.yu.zehnit;

import android.app.Application;
import android.bluetooth.le.ScanSettings;
import android.util.Log;
import com.yu.zehnit.tools.EqpAdapter;
import com.yu.zehnit.tools.Equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.ble.ScanConfiguration;
import cn.wandersnail.ble.ScannerType;
import cn.wandersnail.commons.base.AppHolder;
import cn.wandersnail.commons.poster.ThreadMode;



/**
 * 在这里定义一些全局变量，比如：设备列表和设备适配器
 */
public class MyApplication extends Application {

    public static final UUID SRVC_UUID = UUID.fromString("a5ee0033-b9b0-421b-bfba-09662b4be396");
    public static final UUID CHAR_UUID = UUID.fromString("4054303d-aa2d-45f4-9664-bbf2c9627f0c");

    private static MyApplication instance;
    private List<Equipment> eqpList = new ArrayList<>();
    private EqpAdapter adapter;
    private boolean isConnected = false;
    private boolean targetIsOn = false;

    public boolean getTargetIsOn() {
        return targetIsOn;
    }

    public void setTargetIsOn(boolean targetIsOn) {
        this.targetIsOn = targetIsOn;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public List<Equipment> getEqpList() {
        return eqpList;
    }

    public void setEqpList(List<Equipment> eqpList) {
        this.eqpList = eqpList;
    }

    public EqpAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(EqpAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        AppHolder.initialize(this);
       // initBleManager();


        //构建自定义实例，需要在EasyBLE.getInstance()之前
        ScanConfiguration scanConfig = new ScanConfiguration()
                .setScanSettings(new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)//搜索模式
                        .build())
                .setScanPeriodMillis(10000)//搜索周期
                .setAcceptSysConnectedDevice(true)
                .setOnlyAcceptBleDevice(true);
        EasyBLE ble = EasyBLE.getBuilder().setScanConfiguration(scanConfig)
                .setObserveAnnotationRequired(false)//不强制使用{@link Observe}注解才会收到被观察者的消息，强制使用的话，性能会好一些
                .setMethodDefaultThreadMode(ThreadMode.MAIN)//指定回调方法和观察者方法的默认线程
                .setScannerType(ScannerType.LE)//指定蓝牙扫描器，默认为系统Android5.0以上使用ScannerType.LE
                .build();
        ble.initialize(this);

    }

    public static MyApplication getInstance() {
        return instance;
    }

}
