package com.yu.zehnit.ui.control;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.MyApplication;
import com.yu.zehnit.R;
import com.yu.zehnit.tools.CtrlAdapter;
import com.yu.zehnit.tools.MyCtrl;
import com.yu.zehnit.tools.OnRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.wandersnail.ble.Connection;
import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.ble.RequestBuilderFactory;
import cn.wandersnail.ble.WriteCharacteristicBuilder;
import cn.wandersnail.ble.WriteOptions;
import cn.wandersnail.widget.dialog.DefaultAlertDialog;
import cn.wandersnail.widget.textview.SwitchButton;

public class ControlFragment extends Fragment {

    private ControlViewModel controlViewModel;
    private static final String TAG = "dxx";

    private List<MyCtrl> ctrlList = new ArrayList<>();
    private boolean[] clickedStatus = new boolean[5]; // 记录点击状态
    private SwitchButton sbTarget;

    private Connection connection;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controlViewModel =
                new ViewModelProvider(this).get(ControlViewModel.class);
        View root = inflater.inflate(R.layout.fragment_control, container, false);

        if (EasyBLE.getInstance().getOrderedConnections().size() != 0) {
            connection = EasyBLE.getInstance().getOrderedConnections().get(0);
        }

        initCtrl();
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view_ctrl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        CtrlAdapter adapter = new CtrlAdapter(ctrlList);
        adapter.setListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onClick(View v,int pos) {
                MyCtrl ctrl = ctrlList.get(pos);
                CardView cardView = v.findViewById(R.id.card_view);
                TextView textView = cardView.findViewById(R.id.text_ctrl);
                int index = -1; // 处于点击状态的item的索引
                for (int i = 0; i < clickedStatus.length; i++) {
                    if (clickedStatus[i]) {
                        index = i;
                        break;
                    }
                }
                if (index == pos) {
                    // 该项是已点击状态
                    // 设置未选中状态的图片
                    ctrlOff(ctrl, pos);
                    // 设置未选中状态的cardView颜色和字体颜色
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    textView.setTextColor(getResources().getColor(R.color.colorGray));
                    // 更新点击状态
                    clickedStatus[pos] = false;
                } else if (index == -1){
                    // 没有item处于点击状态
                    // 记录点击状态
                    clickedStatus[pos] = true;
                    // 设置选中状态的图片
                    ctrlOn(ctrl, pos);

                    // 设置选中状态的cardView颜色和字体颜色
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    textView.setTextColor(getResources().getColor(R.color.white));

                    // 点击该选项进行的操作
                    ctrlTracking(pos);

                } else {
                    // 其他item处于点击状态
                    Toast.makeText(getContext(), "请关闭之前的控制开关，再进行新的操作", Toast.LENGTH_SHORT).show();
                }
                // 监听数据改变
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);

        sbTarget = root.findViewById(R.id.switch_target);
        sbTarget.setBackColor(SwitchButton.generateBackColor(ContextCompat.getColor(getContext(), R.color.colorGreen)));
        sbTarget.setThumbColor(SwitchButton.getDefaultThumbColor());
        sbTarget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte[] data = null;
                if (connection != null) {
                    if (isChecked) {
//                    Toast.makeText(getContext(), "开关打开了", Toast.LENGTH_SHORT).show();
                        data = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, (byte) 0xff, (byte) 0xC0};
                    } else {
//                    Toast.makeText(getContext(), "开关关闭了", Toast.LENGTH_SHORT).show();
                        data = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, 0x00, (byte) 0xC0};
                    }
                    writeCharacteristic(data);
                }
            }
        });

        return root;
    }

    private void ctrlOff(MyCtrl ctrl, int pos){
        byte[] data = null;
        switch (pos) {
            case 0:
                ctrl.setImgId(R.drawable.gaze1);
                break;
            case 1:
                ctrl.setImgId(R.drawable.gaze2);
                break;
            case 2:
                ctrl.setImgId(R.drawable.gaze3);
                break;
            case 3:
                ctrl.setImgId(R.drawable.pursuit);
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x00, (byte) 0xC0};
                writeCharacteristic(data);
                break;
            case 4:
                ctrl.setImgId(R.drawable.saccade);
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x00, (byte) 0xC0};
                writeCharacteristic(data);
                break;
        }
        ctrl.setSwitchImgId(R.drawable.switch_off);
    }

    private void ctrlOn(MyCtrl ctrl, int pos) {
        switch (pos) {
            case 0:
                ctrl.setImgId(R.drawable.gaze1_2);
                break;
            case 1:
                ctrl.setImgId(R.drawable.gaze2_2);
                break;
            case 2:
                ctrl.setImgId(R.drawable.gaze3_2);
                break;
            case 3:
                ctrl.setImgId(R.drawable.pursuit_2);
                break;
            case 4:
                ctrl.setImgId(R.drawable.saccade_2);
                break;
        }
        ctrl.setSwitchImgId(R.drawable.switch_on);
    }

    private void ctrlTracking(int pos) {
        byte[] data = null;
        switch (pos) {
            case 0:

                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
               // data1=Integer.toHexString(Float.floatToIntBits(0.1f));
                data = new byte[]{(byte) 0xC0, 0x01, 0x13, 0x00, 0x04, 0x00, 0x00, (byte) 0xF0, 0x41, (byte) 0xC0};
                writeCharacteristic(data);
                data = new byte[]{(byte) 0xC0, 0x01, 0x14, 0x00, 0x04, (byte)0xCC, (byte)0xCC, (byte) 0xCC, (byte)0x3D, (byte) 0xC0};
                writeCharacteristic(data);
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x02, (byte) 0xC0};
                writeCharacteristic(data);
                break;
            case 4:
                data = new byte[]{(byte) 0xC0, 0x01, 0x13, 0x00, 0x04, 0x00, 0x00, (byte) 0xF0, 0x41, (byte) 0xC0};
                writeCharacteristic(data);
                data = new byte[]{(byte) 0xC0, 0x01, 0x14, 0x00, 0x04, (byte)0xCC, (byte)0xCC, (byte) 0xCC, (byte)0x3D, (byte) 0xC0};
                writeCharacteristic(data);
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x01, (byte) 0xC0};
                writeCharacteristic(data);
                break;
        }
    }

    public static byte[] float2byte(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;

    }


    private void initCtrl(){
        MyCtrl ctrlGaze1 = new MyCtrl(getString(R.string.gaze_holding1), R.drawable.gaze1, R.drawable.switch_off);
        ctrlList.add(ctrlGaze1);
        MyCtrl ctrlGaze2 = new MyCtrl(getString(R.string.gaze_holding2), R.drawable.gaze2, R.drawable.switch_off);
        ctrlList.add(ctrlGaze2);
        MyCtrl ctrlGaze3 = new MyCtrl(getString(R.string.gaze_holding3), R.drawable.gaze3, R.drawable.switch_off);
        ctrlList.add(ctrlGaze3);
        MyCtrl ctrlTrack = new MyCtrl(getString(R.string.smooth_pursuit), R.drawable.pursuit,  R.drawable.switch_off);
        ctrlList.add(ctrlTrack);
        MyCtrl ctrlSaccade = new MyCtrl(getString(R.string.saccades), R.drawable.saccade, R.drawable.switch_off);
        ctrlList.add(ctrlSaccade);
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
}