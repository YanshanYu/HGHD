package com.yu.zehnit.tools;

public class MyCtrl {

    private String mName;
   // private int mTextBackground;
    private int mTextColor;
    private int mImgId;
    private int switchImgId;

    public MyCtrl() {
    }

    public MyCtrl(String name,int textColor, int imgId,int switchImgId) {
        this.mName = name;
        this.mTextColor=textColor;
      //  this.mTextBackground=textBackground;
        this.mImgId = imgId;
        this.switchImgId=switchImgId;

    }

    public String getName() {
        return mName;
    }
    public int getTextColor(){return mTextColor;}
    public void setTextColor(int textColor){this.mTextColor=textColor;}
   // public int getTextBackground() {
    //    return mTextBackground;
   // }
   // public void setTextBackground(int textBackground) {
   //     this.mTextBackground = textBackground;
   // }
    public int getImgId() {
        return mImgId;
    }
    public void setImgId(int imgId) {
        this.mImgId = imgId;
    }
    public int getSwitchImgId() {
        return switchImgId;
    }
    public void setSwitchImgId(int switchImgId) {
        this.switchImgId = switchImgId;
    }
}
