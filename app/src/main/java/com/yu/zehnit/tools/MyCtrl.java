package com.yu.zehnit.tools;

public class MyCtrl {

    private String name;
    private int imgId;
    private int imgId2;
    private int switchImgId;

    public MyCtrl() {
    }

    public MyCtrl(String name, int imgId, int imgId2, int switchImgId) {
        this.name = name;
        this.imgId = imgId;
        this.imgId2 = imgId2;
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

    public int getImgId2() {
        return imgId2;
    }
}
