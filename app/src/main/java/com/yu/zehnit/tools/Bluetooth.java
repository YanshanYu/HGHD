package com.yu.zehnit.tools;

import android.bluetooth.BluetoothGattCharacteristic;

import com.yu.zehnit.MyApplication;

import cn.wandersnail.ble.Connection;
import cn.wandersnail.ble.RequestBuilderFactory;
import cn.wandersnail.ble.WriteCharacteristicBuilder;
import cn.wandersnail.ble.WriteOptions;

public class Bluetooth {
    public Bluetooth(){ }
    public void writeCharacteristic(Connection connection,byte[] data) {

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
}
