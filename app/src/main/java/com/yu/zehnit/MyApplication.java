package com.yu.zehnit;

import android.app.Application;
import android.bluetooth.le.ScanSettings;

import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.ble.ScanConfiguration;
import cn.wandersnail.ble.ScannerType;
import cn.wandersnail.commons.base.AppHolder;
import cn.wandersnail.commons.poster.ThreadMode;

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        AppHolder.initialize(this);

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
