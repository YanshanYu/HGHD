package com.yu.zehnit;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityController {

    // 单例类只需要一个Activity集合，通过ArrayList进行暂存
    public static List<Activity> activities = new ArrayList<>();

    // 添加
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    // 移除
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    // 销毁所有
    public static void finishAll() {
        for (Activity activity : activities) {
            // 需要判断Activity是否正处于销毁状态
            if (!activity.isFinishing())  activity.finish();
        }
    }
}
