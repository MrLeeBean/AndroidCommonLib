package com.shuai.android.common_lib.library_common.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

public class ActivityManager {

    private static Stack<Activity> activityStack;

    public static ActivityManager getInstance() {
        return ViewManagerHolder.sInstance;
    }

    private static class ViewManagerHolder {
        private static final ActivityManager sInstance = new ActivityManager();
    }

    private ActivityManager() {
    }

    /**
     * 添加指定Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }


    /**
     * 获取当前Activity
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }


    /**
     * 结束当前Activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }


    /**
     * 结束指定Class的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                return;
            }
        }
    }


    /**
     * 结束全部的Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    /**
     * 退出应用程序
     */
    public void exitApp(Context context) {
        Log.e("ActivityManager", "app exit" );
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            Log.e("ActivityManager", "app exit" + e.getMessage());
        }
    }
}
