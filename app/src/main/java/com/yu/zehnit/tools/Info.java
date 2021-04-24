package com.yu.zehnit.tools;

public class Info {
    private String name;
    private int imgId;

    public Info() {
    }

    public Info(String name, int imgId) {
        this.name = name;
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
