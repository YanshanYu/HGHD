package com.yu.zehnit.ui.sessions;


import com.yu.zehnit.MainActivity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Session implements Serializable {
    public static final int AMOUNT_TASKS= MainActivity.TASKCAPTIONS.length;
    private int mNo;
    private Date mDate;
    private final int[] mTaskScores;


    public Session(){
        mNo=1;
        mDate= Calendar.getInstance().getTime();
        mTaskScores= new int[AMOUNT_TASKS];
        Arrays.fill(mTaskScores, 0);
    }
    public int getNo(){return mNo;}
    public void setNo(int no){mNo=no;}
    public Date getDate() {
        return mDate;
    }
    public void setDate(Date date){mDate=date;}


    public int getTotalScore() {
        int s=0;
        for (int mTaskScore : mTaskScores) s += mTaskScore;
        return s;
    }

    public void setTaskScore(int i, int score){
        mTaskScores[i]=score;
    }

    public int getTaskScore(int i){
        return mTaskScores[i];
    }
}
