package com.yu.zehnit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.yu.zehnit.tools.Bluetooth;
import com.yu.zehnit.tools.Task;

import cn.wandersnail.ble.Connection;
import cn.wandersnail.ble.EasyBLE;

public class TaskActivity extends BaseActivity {
    public static final String TASK="Task";
    private Task mTask;
    private TextView mtvRemaining;
    private ImageView mivHead;
    private TextView mtvTitle;
    private int mDuration;
    private CountDownTimer mTimer;
    private Toolbar toolbar;
    private Bluetooth bluetooth;
    private Connection conn;
    private ImageButton btnRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        bluetooth=new Bluetooth();
        if (EasyBLE.getInstance().getOrderedConnections().size() != 0) {
            conn = EasyBLE.getInstance().getOrderedConnections().get(0);
        }
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
        mDuration= mTask.getDuration()* mTask.getVariants();
        mtvRemaining.setText(mDuration+"s");
        setTitle();

        // 监听返回按钮
     /*   toolbar = findViewById(R.id.toolbar_task);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    private void setTitle() {
        mtvTitle.setText(mTask.getCaption()+":  "+this.getString(R.string.variant)+"  "+(mTask.getCurrentVariant()+1));
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putSerializable(TASK,mTask);
        super.onSaveInstanceState(savedInstanceState);
    }
    public void onVideoClick(View view) {
        Intent intent=new Intent(this,VideoPlayerActivity.class);
        intent.putExtra("ID",mTask.getTaskNo());
        startActivity(intent);
    }

    public void onRunClick(View view){
        btnRun.setEnabled(false);
        PerformTaskCommand();
        mTimer=new CountDownTimer(mDuration*1000,1000) {
            @Override
            public void onTick(long l) {
                int rest=(int)l/1000;
                int elapsed=mTask.getDuration()*mTask.getVariants()-rest;
                if(elapsed%mTask.getDuration()==0&&elapsed!=0) {
                    setupNextVariant();
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
        Intent i=new Intent();
        i.putExtra(TASK,mTask);
        setResult(Activity.RESULT_OK,i);
        EndTaskCommand();
        finish();
    }
    public void setupNextVariant(){
        mTask.setVariantScore(mTask.getMaxScore());
        if(mTask.incVariant()) {
            setTitle();
            PerformTaskCommand();
            Log.e("setupNextVariant","current task"+mTask.getFrequency());
        }
      //  Log.e("yu","current task"+mTask.getFrequency());
    }
    public void PerformTaskCommand(){
        byte[] temp;
        int index=0;
        byte[]data;
        float fre=mTask.getFrequency();
        float gain=mTask.getGain();
        Log.e("yu","current task"+mTask.getFrequency());
        byte[] laserOnCommand = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, (byte) 0xff, (byte) 0xC0};
        bluetooth.writeCharacteristic(conn,laserOnCommand);
        switch (mTask.getTaskNo()) {
            //Focus
            case 0:
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
                byte[] purFreData = new byte[10];
                purFreData[0] = (byte) 0xC0;
                purFreData[1] = 0x01;
                purFreData[2] = 0x14;    //set frequency
                purFreData[3] = 0x00;
                purFreData[4] = 0x04;
                purFreData[9] = (byte) 0xC0;
                temp=getByteArray(fre);
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
                temp=getByteArray(45.0f);
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
                byte[]jumpFreData=new byte[10];
                jumpFreData[0] = (byte) 0xC0;
                jumpFreData[1] = 0x01;
                jumpFreData[2] = 0x14;    //set frequency
                jumpFreData[3] = 0x00;
                jumpFreData[4] = 0x04;
                jumpFreData[9] = (byte) 0xC0;
                temp=getByteArray(fre);
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

                //Gaze
            case 4:
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
        }
    }
    public void EndTaskCommand(){
        byte[] data;
        byte[] laserOffCommand = new byte[]{(byte) 0xC0, 0x01, 0x10, 0x00, 0x01, 0x00, (byte) 0xC0};
        bluetooth.writeCharacteristic(conn,laserOffCommand);
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