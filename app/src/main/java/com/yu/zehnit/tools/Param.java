package com.yu.zehnit.tools;

public class Param {

    private String experimentName;
    private int category; //0表示只有一个参数，1表示有两个参数
    private String paramName1;
    private String paramName2;
    private float paramValue1;
    private float paramValue2;

    public Param() {
    }

    public Param(String experimentName, int category, String paramName1, float paramValue1) {
        this.experimentName = experimentName;
        this.category = category;
        this.paramName1 = paramName1;
        this.paramValue1 = paramValue1;
    }

    public Param(String experimentName, int category, String paramName1, float paramValue1, String paramName2, float paramValue2) {
        this.experimentName = experimentName;
        this.category = category;
        this.paramName1 = paramName1;
        this.paramName2 = paramName2;
        this.paramValue1 = paramValue1;
        this.paramValue2 = paramValue2;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public String getParamName1() {
        return paramName1;
    }

    public String getParamName2() {
        return paramName2;
    }

    public int getCategory() {
        return category;
    }

    public float getParamValue1() {
        return paramValue1;
    }

    public float getParamValue2() {
        return paramValue2;
    }
}
