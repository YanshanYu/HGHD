package com.yu.zehnit.tools;

public class Equipment{

    private int category; // 类别

    private String name; // 设备名称
    private int imgId; // 设备图片
    private int imgGo; // 跳转按钮

    private int imgAdd; // 添加图标
    private String text; // 说明

    public Equipment() {
    }

    public Equipment(int category, String name, int imgId, String text) {
        this.category = category;
        this.name = name;
        this.imgId = imgId;
        this.text = text;
    }

    public Equipment(int category) {
        this.category = category;
    }

    public int getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getImgId() {
        return imgId;
    }

    public int getImgGo() {
        return imgGo;
    }

    public int getImgAdd() {
        return imgAdd;
    }

    public String getText() {
        return text;
    }
}
