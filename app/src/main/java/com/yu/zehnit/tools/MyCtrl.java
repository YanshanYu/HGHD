package com.yu.zehnit.tools;

public class MyCtrl {

    private String name;
    private int imgId;
    private int switchImgId;

    public MyCtrl() {
    }

    public MyCtrl(String name, int imgId, int switchImgId) {
        this.name = name;
        this.imgId = imgId;
        this.switchImgId = switchImgId;
    }

    public String getName() {
        return name;
    }


    public int getImgId() {
        return imgId;
    }

    public int getSwitchImgId() {
        return switchImgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public void setSwitchImgId(int switchImgId) {
        this.switchImgId = switchImgId;
    }
}
