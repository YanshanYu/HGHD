package com.yu.zehnit;
import com.gyf.immersionbar.ImmersionBar;
import com.yu.zehnit.ui.sessions.Session;
import com.yu.zehnit.tools.Task;
import com.yu.zehnit.ui.sessions.SessionDataManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.Date;

import cn.wandersnail.ble.EventObserver;

public class SessionActivity extends BaseActivity implements EventObserver {
    private static final String NUM="num";
    public static final String SESSION="Session";
    private static final String PERFORMED="Performed";
    private static final int REQUEST_TASK=1;

    private int SessionNumber;
    private final int AMOUNTTASKS = MainActivity.TASKCAPTIONS.length;
    private Session mSession;
    private final TextView[] mtvCaptions= new TextView[AMOUNTTASKS];
    private final TextView[] mtvScores= new TextView[AMOUNTTASKS];
    private final String[] captionString=new String[5];
    private TextView mtvTotalScore;
    private boolean[] mIsPerformed;
    private TextView sessionTitle;
    private Toolbar toolbar;

    // we have 5 tasks, each (but SPN) has 3 variants
    private final int[]mMaxVars={1,3,3,3,3};
    // Duration in seconds for each tasks, all variants of a task have the same duration
    private final int[]mDurations={10,10,10,10,10};
    private final int[][] mScores ={{10,0,0},{15,16,17},{18,19,20},{21,22,23},{24,25,26}};
    //Parameter set for the task variants
    private final float[][]mFrequencies={{0,0,0},{0.2f,0.4f,0.6f},{0.2f,0.4f,0.6f},{0,0,0},{0,0,0}};
    private final float[][]mGains={{0,0,0},{0,0,0},{0,0,0},{0.4f,0.7f,1},{0,-0.3f,-0.6f}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true)
                .fitsSystemWindows(true).init();
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setResult(Activity.RESULT_CANCELED);
        sessionTitle=this.findViewById(R.id.session_title);
        mtvCaptions[0]=findViewById(R.id.tvspn);
        mtvCaptions[1]=findViewById(R.id.tvpursuit);
        mtvCaptions[2]=findViewById(R.id.tvsaccades);
        mtvCaptions[3]=findViewById(R.id.tvvorsupp);
        mtvCaptions[4]=findViewById(R.id.tvvortrain);
        mtvScores[0]=findViewById(R.id.tvscorespn);
        mtvScores[1]=findViewById(R.id.tvscorepursuit);
        mtvScores[2]=findViewById(R.id.tvscoresacc);
        mtvScores[3]=findViewById(R.id.tvscorevorsupp);
        mtvScores[4]=findViewById(R.id.tvscorevortrain);
        mtvTotalScore=findViewById(R.id.tvscoretotal);
        if(savedInstanceState!=null)
        {
            SessionNumber=savedInstanceState.getInt(NUM);
            mSession=(Session) savedInstanceState.getSerializable(SESSION);
            mIsPerformed=savedInstanceState.getBooleanArray(PERFORMED);
        }else{
            SessionNumber=getIntent().getIntExtra("NO",0);
            if(SessionNumber< SessionDataManager.getSize()){
                mSession=SessionDataManager.getSession(SessionNumber);
                sessionTitle.setText(this.getString(R.string.sessions)+(SessionNumber));
                for(int i=0;i<5;i++){
                    mtvScores[i].setText(Integer.toString(mSession.getTaskScore(i)));
                }
                mtvTotalScore.setText(Integer.toString(mSession.getTotalScore()));

            }else{
                mSession=new Session();
                mIsPerformed=new boolean[MainActivity.TASKCAPTIONS.length];
                for(int i=0;i<MainActivity.TASKCAPTIONS.length;i++){
                    mIsPerformed[i]=false;
                }
                sessionTitle.setText(this.getString(R.string.sessions)+(SessionNumber+1));
            }
        }


       // for(int i=0;i<AMOUNTTASKS;i++){
            //mtvCaptions[i].setText(MainActivity.TASKCAPTIONS[i]);
        //    captionString[i]=mtvCaptions[i].getText().toString();
      //  }

        // 监听返回按钮
        toolbar = findViewById(R.id.toolbar_sessions);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(NUM, SessionNumber);
        savedInstanceState.putSerializable(SESSION,mSession);
        savedInstanceState.putBooleanArray(PERFORMED,mIsPerformed);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onBackPressed(){
        Intent i= new Intent();
        i.putExtra(SESSION,mSession);
        setResult(Activity.RESULT_OK,i);
        super.onBackPressed();
        finish();
    }

    public void onTaskClick(View view){
        int i=Integer.parseInt(String.valueOf(view.getTag()));
        startTask(i);
    }

    public void onNextClick(View view){
        for(int i=0; i<mIsPerformed.length;i++){
            if(mIsPerformed[i])continue;
            startTask(i);
            return;
        }
        onStoreClick(null);
    }

    public void onStoreClick(View view){
        Intent intent= new Intent();
        intent.putExtra(SESSION, mSession);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    private Task setupTask(int no){
        float[] frqs= new float[mMaxVars[no]];
        float[] gains= new float[mMaxVars[no]];
        int[] scores= new int[mMaxVars[no]];
        for(int i=0;i<mMaxVars[no];i++){
            frqs[i]=mFrequencies[no][i];
            gains[i]=mGains[no][i];
            scores[i]= mScores[no][i];
        }
        return new Task(no, mtvCaptions[no].getText().toString(),mDurations[no],frqs,gains,scores,no>2);
    }

    private void startTask(int no){
        Intent intent = new Intent(this,TaskActivity.class);
        intent.putExtra(TaskActivity.TASK,setupTask(no));
        startActivityForResult(intent,REQUEST_TASK);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=Activity.RESULT_OK) return;
        if(requestCode==REQUEST_TASK){
            Task t= (Task)data.getSerializableExtra(TaskActivity.TASK);
            mSession.setTaskScore(t.getTaskNo(),t.getTotalScore());
            mSession.setDate(new Date());
            mtvScores[t.getTaskNo()].setText(Integer.toString(t.getTotalScore()));
            mtvTotalScore.setText(Integer.toString(mSession.getTotalScore()));
           // if(!mIsPerformed[t.getTaskNo()])mIsPerformed[t.getTaskNo()]=t.getTotalScore()>0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}