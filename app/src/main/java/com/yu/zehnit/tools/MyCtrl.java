package com.yu.zehnit.tools;

public class MyCtrl {

    private String mName;
    private int mTextBackground;
    private int mTextColor;
    private int mImgId;
    private int mVideoId;
    private int mVideoColor;
    private int mSettingId;
    private int mSettingColor;
    private int mSwitchImgId;

    public MyCtrl() {
    }

    public MyCtrl(String name,int textColor,int textBackground, int imgId,int videoId,int videoColor,int settingId,int settingColor, int switchImgId) {
        this.mName = name;
        this.mTextColor=textColor;
        this.mTextBackground=textBackground;
        this.mImgId = imgId;
        this.mVideoId=videoId;
        this.mVideoColor=videoColor;
        this.mSettingId=settingId;
        this.mSettingColor=settingColor;
        this.mSwitchImgId = switchImgId;
    }

    public String getName() {
        return mName;
    }
    public int getTextColor(){return mTextColor;}
    public void setTextColor(int textColor){this.mTextColor=textColor;}
    public int getTextBackground() {
        return mTextBackground;
    }
    public void setTextBackground(int textBackground) {
        this.mTextBackground = textBackground;
    }
    public int getImgId() {
        return mImgId;
    }
    public void setImgId(int imgId) {
        this.mImgId = imgId;
    }
    public int getVideoId(){ return mVideoId; }
    public void setVideoId(int videoId){this.mVideoId=videoId;}
    public int getVideoColor(){return mVideoColor;}
    public void setVideoColor(int videoColor){this.mVideoColor=videoColor;}
    public int getSettingId(){return  mSettingId; }
    public void setSettingId(int settingId){this.mSettingId=settingId;}
    public void setSettingColor(int settingColor){this.mSettingColor=settingColor;}
    public int getSettingColor(){return mSettingColor;}
    public int getSwitchImgId() {
        return mSwitchImgId;
    }
    public void setSwitchImgId(int switchImgId) {
        this.mSwitchImgId = switchImgId;
    }
}
