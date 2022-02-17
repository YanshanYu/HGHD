package com.yu.zehnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.print.PrintAttributes;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.yu.zehnit.tools.Bluetooth;
import com.yu.zehnit.tools.Data;
import com.yu.zehnit.tools.MySurfaceView;
import com.yu.zehnit.tools.PaintView;
import com.yu.zehnit.tools.SharedPreferencesUtils;
import com.yu.zehnit.tools.TargetView;

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

public class TVModeActivity extends BaseActivity implements EventObserver {
    private static String DEBUG="TVModeActivity";
    private View tv;
    private ActionBar actionBar;
  //  private MySurfaceView mGLSurfaceView;
    private ConstraintLayout constraintLayout;
    private boolean isShowActionBar=false;
    private float frequency=0.0f;
    private float gain=0.0f;
    private int no=0;
    private long t=0;
    private Timer timer;
    private float msPeriodDuration=100.0f;
    private int width;
    private int height;
    private int tv_width;
    private int tv_height;
    private int tv_distance;
    private Bundle bundle;
    private float pitch, yaw,roll;
    private Bluetooth bluetooth;
    private Connection conn;
    private float offset_yaw=0;
    private boolean setOffsetFlag=false;

    private SoundPool mSoundPool;
    private int beepId;

    MediaPlayer mplayerBeforeStart;
    MediaPlayer[] mMediaPlayer=new MediaPlayer[2];

    private boolean voicePlaying=true;
    private boolean beepVoice=false;
    private String language;
    private boolean stopPlayAlert=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tvmode);
        //调用3D头像显示
       // mGLSurfaceView=new MySurfaceView(this);
      //  setContentView(mGLSurfaceView);
        bluetooth=new Bluetooth();
        if (EasyBLE.getInstance().getOrderedConnections().size() != 0) {
            conn = EasyBLE.getInstance().getOrderedConnections().get(0);
        }
        // 注册观察者
        EasyBLE.getInstance().registerObserver(this);
        StartOutputGyroData();
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
        language=(String) SharedPreferencesUtils.getParam(this, "language", "zh");
        if("zh".equals(language)){
            mMediaPlayer[0]= MediaPlayer.create(this,R.raw.alert_no_move_head_zh);
            mMediaPlayer[1]=MediaPlayer.create(this,R.raw.alert_move_head_zh);
        }else{
            mMediaPlayer[0]=MediaPlayer.create(this,R.raw.alert_no_move_head_en);
            mMediaPlayer[1]=MediaPlayer.create(this,R.raw.alert_move_head_en);
        }

        mMediaPlayer[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayAlert=true;
            }
        });
        mMediaPlayer[1].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayAlert=true;
            }
        });
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        actionBar=this.getSupportActionBar();
        actionBar.hide();

        width=getWindowManager().getDefaultDisplay().getWidth()-20;
        height=getWindowManager().getDefaultDisplay().getHeight()-20;
        SharedPreferencesUtils.setFileName("tv_setting");
        tv_width=(int)SharedPreferencesUtils.getParam(TVModeActivity.this, "tv_width", 10);
        tv_height=(int)SharedPreferencesUtils.getParam(TVModeActivity.this,"tv_height",10);
        tv_distance=(int)SharedPreferencesUtils.getParam(TVModeActivity.this,"tv_distance",100);
      /*  tv=new TargetView(mGLSurfaceView.getContext());
        FrameLayout.LayoutParams paramsBT=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsBT.gravity= Gravity.CENTER;
        this.addContentView(tv,paramsBT);*/
        tv=(View)findViewById(R.id.target_view);
       // final ImageButton imageButton=new ImageButton(mGLSurfaceView.getContext());
        final ImageButton imageButton=new ImageButton(this);
        imageButton.setBackgroundResource(R.drawable.back_white);
        FrameLayout.LayoutParams paramsImageButton=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsImageButton.gravity=Gravity.LEFT;
       // paramsImageButton.topMargin = 0;
        this.addContentView(imageButton,paramsImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                finish();
            }
        });
        constraintLayout=findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offset_yaw=yaw;
                ResetOffset(offset_yaw);
            }
        });
       /* mGLSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGLSurfaceView.Zero();
            }
        });*/
        Intent intent = this.getIntent();
        bundle=intent.getExtras();
        no=bundle.getInt("No");
        frequency=bundle.getFloat("frequency",0);
        gain=bundle.getFloat("gain",0);
        gain=1-gain;
        if(frequency>0) msPeriodDuration=100.0f/frequency;
        switch(no){
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
                    mplayerBeforeStart.release();
                    mplayerBeforeStart=null;
                    timer=new Timer();
                    timer.schedule(task,1000,10);
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mplayerBeforeStart!=null) mplayerBeforeStart.release();
        if(mMediaPlayer[0]!=null) mMediaPlayer[0].release();
        if(mMediaPlayer[1]!=null) mMediaPlayer[1].release();
        setNotificationDisable();
        timer.cancel();

        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            timer.cancel();
            if(mplayerBeforeStart!=null) mplayerBeforeStart.release();
            if(mMediaPlayer[0]!=null) mMediaPlayer[0].release();
            if(mMediaPlayer[1]!=null) mMediaPlayer[1].release();
            setNotificationDisable();
            Intent i = new Intent();
            i.putExtra("tv_break", 0);
            setResult(Activity.RESULT_OK);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    Handler handler =new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:   //focus

                    break;
                case 1:   //pursuit
                   // tv.setTranslationX(width/2*(float)Math.sin(2*Math.PI*t/msPeriodDuration));

                    float x=(float)(width*tv_distance*Math.tan(Math.PI*15/180))/tv_width;
                    tv.setTranslationX(x*(float)Math.sin(2*Math.PI*t/msPeriodDuration));
                    break;
                case 2:   //saccades
                    if(t%(int)msPeriodDuration==0) tv.setTranslationX(getNextSaccades());
                    break;
                case 3:   //shake
                  //  if(Math.abs(yaw-offset_yaw)<=30) {
                    float l0=gain * (float) (width * tv_distance * Math.tan(Math.PI*(yaw - offset_yaw) / 180)) / tv_width;

                    if(l0 >= width/2) tv.setTranslationX(width/2);
                    else if(l0<=-width/2) tv.setTranslationX(-width/2);
                    else tv.setTranslationX(l0);
                  //  }
                  //  else{
                  //      tv.setTranslationX(-((float) (width * tv_distance * Math.tan(Math.PI * 30 / 180)) / tv_width));
                   // }
                    break;
                case 4:   //gaze
                  //  if(Math.abs(yaw-offset_yaw)<=30) {
                    float l1=gain*((float) (width * tv_distance * Math.tan(Math.PI * (yaw - offset_yaw) / 180)) / tv_width);
                    if(l1 >= width/2)
                        tv.setTranslationX(width/2);
                    else if(l1<=-width/2)
                        tv.setTranslationX(-width/2);
                    else tv.setTranslationX(l1);
                    break;
            }
        }
    };
    TimerTask task=new TimerTask() {
        @Override
        public void run() {
            t++;
            // if(t>msPeriodDuration)t=0;
            Message message=new Message();
            message.what=no;
            handler.sendMessage(message);
        }
    };
    int tmp = 0;
    int oldPos;
    public int getNextSaccades() {
        oldPos=tmp;
        do{
            tmp = (int) (width * Math.random()) - width / 2;
        }while(Math.abs(tmp)<width/5);
      /*  if (tmp < width / 5 && tmp > 0) {
            tmp += width / 5;
        } else if (tmp > -width / 5 && tmp < 0) {
            tmp -= width / 5;
        }*/
        return tmp;
    }
    public void StartOutputGyroData(){
        byte[] GyroDataStartCommand=new byte[]{(byte) 0xC0, 0x01, 0x18, 0x00, 0x01, 0x01, (byte) 0xC0};
        bluetooth.writeCharacteristic(conn,GyroDataStartCommand);
    }
    public void StopOutputGyroData(){
        byte[] GyroDataStopCommand=new byte[]{(byte) 0xC0, 0x01, 0x18, 0x00, 0x01, 0x00, (byte) 0xC0};
        bluetooth.writeCharacteristic(conn,GyroDataStopCommand);
    }
    private void setNotificationEnable(){
        RequestBuilder<NotificationChangeCallback> builder = new RequestBuilderFactory().getSetNotificationBuilder(MyApplication.SRVC_UUID, MyApplication.CHAR_UUID, true);
        builder.build().execute(conn);
    }
    private void setNotificationDisable(){
        RequestBuilder<NotificationChangeCallback> builder = new RequestBuilderFactory().getSetNotificationBuilder(MyApplication.SRVC_UUID, MyApplication.CHAR_UUID, false);
        builder.build().execute(conn);
    }

    @Override
    public void onCharacteristicRead(@NonNull Request request, @NonNull byte[] value) {

    }

    @Override
    public void onCharacteristicChanged(@NonNull Device device, @NonNull UUID service, @NonNull UUID characteristic, @NonNull byte[] value) {
        if(value[2]==0x18&&value.length==18){
            byte[] pitchByte=new byte[4];
            byte[] yawByte=new byte[4];
            byte[] rollByte=new byte[4];
            for(int i=0;i<4;i++){
                pitchByte[i]=value[i+5];
                yawByte[i]=value[i+9];
                rollByte[i]=value[i+13];
            }
         //   pitch=getFloat(pitchByte);
            yaw=getFloat(yawByte);
         //   roll=getFloat(rollByte);
          //  Data.setXdu(pitch);
          //  Data.setYdu(yaw);
          //  Data.setZdu(roll);
          //  Log.e(DEBUG,"pitch: "+ String.valueOf(pitch));
          //  Log.e(DEBUG,"yaw: "+ String.valueOf(yaw));
          //  Log.e(DEBUG,"roll: "+ String.valueOf(roll));
            if(!setOffsetFlag){
                offset_yaw=yaw;
                setOffsetFlag=true;
            }
        }
    }

    @Override
    public void onCharacteristicWrite(@NonNull Request request, @NonNull byte[] value) {
        Log.d(DEBUG, "AddEquipmentActivity onCharacteristicWrite: 成功写入特征值：" + StringUtils.toHex(value));
        // 写入成功后读取返回的特征值
        if(value[2]==0x18) {
            readCharacteristic();
        }
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
                //progressDialog.setMessage(getString(R.string.connect_device));
                break;
            case DISCONNECTED:
                tipDialog(getString(R.string.connection_failed));
                break;

        }
    }
    private void tipDialog(String msg){
        DefaultAlertDialog dialog = new DefaultAlertDialog(TVModeActivity.this);
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
    public static float getFloat(byte[] b) {
        int accum = 0;
        accum = accum|(b[0] & 0xff) << 0;
        accum = accum|(b[1] & 0xff) << 8;
        accum = accum|(b[2] & 0xff) << 16;
        accum = accum|(b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }
    public void ResetOffset(float offset){
        byte[] offsetYawData = new byte[10];
        byte[] temp;
        offsetYawData[0] = (byte) 0xC0;
        offsetYawData[1] = 0x01;
        offsetYawData[2] = 0x14;
        offsetYawData[3] = 0x00;
        offsetYawData[4] = 0x04;
        offsetYawData[9] = (byte) 0xC0;
        temp=getByteArray(offset);
        // 倒着对应
        for (int i = 0; i < temp.length; i++) {
            offsetYawData[8 - i] = temp[i];
        }
        bluetooth.writeCharacteristic(conn,offsetYawData);
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