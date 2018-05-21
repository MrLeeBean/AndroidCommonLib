package com.shuai.android.common_lib.library_common.utils;


import com.shuai.android.common_lib.library_common.application.BaseApplication;

/**
 * 和线程相关的工具类
 */

public class ThreadUtils {

    /**
     * 把Runnable方法提交到主线程执行
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (android.os.Process.myTid() == BaseApplication.getMainTid()) {
            runnable.run();
        } else {
            BaseApplication.getHandler().post(runnable);//运行一个handler
        }
    }
}
