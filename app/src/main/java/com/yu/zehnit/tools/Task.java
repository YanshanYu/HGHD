package com.yu.zehnit.tools;

import java.io.Serializable;

public class Task implements Serializable {

    private final int mTaskNo;
    private int mCurrentVariant;
    private String mCaption;
    private final int[] mMaxScores;
    private final int mDuration;
    private final float[] mFrequencies;
    //private final float[] mAmplitute;
    private final float[] mGains;
    private final boolean mPlayStimulus;
    private final int[] mScores;
    private final int mVariants;

    public Task(int taskno, String caption, int duration, float[] frequencies, float[] gains, int[] scores, boolean playstimulus){
        mCaption=caption;
        mDuration=duration;
        mFrequencies =frequencies;
        mGains =gains;
        mMaxScores=scores;
        mPlayStimulus=playstimulus;
        mTaskNo=taskno;
        mCurrentVariant=0;
        mVariants=mMaxScores.length;
        mScores= new int[mVariants];
    }

    public int getTaskNo() {
        return mTaskNo;
    }

    public int getVariants(){return mVariants;}

    public float getFrequency() {
        return mFrequencies[mCurrentVariant];
    }

    public float getGain() {
        return mGains[mCurrentVariant];
    }

    public int getDuration() {
        return mDuration;
    }

    public int getMaxScore() {
        return mMaxScores[mCurrentVariant];
    }

    public void setVariantScore(int score) {
        mScores[mCurrentVariant]=score;
    }

    public String getCaption() {
        return mCaption;
    }
    public void setmCaption(String caption){mCaption=caption;}

    public int getTotalScore(){
        int s=0;
        for(int i=0;i<mFrequencies.length;i++)s+=mScores[i];
        return s;
    }

    public boolean incVariant(){
        mCurrentVariant++;
        return (mCurrentVariant<mFrequencies.length);
    }

    public int getCurrentVariant(){
        return mCurrentVariant;
    }

    public boolean getPlayStimulus(){
        return mPlayStimulus;
    }


}
