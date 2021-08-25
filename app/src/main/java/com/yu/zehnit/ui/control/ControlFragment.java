package com.yu.zehnit.ui.control;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.MainActivity;
import com.yu.zehnit.MyApplication;
import com.yu.zehnit.R;
import com.yu.zehnit.VideoPlayerActivity;
import com.yu.zehnit.tools.Bluetooth;
import com.yu.zehnit.tools.CtrlAdapter;
import com.yu.zehnit.tools.MyCtrl;
import com.yu.zehnit.tools.OnRecycleViewItemClickListener;
import com.yu.zehnit.tools.SharedPreferencesUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.wandersnail.ble.Connection;
import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.ble.RequestBuilderFactory;
import cn.wandersnail.ble.WriteCharacteristicBuilder;
import cn.wandersnail.ble.WriteOptions;
import cn.wandersnail.commons.util.StringUtils;
import cn.wandersnail.widget.textview.SwitchButton;

public class ControlFragment extends Fragment {

    private ControlViewModel controlViewModel;
    private static final String TAG = "dxx";

    private List<MyCtrl> ctrlList = new ArrayList<>();
    private boolean[] clickedStatus = new boolean[5]; // 记录点击状态
    private SwitchButton sbTarget;
    private boolean startFlag=false;
    private View.OnClickListener mVideoClick;
    private View.OnClickListener mSettingClick;
    private View.OnClickListener mStartClick;
    private Bluetooth bluetooth;

    private TextView textCtrl;

    private Connection connection;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        controlViewModel =
                new ViewModelProvider(this).get(ControlViewModel.class);
        View root = inflater.inflate(R.layout.fragment_control, container, false);

        if (EasyBLE.getInstance().getOrderedConnections().size() != 0) {
            connection = EasyBLE.getInstance().getOrderedConnections().get(0);
        }
        bluetooth=new Bluetooth();
        initCtrl();

        RecyclerView recyclerView = root.findViewById(R.id.recycle_view_ctrl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        CtrlAdapter adapter = new CtrlAdapter(ctrlList);
        recyclerView.setAdapter(adapter);
        adapter.setVideoClickListener(new CtrlAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v,int pos) {
                if (!startFlag) {
                    Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                    intent.putExtra("ID", pos);
                    startActivity(intent);
                    // 监听数据改变
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adapter.setSettingClickListener(new CtrlAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v,int pos) {
                if (!startFlag) {
                    switch (pos) {
                        case 0:
                            break;
                        case 1:
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Settings");
                            builder.setMessage("Speed");
                            final SeekBar seek = new SeekBar(getActivity());
                            seek.setMax(100);
                            seek.setKeyProgressIncrement(1);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.setView(seek);
                            builder.show();
                            break;
                        case 2:
                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setTitle("Settings");
                            builder1.setMessage("Frequency:");
                            final SeekBar seek1 = new SeekBar(getActivity());
                            seek1.setMax(100);
                            seek1.setKeyProgressIncrement(1);
                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder1.setView(seek1);
                            builder1.show();
                            break;
                        case 3:
                            final AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                            builder2.setTitle("Settings");
                            builder2.setMessage("Gain");
                            final SeekBar seek2 = new SeekBar(getActivity());
                            seek2.setMax(100);
                            seek2.setKeyProgressIncrement(1);
                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder2.setView(seek2);
                            builder2.show();
                            break;
                        case 4:
                        default:
                            break;
                    }
                    // 监听数据改变
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adapter.setStartClickListener(new CtrlAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v,int pos) {
                MyCtrl ctrl = ctrlList.get(pos);

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
                    startFlag=false;
                    // 更新点击状态
                    clickedStatus[pos] = false;
                } else if (index == -1){
                    // 没有item处于点击状态
                    // 记录点击状态
                    clickedStatus[pos] = true;
                    // 设置选中状态的图片
                    ctrlOn(ctrl, pos);
                    startFlag=true;
                    // 设置选中状态的cardView颜色和字体颜色
                    //cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                   // textView.setTextColor(getResources().getColor(R.color.white));
                   // textView.setBackgroundResource(R.drawable.custom_label_background);

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


     /*   adapter.setListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onClick(View v,int pos) {
                MyCtrl ctrl = ctrlList.get(pos);
                CardView cardView = v.findViewById(R.id.card_view);
                TextView textView = cardView.findViewById(R.id.text_ctrl);
                ImageView imageCtrl=cardView.findViewById(R.id.img_ctrl);
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
                   // cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                   // textView.setTextColor(getResources().getColor(R.color.colorGray));
                    textView.setBackgroundResource(R.drawable.custom_label_background_uncheck);

                    // 更新点击状态
                    clickedStatus[pos] = false;
                } else if (index == -1){
                    // 没有item处于点击状态
                    // 记录点击状态
                    clickedStatus[pos] = true;
                    // 设置选中状态的图片
                    ctrlOn(ctrl, pos);

                    // 设置选中状态的cardView颜色和字体颜色
                    //cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setBackgroundResource(R.drawable.custom_label_background);

                    // 点击该选项进行的操作
                    ctrlTracking(pos);

                } else {
                    // 其他item处于点击状态
                    Toast.makeText(getContext(), "请关闭之前的控制开关，再进行新的操作", Toast.LENGTH_SHORT).show();
                }
                // 监听数据改变
                adapter.notifyDataSetChanged();
            }
        });*/
       // recyclerView.setAdapter(adapter);

       /* sbTarget = root.findViewById(R.id.switch_target);
        sbTarget.setChecked(MyApplication.getInstance().getTargetIsOn());
//        Log.d(TAG, "onCreateView: 视靶是否打开了..." + MyApplication.getInstance().getTargetIsOn());
        sbTarget.setBackColor(SwitchButton.generateBackColor(ContextCompat.getColor(getContext(), R.color.colorGreen)));
        sbTarget.setThumbColor(SwitchButton.getDefaultThumbColor());
        sbTarget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte[] data = null;
                if (connection != null) {
                    if (isChecked) {
                        MyApplication.getInstance().setTargetIsOn(true);
                        data = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, (byte) 0xff, (byte) 0xC0};
                    } else {
                        MyApplication.getInstance().setTargetIsOn(false);
                        data = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, 0x00, (byte) 0xC0};
                    }
                    bluetooth.writeCharacteristic(connection,data);
                }
            }
        });*/

        return root;
    }

    private void ctrlOff(MyCtrl ctrl, int pos){
        byte[] data = new byte[10];
        switch (pos) {
            case 0:
                ctrl.setImgId(R.drawable.gaze_holding_uncheck_1);

                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 1:
                ctrl.setImgId(R.drawable.pursuit_uncheck);
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 2:
                ctrl.setImgId(R.drawable.saccades_uncheck);
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 3:
                ctrl.setImgId(R.drawable.gaze_holding_uncheck_2);
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 4:
                ctrl.setImgId(R.drawable.gaze_holding_uncheck_3);
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
        }
        // move the motor to the center
        data = new byte[]{(byte) 0xC0, 0x01, 0x12, 0x00, 0x00, (byte) 0xC0};
        bluetooth.writeCharacteristic(connection,data);
        // switch off the laser
        data = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, 0x00, (byte) 0xC0};
        bluetooth.writeCharacteristic(connection,data);
        ctrl.setSwitchImgId(R.drawable.switch_off);
        ctrl.setTextColor(getResources().getColor(R.color.colorGray));
        ctrl.setTextBackground(R.drawable.custom_label_background_uncheck);
        initVideoSettingLayout();

    }

    private void ctrlOn(MyCtrl ctrl, int pos) {
        switch (pos) {
            case 0:
                ctrl.setImgId(R.drawable.gaze1);
                break;
            case 1:
                ctrl.setImgId(R.drawable.pursuit);
                break;
            case 2:
                ctrl.setImgId(R.drawable.saccade);
                break;
            case 3:
                ctrl.setImgId(R.drawable.gaze2);
                break;
            case 4:
                ctrl.setImgId(R.drawable.gaze3);
                break;

        }
        ctrl.setSwitchImgId(R.drawable.switch_on);
        ctrl.setTextBackground(R.drawable.custom_label_background);
        ctrl.setTextColor(getResources().getColor(R.color.white));
        initVideoSettingLayout();
        // switch on the laser
        byte[] data = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, (byte) 0xff, (byte) 0xC0};
        bluetooth.writeCharacteristic(connection,data);

    }

    private void ctrlTracking(int pos) {
        SharedPreferencesUtils.setFileName("data");
        byte[] data = new byte[10];
        byte[] temp;
       // byte[] frequency;
       // byte[] amplitude;
        switch (pos) {
            case 0:
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, (byte)0x80, (byte)0x3f, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 1:
                data[0] = (byte) 0xC0;
                data[1] = 0x01;
                data[2] = 0x14;
                data[3] = 0x00;
                data[4] = 0x04;
                data[9] = (byte) 0xC0;
                float pursuitFrequency = (float) SharedPreferencesUtils.getParam(getContext(), "pursuit_frequency", 0.0f);
                float pursuitAmplitude = (float) SharedPreferencesUtils.getParam(getContext(), "pursuit_amplitude", 0.0f);
                temp=getByteArray(pursuitFrequency);
                // 倒着对应
                for (int i = 0; i < temp.length; i++) {
                    data[8 - i] = temp[i];
                }
                Log.d(TAG, "ctrlTracking: 频率 " + StringUtils.toHex(data));
                bluetooth.writeCharacteristic(connection,data);

                // 幅度
                data[2] = 0x13;
                temp=getByteArray(pursuitAmplitude);
                for (int i = 0; i < temp.length; i++) {
                    data[8 - i] = temp[i];
                }
                Log.d(TAG, "ctrlTracking: 幅度 " + StringUtils.toHex(data));
                bluetooth.writeCharacteristic(connection,data);
                // 模式
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x02, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 2:
                // 频率
                data[0] = (byte) 0xC0;
                data[1] = 0x01;
                data[2] = 0x14;
                data[3] = 0x00;
                data[4] = 0x04;
                data[9] = (byte) 0xC0;
                float saccadeFrequency = (float) SharedPreferencesUtils.getParam(getContext(), "saccade_frequency", 0.0f);
                temp=getByteArray(saccadeFrequency);
                    for (int i = 0; i < temp.length; i++) {
                        data[8 - i] = temp[i];
                    }
                    bluetooth.writeCharacteristic(connection,data);

                //幅度
                data = new byte[]{(byte) 0xC0, 0x01, 0x13, 0x00, 0x04, 0x00, 0x00, (byte)0xa0, 0x40, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                // 模式
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x01, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 3:

                // 增益
                data[0] = (byte) 0xC0;
                data[1] = 0x01;
                data[2] = 0x11;
                data[3] = 0x00;
                data[4] = 0x04;
                data[9] = (byte) 0xC0;
                float gainValue = (float) SharedPreferencesUtils.getParam(getContext(), "gain", 0.0f);
                temp=getByteArray(gainValue);
                for (int i = 0; i < temp.length; i++) {
                    data[8 - i] = temp[i];
                }
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 4:
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x40, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);

                break;

        }
    }
    public void initVideoSettingLayout()
    {
        if(!startFlag) {
            for (int i = 0; i < ctrlList.size(); i++) {
                ctrlList.get(i).setVideoId(R.drawable.control_video_play_uncheck);
                ctrlList.get(i).setSettingId(R.drawable.control_setting_uncheck);
                ctrlList.get(i).setVideoColor(getResources().getColor(R.color.colorLine));
                ctrlList.get(i).setSettingColor(getResources().getColor(R.color.colorLine));
            }
        }
        else{
            for (int i = 0; i < ctrlList.size(); i++) {
                ctrlList.get(i).setVideoId(R.drawable.control_video_play);
                ctrlList.get(i).setSettingId(R.drawable.control_setting);
                ctrlList.get(i).setVideoColor(getResources().getColor(R.color.colorPrimary));
                ctrlList.get(i).setSettingColor(getResources().getColor(R.color.colorPrimary));
            }
        }

    }


    private void initCtrl(){
        MyCtrl ctrlGaze1 = new MyCtrl(getString(R.string.gaze_holding1),R.color.colorGray,R.drawable.custom_label_background_uncheck, R.drawable.gaze_holding_uncheck_1,R.drawable.control_video_play,R.color.colorPrimary,R.drawable.control_setting,R.color.colorPrimary, R.drawable.switch_off);
        ctrlGaze1.setTextColor(getResources().getColor(R.color.colorGray));
        ctrlGaze1.setSettingColor(getResources().getColor(R.color.colorPrimary));
        ctrlGaze1.setVideoColor(getResources().getColor(R.color.colorPrimary));
        ctrlList.add(ctrlGaze1);
        MyCtrl ctrlTrack = new MyCtrl(getString(R.string.smooth_pursuit),R.color.colorGray,R.drawable.custom_label_background_uncheck, R.drawable.pursuit_uncheck, R.drawable.control_video_play,R.color.colorPrimary,R.drawable.control_setting,R.color.colorPrimary, R.drawable.switch_off);
        ctrlTrack.setTextColor(getResources().getColor(R.color.colorGray));
        ctrlTrack.setSettingColor(getResources().getColor(R.color.colorPrimary));
        ctrlTrack.setVideoColor(getResources().getColor(R.color.colorPrimary));
        ctrlList.add(ctrlTrack);
        MyCtrl ctrlSaccade = new MyCtrl(getString(R.string.saccades),R.color.colorGray,R.drawable.custom_label_background_uncheck, R.drawable.saccades_uncheck, R.drawable.control_video_play,R.color.colorPrimary,R.drawable.control_setting,R.color.colorPrimary, R.drawable.switch_off);
        ctrlSaccade.setTextColor(getResources().getColor(R.color.colorGray));
        ctrlSaccade.setSettingColor(getResources().getColor(R.color.colorPrimary));
        ctrlSaccade.setVideoColor(getResources().getColor(R.color.colorPrimary));
        ctrlList.add(ctrlSaccade);
        MyCtrl ctrlGaze2 = new MyCtrl(getString(R.string.gaze_holding2),R.color.colorGray,R.drawable.custom_label_background_uncheck, R.drawable.gaze_holding_uncheck_2,R.drawable.control_video_play,R.color.colorPrimary,R.drawable.control_setting,R.color.colorPrimary, R.drawable.switch_off);
        ctrlGaze2.setTextColor(getResources().getColor(R.color.colorGray));
        ctrlGaze2.setSettingColor(getResources().getColor(R.color.colorPrimary));
        ctrlGaze2.setVideoColor(getResources().getColor(R.color.colorPrimary));
        ctrlList.add(ctrlGaze2);
        MyCtrl ctrlGaze3 = new MyCtrl(getString(R.string.gaze_holding3),R.color.colorGray,R.drawable.custom_label_background_uncheck, R.drawable.gaze_holding_uncheck_3,R.drawable.control_video_play,R.color.colorPrimary,R.drawable.control_setting,R.color.colorPrimary, R.drawable.switch_off);
        ctrlGaze3.setTextColor(getResources().getColor(R.color.colorGray));
        ctrlGaze3.setSettingColor(getResources().getColor(R.color.colorPrimary));
        ctrlGaze3.setVideoColor(getResources().getColor(R.color.colorPrimary));
        ctrlList.add(ctrlGaze3);

    }
    public static byte[]getByteArray(float f){
        int intbits=Float.floatToIntBits(f);
        return getByteArray(intbits);
    }
    public static byte[]getByteArray(int i){
        byte[] b=new byte[4];
        b[0]=(byte)((i & 0xff000000)>>24);
        b[1]=(byte)((i & 0x00ff0000)>>16);
        b[2]=(byte)((i & 0x0000ff00)>>8);
        b[3]=(byte)(i & 0x000000ff);
        return b;
    }






}