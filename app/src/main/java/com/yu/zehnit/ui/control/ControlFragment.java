package com.yu.zehnit.ui.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.ParamSettingActivity;
import com.yu.zehnit.R;
import com.yu.zehnit.VideoPlayerActivity;
import com.yu.zehnit.tools.Bluetooth;
import com.yu.zehnit.tools.CtrlAdapter;
import com.yu.zehnit.tools.MyCtrl;
import com.yu.zehnit.tools.OnRecycleViewItemClickListener;
import com.yu.zehnit.tools.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import cn.wandersnail.ble.Connection;
import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.commons.util.StringUtils;
import cn.wandersnail.widget.textview.SwitchButton;

public class ControlFragment extends Fragment {

    private ControlViewModel controlViewModel;
    private static final String TAG = "dxx";

    private List<MyCtrl> ctrlList = new ArrayList<>();
    private boolean[] clickedStatus = new boolean[5]; // 记录点击状态
    private SwitchButton sbTarget;
    private boolean startFlag=false;
    private Bluetooth bluetooth;
    private Connection connection;
    private LinearLayout btVideoPlay;
    private LinearLayout btSettings;
    private ImageView ivVideoPlay;
    private TextView tvVideoPlay;
    private ImageView ivSettings;
    private TextView tvSettings;
    private ImageView btStart;
    private LinearLayout optionLayout;
    private int clickIndex=-1;
    private static final String FILE_NAME="param_data";
    private float paramValue;

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
        optionLayout=root.findViewById(R.id.option_layout);
        btVideoPlay=root.findViewById(R.id.btn_video_play);
        ivVideoPlay=root.findViewById(R.id.ivvideo_play);
        tvVideoPlay=root.findViewById(R.id.tvvideo_play);
        btSettings=root.findViewById(R.id.btn_settings);
        ivSettings=root.findViewById(R.id.ivsettings);
        tvSettings=root.findViewById(R.id.tvsettings);
        btStart=root.findViewById(R.id.btn_start);


        optionLayout.setVisibility(View.INVISIBLE);
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view_ctrl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        CtrlAdapter adapter = new CtrlAdapter(ctrlList);
        recyclerView.setAdapter(adapter);
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
                    startFlag=false;
                    // 设置未选中状态的cardView颜色和字体颜色
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                    //textView.setTextColor(getResources().getColor(R.color.colorGray));
                    // 更新点击状态
                    clickedStatus[pos] = false;
                    initcardView(false);
                    EndCommand(pos);
                } else if (index == -1){
                    // 没有item处于点击状态
                    // 记录点击状态
                    clickedStatus[pos] = true;
                    // 设置选中状态的图片
                    ctrlOn(ctrl, pos);
                    clickIndex=pos;
                    // 设置选中状态的cardView颜色和字体颜色
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    //textView.setTextColor(getResources().getColor(R.color.white));
                    initcardView(true);

                } else {
                    // 其他item处于点击状态
                    Toast.makeText(getContext(), "请关闭之前的控制开关，再进行新的操作", Toast.LENGTH_SHORT).show();
                }
                // 监听数据改变
                adapter.notifyDataSetChanged();
            }
        });
        btVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!startFlag) {
                    Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                    intent.putExtra("ID", clickIndex);
                    startActivity(intent);
                }
            }
        });
        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.setFileName(FILE_NAME);
                if(!startFlag){
                    switch (clickIndex) {
                        case 0:
                            break;
                        case 1:

                            final AlertDialog builder1 = new AlertDialog.Builder(getActivity()).create();
                            builder1.show();
                            builder1.getWindow().setContentView(R.layout.alert_dialog_layout);
                            TextView captionSettings1=builder1.findViewById(R.id.tvsettings_name);
                            ImageView captionSettingsImg1=builder1.findViewById(R.id.icon_settings);
                            TextView captionParam1=builder1.findViewById(R.id.tvparam_name);
                            TextView captionParamValue1=builder1.findViewById(R.id.param_value);
                            SeekBar seekBar1=builder1.findViewById(R.id.param_seekBar);
                            Button btnCancel1=builder1.findViewById(R.id.btn_cancel);
                            Button btnOk1=builder1.findViewById(R.id.btn_ok);
                            captionSettings1.setText(getResources().getString(R.string.smooth_pursuit));
                            captionSettingsImg1.setImageResource(R.drawable.frequency);
                            captionParam1.setText(getResources().getString(R.string.control_speed));
                            paramValue=(float) SharedPreferencesUtils.getParam(builder1.getContext(), "pursuit_fre", 0.0f);
                            captionParamValue1.setText(String.valueOf(paramValue)+"Hz");
                            seekBar1.setProgress((int)(paramValue*100));
                            seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    captionParamValue1.setText((float)progress/100+"Hz");
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    paramValue=(float)seekBar.getProgress()/100;
                                }
                            });
                            btnCancel1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    builder1.dismiss();
                                }
                            });
                            btnOk1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferencesUtils.setParam(builder1.getContext(),"pursuit_fre",paramValue);
                                    builder1.dismiss();
                                }
                            });
                            break;
                        case 2:
                            final AlertDialog builder2 = new AlertDialog.Builder(getActivity()).create();
                            builder2.show();
                            builder2.getWindow().setContentView(R.layout.alert_dialog_layout);
                            TextView captionSettings2=builder2.findViewById(R.id.tvsettings_name);
                            ImageView captionSettingsImg2=builder2.findViewById(R.id.icon_settings);
                            TextView captionParam2=builder2.findViewById(R.id.tvparam_name);
                            TextView captionParamValue2=builder2.findViewById(R.id.param_value);
                            SeekBar seekBar2=builder2.findViewById(R.id.param_seekBar);
                            Button btnCancel2=builder2.findViewById(R.id.btn_cancel);
                            Button btnOk2=builder2.findViewById(R.id.btn_ok);
                            captionSettings2.setText(getResources().getString(R.string.saccades));
                            captionSettingsImg2.setImageResource(R.drawable.frequency);
                            captionParam2.setText(getResources().getString(R.string.frequency));
                            paramValue=(float) SharedPreferencesUtils.getParam(builder2.getContext(), "saccades_fre", 0.0f);
                            captionParamValue2.setText(String.valueOf(paramValue)+"Hz");
                            seekBar2.setProgress((int)(paramValue*100));
                            seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    captionParamValue2.setText((float)progress/100+"Hz");
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    paramValue=(float)seekBar.getProgress()/100;

                                }
                            });
                            btnCancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    builder2.dismiss();
                                }
                            });
                            btnOk2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferencesUtils.setParam(builder2.getContext(),"saccades_fre",paramValue);
                                    builder2.dismiss();
                                }
                            });
                            break;
                        case 3:
                            break;
                        case 4:
                            final AlertDialog builder3 = new AlertDialog.Builder(getActivity()).create();
                            builder3.show();
                            builder3.getWindow().setContentView(R.layout.alert_dialog_layout);
                            TextView captionSettings3=builder3.findViewById(R.id.tvsettings_name);
                            ImageView captionSettingsImg3=builder3.findViewById(R.id.icon_settings);
                            TextView captionParam3=builder3.findViewById(R.id.tvparam_name);
                            TextView captionParamValue3=builder3.findViewById(R.id.param_value);
                            SeekBar seekBar3=builder3.findViewById(R.id.param_seekBar);
                            Button btnCancel3=builder3.findViewById(R.id.btn_cancel);
                            Button btnOk3=builder3.findViewById(R.id.btn_ok);
                            captionSettings3.setText(getResources().getString(R.string.control_gaze));
                            captionSettingsImg3.setImageResource(R.drawable.frequency);
                            captionParam3.setText(getResources().getString(R.string.control_gain));
                            seekBar3.setMax(10);
                            paramValue=(float) SharedPreferencesUtils.getParam(builder3.getContext(), "gain", 0.0f);
                            seekBar3.setProgress((int)(paramValue*10));
                            captionParamValue3.setText(String.valueOf(paramValue));
                            seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    captionParamValue3.setText((float)progress/10+"");
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    paramValue=(float)seekBar.getProgress()/10;
                                }
                            });
                            btnCancel3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    builder3.dismiss();
                                }
                            });
                            btnOk3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferencesUtils.setParam(builder3.getContext(),"gain",paramValue);
                                    builder3.dismiss();
                                }
                            });
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!startFlag){
                    // 点击该选项进行的操作
                    PerformCommand(clickIndex);
                    btStart.setImageResource(R.drawable.icon_end);
                }else{
                    EndCommand(clickIndex);
                    btStart.setImageResource(R.drawable.play);
                }
                startFlag=!startFlag;
            }
        });




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
    private void initcardView(boolean b){
        btVideoPlay.setEnabled(b);
        btSettings.setEnabled(b);
        btStart.setEnabled(b);
        if(b) {
            optionLayout.setVisibility(View.VISIBLE);
            ivVideoPlay.setImageResource(R.drawable.control_video_play);
            tvVideoPlay.setTextColor(getResources().getColor(R.color.colorPrimary));
            ivSettings.setImageResource(R.drawable.control_setting);
            tvSettings.setTextColor(getResources().getColor(R.color.colorPrimary));
            btStart.setImageResource(R.drawable.play);
        }else{
            optionLayout.setVisibility(View.INVISIBLE);
            ivVideoPlay.setImageResource(R.drawable.control_video_play_uncheck);
            tvVideoPlay.setTextColor(getResources().getColor(R.color.colorBlueGray));
            ivSettings.setImageResource(R.drawable.control_setting_uncheck);
            tvSettings.setTextColor(getResources().getColor(R.color.colorBlueGray));
            btStart.setImageResource(R.drawable.icon_end);
        }
    }

    private void ctrlOff(MyCtrl ctrl, int pos){
        switch (pos) {
            case 0:
                ctrl.setImgId(R.drawable.icon_focus_uncheck);
                break;
            case 1:
                ctrl.setImgId(R.drawable.icon_pursuit_uncheck);
                break;
            case 2:
                ctrl.setImgId(R.drawable.icon_saccades_uncheck);
                break;
            case 3:
                ctrl.setImgId(R.drawable.icon_shake_uncheck);
                break;
            case 4:
                ctrl.setImgId(R.drawable.icon_gaze_uncheck);
                break;
        }
        ctrl.setSwitchImgId(R.drawable.switch_off);
        ctrl.setTextColor(getResources().getColor(R.color.colorGray));

    }


    private void ctrlOn(MyCtrl ctrl, int pos) {
        switch (pos) {
            case 0:
                ctrl.setImgId(R.drawable.icon_focus_checked);
                break;
            case 1:
                ctrl.setImgId(R.drawable.icon_pursuit_checked);
                break;
            case 2:
                ctrl.setImgId(R.drawable.icon_saccades_checked);
                break;
            case 3:
                ctrl.setImgId(R.drawable.icon_shake_checked);
                break;
            case 4:
                ctrl.setImgId(R.drawable.icon_gaze_checked);
                break;
        }
        ctrl.setSwitchImgId(R.drawable.switch_on);
        ctrl.setTextColor(getResources().getColor(R.color.white));
    }

    public void EndCommand(int pos){
        byte[] data;
        byte[] laserOffCommand = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, 0x00, (byte) 0xC0};
        bluetooth.writeCharacteristic(connection,laserOffCommand);
        switch(pos){
            case 0:
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 1:
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 2:
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 3:
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);

                break;
            case 4:
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
        }
        data = new byte[]{(byte) 0xC0, 0x01, 0x12, 0x00, 0x00, (byte) 0xC0};
        bluetooth.writeCharacteristic(connection,data);
    }

    private void PerformCommand(int pos) {
        SharedPreferencesUtils.setFileName(FILE_NAME);
        byte[] data = new byte[10];
        byte[] temp;
        //switch on laser
        byte[] laserOnCommand = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, (byte) 0xff, (byte) 0xC0};
        bluetooth.writeCharacteristic(connection,laserOnCommand);

        switch (pos) {
            case 0:
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);
                break;
            case 1:
                data[0] = (byte) 0xC0;
                data[1] = 0x01;
                data[2] = 0x14;
                data[3] = 0x00;
                data[4] = 0x04;
                data[9] = (byte) 0xC0;
                float pursuitFrequency = (float) SharedPreferencesUtils.getParam(getContext(), "pursuit_fre", 0.0f);
                temp=getByteArray(pursuitFrequency);
                // 倒着对应
                for (int i = 0; i < temp.length; i++) {
                    data[8 - i] = temp[i];
                }
                Log.d(TAG, "ctrlTracking: 频率 " + StringUtils.toHex(data));
                Log.d(TAG, "ctrlTracking: 频率 " + pursuitFrequency);
                bluetooth.writeCharacteristic(connection,data);
                // 幅度
                byte[] data1=new byte[10];
                data1[0] = (byte) 0xC0;
                data1[1] = 0x01;
                data1[2] = 0x13;
                data1[3] = 0x00;
                data1[4] = 0x04;
                data1[9] = (byte) 0xC0;

              //  float pursuitAmplitude = (float) SharedPreferencesUtils.getParam(getContext(), "pursuit_amplitude", 0.0f);
                float pursuitAmplitude=30;
                temp=getByteArray(pursuitAmplitude);
                for (int i = 0; i < temp.length; i++) {
                    data1[8 - i] = temp[i];
                }
                bluetooth.writeCharacteristic(connection,data1);
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
                float saccadeFrequency = (float) SharedPreferencesUtils.getParam(getContext(), "saccades_fre", 0.0f);
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
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x40, (byte) 0xC0};
                bluetooth.writeCharacteristic(connection,data);

                break;
            case 4:

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
        }
    }


    private void initCtrl(){
        MyCtrl ctrlFocus = new MyCtrl(getString(R.string.control_focus),R.color.colorGray,R.drawable.icon_focus_uncheck,R.drawable.switch_off);
        ctrlFocus.setTextColor(getResources().getColor(R.color.colorGray));
        ctrlList.add(ctrlFocus);
        MyCtrl ctrlPursuit = new MyCtrl(getString(R.string.smooth_pursuit),R.color.colorGray, R.drawable.icon_pursuit_uncheck,R.drawable.switch_off);
        ctrlPursuit.setTextColor(getResources().getColor(R.color.colorGray));
        ctrlList.add(ctrlPursuit);
        MyCtrl ctrlSaccades = new MyCtrl(getString(R.string.saccades),R.color.colorGray,R.drawable.icon_saccades_uncheck,R.drawable.switch_off);
        ctrlSaccades.setTextColor(getResources().getColor(R.color.colorGray));
        ctrlList.add(ctrlSaccades);
        MyCtrl ctrlShake = new MyCtrl(getString(R.string.control_shake),R.color.colorGray, R.drawable.icon_shake_uncheck,R.drawable.switch_off);
        ctrlShake.setTextColor(getResources().getColor(R.color.colorGray));
        ctrlList.add(ctrlShake);
        MyCtrl ctrlGaze = new MyCtrl(getString(R.string.control_gaze),R.color.colorGray, R.drawable.icon_gaze_uncheck,R.drawable.switch_off);
        ctrlGaze.setTextColor(getResources().getColor(R.color.colorGray));
        ctrlList.add(ctrlGaze);


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