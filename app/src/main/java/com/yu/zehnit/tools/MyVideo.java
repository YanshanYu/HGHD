package com.yu.zehnit.tools;

public class MyVideo {
    private String name;
    private int imageId;

    public MyVideo() {
    }

    public MyVideo(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
