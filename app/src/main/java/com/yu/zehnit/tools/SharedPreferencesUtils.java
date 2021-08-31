package com.yu.zehnit.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的工具类
 */
public class SharedPreferencesUtils {


    private static String name;

    /**
     * 设置文件名
     * info文件存储用户登录信息、设备数量和语言；
     * eqp+数字文件（如eqp1）存储设备信息，包括设备地址和名称
     * @param fileName 文件名称
     */
    public static void setFileName(String fileName) {
        name = fileName;
    }
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context 上下文
     * @param key key
     * @param value val
     */
    public static void setParam(Context context , String key, Object value){

        String type = value.getClass().getSimpleName();
        SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        switch (type) {
            case "Boolean":
                editor.putBoolean(key, (Boolean) value);
                break;
            case "Float":
                editor.putFloat(key, (Float) value);
                break;
            case "String":
                editor.putString(key, (String) value);
                break;
            case "Integer":
                editor.putInt(key, (Integer) value);
                break;
        }

        editor.apply();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultValue){
        String type = defaultValue.getClass().getSimpleName();
        SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        switch (type) {
            case "Boolean":
                return  pref.getBoolean(key, (Boolean) defaultValue);
            case "Float":
                return pref.getFloat(key, (Float) defaultValue);
            case "String":
                return pref.getString(key, (String) defaultValue);
            case "Integer":
                return pref.getInt(key, (Integer) defaultValue);
        }

        return null;
    }
}
