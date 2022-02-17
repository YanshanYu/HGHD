package com.yu.zehnit.tools;

public class Data {
    private static float xzero;
    private static float yzero;
    private static float zzero;
    private static float xdu=0;
    private static float ydu=0;
    private static float zdu=0;


    public static float getXzero(){return  xzero;}
    public static void setXzero(float a){xzero=a;}
    public static float getYzero(){return  yzero;}
    public static void setYzero(float a){yzero=a;}
    public static float getZzero(){return  zzero;}
    public static void setZzero(float a){zzero=a;}

    public static float getXdu() {
        return xdu;
    }
    public static void setXdu(float a) {
        Data.xdu = a;
    }

    public static float getYdu() {
        return ydu;
    }
    public static void setYdu(float a) {
        Data.ydu = a;
    }

    public static float getZdu() { return zdu; }
    public static void setZdu(float a) {
        Data.zdu = a;
    }





}
