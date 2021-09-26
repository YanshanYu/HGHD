package com.yu.zehnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yu.zehnit.tools.Bluetooth;
import com.yu.zehnit.tools.MovingAverage;
import com.yu.zehnit.tools.SharedPreferencesUtils;
import com.yu.zehnit.tools.Task;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import cn.wandersnail.ble.Connection;
import cn.wandersnail.ble.Device;
import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.ble.EventObserver;
import cn.wandersnail.ble.Request;
import cn.wandersnail.ble.RequestBuilder;
import cn.wandersnail.ble.RequestBuilderFactory;
import cn.wandersnail.ble.callback.NotificationChangeCallback;
import cn.wandersnail.ble.callback.ReadCharacteristicCallback;
import cn.wandersnail.commons.util.StringUtils;
import cn.wandersnail.widget.dialog.DefaultAlertDialog;

public class TaskActivity extends BaseActivity implements EventObserver {
    public static final String TASK="Task";
    private final static int REQUEST_ENABLE_BT = 1;

    private Task mTask;
    private TextView mtvRemaining;
    private ImageView mivHead;
    private TextView mtvTitle;
    private int mDuration;
    private CountDownTimer mTimer;
    private Timer mHeadMovetimer;
    private Bluetooth bluetooth;
    private Connection conn;
    private ImageButton btnRun;
    private ProgressDialog progressDialog;
    private float pitch,yaw,roll;
    private float offset_yaw=0;
    private boolean setOffsetFlag=false;
    private int moveHeadCount=0;
    private SoundPool mSoundPool;
    private int beepId;
    private long count=0;
    private boolean movementStart=false;
    private float currentFre;
    private MovingAverage movingAverage;
    MediaPlayer mMediaPlayer;
    MediaPlayer mplayerBeforeStart;
    private boolean voicePlaying=true;
    private boolean beepVoice=false;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        bluetooth=new Bluetooth();
        if (EasyBLE.getInstance().getOrderedConnections().size() != 0) {
            conn = EasyBLE.getInstance().getOrderedConnections().get(0);
        }
        // 注册观察者
        EasyBLE.getInstance().registerObserver(this);
        byte[] GyroDataOpenCommand=new byte[]{(byte) 0xC0, 0x01, 0x18, 0x00, 0x01, 0x01, (byte) 0xC0};
        bluetooth.writeCharacteristic(conn,GyroDataOpenCommand);
        setNotificationEnable();
        AudioAttributes abs=new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mSoundPool=new SoundPool.Builder().setMaxStreams(100)
        .setAudioAttributes(abs)
        .build();
        beepId=mSoundPool.load(this,R.raw.beep,1);
        //noMoveHeadId=mSoundPool.load(this,R.raw.alert_no_move_head,1);
        SharedPreferencesUtils.setFileName("info");
        language=(String) SharedPreferencesUtils.getParam(this, "language", "");
        if("zh".equals(language)){
            mMediaPlayer=MediaPlayer.create(this,R.raw.alert_no_move_head_zh);
        }else{
            mMediaPlayer=MediaPlayer.create(this,R.raw.alert_no_move_head_en);
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                voicePlaying=true;
            }
        });

        SwitchOnLaserCommand();
        movingAverage=new MovingAverage(25);
        mtvRemaining=findViewById(R.id.tvremaining);
        mivHead=findViewById(R.id.ivhead);
        btnRun=findViewById(R.id.btrun);
        if(savedInstanceState!=null)
        {
            mTask=(Task)savedInstanceState.getSerializable(TASK);
        }else{
            mTask=(Task) getIntent().getSerializableExtra(TASK);
        }
        mtvTitle=findViewById(R.id.tvtaskcaption);
        mDuration= mTask.getDuration()* mTask.getVariants()+8;
        mtvRemaining.setText(mDuration+"s");
        setTitle();
        mHeadMovetimer=new Timer();
        mHeadMovetimer.schedule(timerTask,1000,10);
        mivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offset_yaw=yaw;
            }
        });
    }


    protected void onResume() {
        super.onResume();
      //  offset_yaw=yaw;
    }
    final Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    moveHead(yaw-offset_yaw);
                   /* Log.e(TASK,String.valueOf(movingAverage.Average((int)(yaw-offset_yaw))));
                    if(!voicePlaying&&(Math.abs(movingAverage.Average((int)(yaw-offset_yaw)))>10)){
                        mMediaPlayer.start();
                        voicePlaying=true;
                    }*/
                   // Log.e(TASK,String.valueOf(movingAverage.Average((int)(yaw-offset_yaw))));
                    if(!voicePlaying&&(movingAverage.Average((int)(yaw-offset_yaw))>10)){
                        mMediaPlayer.start();

                    }
                  //  if(Math.abs(yaw-offset_yaw)>5)moveHeadCount++;
                    break;

            }
            super.handleMessage(msg);
        }
    };
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            count++;
            if(count%4==0) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
            if(currentFre!=0) {
                int k=(int)(50/currentFre);
                if (beepVoice && (count %k == 0)) mSoundPool.play(beepId, 1, 1, 1, 0, 1);
            }
        }
    };
    private void setTitle() {
        mtvTitle.setText(mTask.getCaption()+":  "+this.getString(R.string.variant)+"  "+(mTask.getCurrentVariant()+1));
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putSerializable(TASK,mTask);
        super.onSaveInstanceState(savedInstanceState);
    }
    public void onVideoClick(View view) {
        if(!movementStart) {
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.putExtra("ID", mTask.getTaskNo());
            startActivity(intent);
        }else{
            Toast.makeText(this, getString(R.string.task_toast_alert), Toast.LENGTH_SHORT).show();
        }

    }

    public void onRunClick(View view){
        btnRun.setEnabled(false);
        movementStart=true;

        switch (mTask.getTaskNo()){
            case 0:
                if("zh".equals(language)) {
                    mplayerBeforeStart = MediaPlayer.create(this, R.raw.alert_focus_zh);
                } else{
                    mplayerBeforeStart=MediaPlayer.create(this,R.raw.alert_focus_en);
                }
                break;
            case 1:
            case 2:
                if("zh".equals(language)) {
                    mplayerBeforeStart = MediaPlayer.create(this, R.raw.alert_pursuit_saccades_zh);
                }else{
                    mplayerBeforeStart=MediaPlayer.create(this,R.raw.alert_pursuit_saccades_en);
                }
                break;
            case 3:
            case 4:
                if("zh".equals(language)) {
                    mplayerBeforeStart = MediaPlayer.create(this, R.raw.alert_shake_gaze_zh);
                }else{
                    mplayerBeforeStart=MediaPlayer.create(this,R.raw.alert_shake_gaze_en);
                }
                break;
        }
        mplayerBeforeStart.start();
        if(null!=mplayerBeforeStart){
            mplayerBeforeStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    PerformTaskCommand();
                    mplayerBeforeStart.release();
                    mplayerBeforeStart=null;

                }
            });
        }

        mTimer=new CountDownTimer(mDuration*1000,1000) {
            @Override
            public void onTick(long l) {
                int rest=(int)l/1000;

                int elapsed=mTask.getDuration()*mTask.getVariants()-rest;
                if(elapsed%mTask.getDuration()==0&&elapsed!=0) {
                    EndTaskCommand();
                    mTask.setVariantScore(mTask.getMaxScore());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setupNextVariant();
                        }
                    },1000);


                }
                runOnUiThread(()->mtvRemaining.setText(rest+"s"));
            }

            @Override
            public void onFinish() {
                EndTask();
            }
        };
        mTimer.start();

    }
    public void onEndClick(View view){
        EndTask();
        btnRun.setEnabled(true);
    }
    private void EndTask(){
        if(null!=mTimer) mTimer.cancel();
        if(null!=mHeadMovetimer) mHeadMovetimer.cancel();

        Intent i=new Intent();
        i.putExtra(TASK,mTask);
        setResult(Activity.RESULT_OK,i);
        EndTaskCommand();
      //  EasyBLE.getInstance().unregisterObserver(this);
        movementStart=false;
        mSoundPool.unload(beepId);
        mMediaPlayer.release();
        SwitchOffLaserCommand();
        finish();
    }
    public void setupNextVariant(){

        if(mTask.incVariant()) {
            setTitle();
            PerformTaskCommand();
            Log.e("setupNextVariant","current task"+mTask.getFrequency());
        }
      //  Log.e("yu","current task"+mTask.getFrequency());
    }
    public void SwitchOnLaserCommand(){
        byte[] laserOnCommand = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, (byte) 0xff, (byte) 0xC0};
        bluetooth.writeCharacteristic(conn,laserOnCommand);
    }
    public void SwitchOffLaserCommand(){
        byte[] laserOffCommand = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, 0x00, (byte) 0xC0};
        bluetooth.writeCharacteristic(conn,laserOffCommand);
    }
    public void PerformTaskCommand(){
        byte[] temp;
        int index=0;
        byte[]data;
        currentFre=mTask.getFrequency();
        float gain=mTask.getGain();

            switch (mTask.getTaskNo()) {
            //Focus
            case 0:
                voicePlaying=false;
                byte[] focusGainData = new byte[10];
                focusGainData[0] = (byte) 0xC0;
                focusGainData[1] = 0x01;
                focusGainData[2] = 0x14;
                focusGainData[3] = 0x00;
                focusGainData[4] = 0x04;
                focusGainData[9] = (byte) 0xC0;
                temp=getByteArray(gain);

                // 倒着对应
                for (int i = 0; i < temp.length; i++) {
                    focusGainData[8 - i] = temp[i];
                }
                bluetooth.writeCharacteristic(conn,focusGainData);
                break;

            // Pursuit
            case 1:
                voicePlaying=false;
                byte[] purFreData = new byte[10];
                purFreData[0] = (byte) 0xC0;
                purFreData[1] = 0x01;
                purFreData[2] = 0x14;    //set frequency
                purFreData[3] = 0x00;
                purFreData[4] = 0x04;
                purFreData[9] = (byte) 0xC0;
                temp=getByteArray(currentFre);
                // 倒着对应
                for (int i = 0; i < temp.length; i++) {
                    purFreData[8 - i] = temp[i];
                }
                bluetooth.writeCharacteristic(conn,purFreData);
                // set amplitude  initial the amplitude as 45
                byte[] purAmpData = new byte[10];
                purAmpData[0] = (byte) 0xC0;
                purAmpData[1] = 0x01;
                purAmpData[2] = 0x13;    //set amplitude
                purAmpData[3] = 0x00;
                purAmpData[4] = 0x04;
                purAmpData[9] = (byte) 0xC0;
                temp=getByteArray(30.0f);
                // 倒着对应
                for (int i = 0; i < temp.length; i++) {
                    purAmpData[8 - i] = temp[i];
                }

                bluetooth.writeCharacteristic(conn,purAmpData);
                // 模式
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x02, (byte) 0xC0};
                bluetooth.writeCharacteristic(conn,data);
                break;

            //Jump
            case 2:
                voicePlaying=false;
                byte[]jumpFreData=new byte[10];
                jumpFreData[0] = (byte) 0xC0;
                jumpFreData[1] = 0x01;
                jumpFreData[2] = 0x14;    //set frequency
                jumpFreData[3] = 0x00;
                jumpFreData[4] = 0x04;
                jumpFreData[9] = (byte) 0xC0;
                temp=getByteArray(currentFre);
                // 倒着对应
                for (int i = 0; i < temp.length; i++) {
                    jumpFreData[8 - i] = temp[i];
                }
                bluetooth.writeCharacteristic(conn,jumpFreData);
                // set amplitude  initial the amplitude as 45
                data = new byte[]{(byte) 0xC0, 0x01, 0x13, 0x00, 0x04, 0x00, 0x00, (byte)0xa0, 0x40, (byte) 0xC0};
                bluetooth.writeCharacteristic(conn,data);
                // 模式
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x01, (byte) 0xC0};
                bluetooth.writeCharacteristic(conn,data);
                break;

            //Shake
            case 3:
                voicePlaying=true;
                beepVoice=true;
                byte[] shakeGainData=new byte[10];
                shakeGainData[0] = (byte) 0xC0;
                shakeGainData[1] = 0x01;
                shakeGainData[2] = 0x11;  // 增益
                shakeGainData[3] = 0x00;
                shakeGainData[4] = 0x04;
                shakeGainData[9] = (byte) 0xC0;
                temp=getByteArray(gain);
                // 倒着对应
                for (int i = 0; i < temp.length; i++) {
                    shakeGainData[8 - i] = temp[i];
                }
                bluetooth.writeCharacteristic(conn,shakeGainData);
                break;
            //Gaze
            case 4:
                voicePlaying=true;
                beepVoice=true;
                byte[] gazeGainData=new byte[10];
                gazeGainData[0] = (byte) 0xC0;
                gazeGainData[1] = 0x01;
                gazeGainData[2] = 0x11;     // 增益
                gazeGainData[3] = 0x00;
                gazeGainData[4] = 0x04;
                gazeGainData[9] = (byte) 0xC0;
                temp=getByteArray(gain);
                // 倒着对应
                for (int i = 0; i < temp.length; i++) {
                    gazeGainData[8 - i] = temp[i];
                }
                bluetooth.writeCharacteristic(conn,gazeGainData);
                break;
        }
    }
    public void EndTaskCommand(){
        byte[] data;

        switch(mTask.getTaskNo()){
            case 0:
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(conn,data);
                break;
            case 1:
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(conn,data);
                break;
            case 2:
                data = new byte[]{(byte) 0xC0, 0x01, 0x16, 0x00, 0x01, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(conn,data);
                break;
            case 3:
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(conn,data);

                break;
            case 4:
                data = new byte[]{(byte) 0xC0, 0x01, 0x11, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0};
                bluetooth.writeCharacteristic(conn,data);
                break;
        }
        data = new byte[]{(byte) 0xC0, 0x01, 0x12, 0x00, 0x00, (byte) 0xC0};
        bluetooth.writeCharacteristic(conn,data);
        byte[] GyroDataStopCommand=new byte[]{(byte) 0xC0, 0x01, 0x18, 0x00, 0x01, 0x00, (byte) 0xC0};
        bluetooth.writeCharacteristic(conn,GyroDataStopCommand);
        setNotificationDisable();
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
    public static float getFloat(byte[] b) {
        int accum = 0;
        accum = accum|(b[0] & 0xff) << 0;
        accum = accum|(b[1] & 0xff) << 8;
        accum = accum|(b[2] & 0xff) << 16;
        accum = accum|(b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }
    private void moveHead(float angles){
        mivHead.setPivotX(mivHead.getWidth()/2);
        mivHead.setPivotY(mivHead.getHeight()/2);
        mivHead.setRotation(angles);
    }



    @Override
    public void onCharacteristicRead(@NonNull Request request, @NonNull byte[] value) {

    }

    @Override
    public void onCharacteristicChanged(@NonNull Device device, @NonNull UUID service, @NonNull UUID characteristic, @NonNull byte[] value) {
      //  Log.e(TASK, "Gyro data String:" + StringUtils.toHex(value));
        if(value[2]==0x18&&value.length==18){
            byte[] pitchByte=new byte[4];
            byte[] yawByte=new byte[4];
            byte[] rollByte=new byte[4];
            for(int i=0;i<4;i++){
                pitchByte[i]=value[i+5];
                yawByte[i]=value[i+9];
                rollByte[i]=value[i+13];
            }
            pitch=getFloat(pitchByte);
            yaw=getFloat(yawByte);
            roll=getFloat(rollByte);
          //  Log.d(TASK, "Gyro data:" + pitch +"    "+yaw+"    "+roll);
            if(!setOffsetFlag){
                offset_yaw=yaw;
                setOffsetFlag=true;
            }
        }

    }

    @Override
    public void onCharacteristicWrite(@NonNull Request request, @NonNull byte[] value) {
        Log.d(TASK, "AddEquipmentActivity onCharacteristicWrite: 成功写入特征值：" + StringUtils.toHex(value));
        // 写入成功后读取返回的特征值
        if(value[2]==0x18) {
            readCharacteristic();
        }
    }
    private void setNotificationEnable(){
        RequestBuilder<NotificationChangeCallback> builder = new RequestBuilderFactory().getSetNotificationBuilder(MyApplication.SRVC_UUID, MyApplication.CHAR_UUID, true);
        builder.build().execute(conn);
    }
    private void setNotificationDisable(){
        RequestBuilder<NotificationChangeCallback> builder = new RequestBuilderFactory().getSetNotificationBuilder(MyApplication.SRVC_UUID, MyApplication.CHAR_UUID, false);
        builder.build().execute(conn);
    }

    private void readCharacteristic(){
        RequestBuilder<ReadCharacteristicCallback> builder = new RequestBuilderFactory().getReadCharacteristicBuilder(MyApplication.SRVC_UUID, MyApplication.CHAR_UUID);
        builder.setTag(UUID.randomUUID().toString());
        builder.setPriority(Integer.MAX_VALUE);//设置请求优先级
        builder.setCallback(new ReadCharacteristicCallback() {
          //  @RunOn(ThreadMode.BACKGROUND)
            @Override
            public void onCharacteristicRead(@NonNull Request request, @NonNull byte[] value) {
                Log.d("EasyBLE", "主线程：" + (Looper.getMainLooper() == Looper.myLooper()) + ", 读取到特征值：" + StringUtils.toHex(value, " "));
              //  ToastUtils.showShort("读取到特征值：" + StringUtils.toHex(value, " "));
            }

            @Override
            public void onRequestFailed(@NonNull Request request, int failType, @Nullable Object value) {

            }
        });
        builder.build().execute(conn);
    }

    @Override
    public void onConnectionStateChanged(@NonNull Device device) {
        switch (device.getConnectionState()) {
            case SCANNING_FOR_RECONNECTION:
                break;
            case CONNECTING:
              //  Log.d(TAG, "AddEquipmentActivity onConnectionStateChanged: 正在连接...");
                break;
            case CONNECTED:
              //  Log.d(TAG, "AddEquipmentActivity onConnectionStateChanged: 已连接");
                break;
            case SERVICE_DISCOVERING:
             //   Log.d(TAG, "AddEquipmentActivity onConnectionStateChanged: 正在发现服务...");
                break;
            case SERVICE_DISCOVERED:
             //   Log.d(TAG, "AddEquipmentActivity onConnectionStateChanged: 已发现服务");
                progressDialog.setMessage(getString(R.string.connect_device));
                break;
            case DISCONNECTED:
                tipDialog(getString(R.string.connection_failed));
                break;

        }
    }


    private void tipDialog(String msg){
        DefaultAlertDialog dialog = new DefaultAlertDialog(TaskActivity.this);
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
                  //  progressDialog.show();

                }
            });
        } else {
            dialog.setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // progressDialog.dismiss();
                   // requestBluetoothEnable();
                }
            });
        }
        dialog.show();
    }

    private void requestBluetoothEnable() {
        if (!EasyBLE.getInstance().isBluetoothOn()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

}