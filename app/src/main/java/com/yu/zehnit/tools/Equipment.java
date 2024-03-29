package com.yu.zehnit.tools;

public class Equipment{

    private int category; // 类别

    private String name; // 设备名称
    private String address; // 设备地址
    private int imgId; // 设备图片
    private int imgGo; // 跳转按钮

    private int imgAdd; // 添加图标
    private String text; // 在线状态

    public Equipment() {
    }

    public Equipment(int category, String name, String address, int imgId, String text) {
        this.category = category;
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
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

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
