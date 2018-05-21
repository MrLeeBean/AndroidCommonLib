package com.shuai.android.common_lib.library_common.utils;

import android.app.Activity;
import android.text.TextUtils;


import com.shuai.android.common_lib.R;

import de.mateware.snacky.Snacky;


/**
 * 底部弹出弹框工具类。
 * github:https://github.com/matecode/Snacky
 */

public class SnackyHelper {

    /**
     * 错误提示
     *
     * @param activity
     * @param message
     */
    public static void showFail(Activity activity, String message) {

        try {
            String msg;
            if (!TextUtils.isEmpty(message)) {
                msg = "提示" + "\n\n" + message;
            } else {
                msg = "\n" + "提示" + "\n";
            }
            Snacky.builder()
                    .setActivity(activity)
                    .setText(msg)
                    .setBackgroundColor(activity.getResources().getColor(R.color.colorSnackyFail))
                    .setDuration(Snacky.LENGTH_LONG)
                    .setActionText("关闭")
                    .error()
                    .show();
        } catch (Exception e) {

        }


    }

    /**
     * 成功提示
     *
     * @param activity
     * @param message
     */
    public static void showSuccess(Activity activity, String message) {
        try {
            String msg;
            if (!TextUtils.isEmpty(message)) {
                msg = "提示" + "\n\n" + message;
            } else {
                msg = "\n" + "提示" + "\n";
            }
            Snacky.builder()
                    .setActivity(activity)
                    .setText(msg)
                    .setBackgroundColor(activity.getResources().getColor(R.color.colorSnackySuccess))
                    .setDuration(Snacky.LENGTH_LONG)
                    .setActionText("关闭")
                    .success()
                    .show();
        } catch (Exception e) {


        }

    }

    /**
     * 警告提示
     *
     * @param activity
     * @param message
     */
    public static void showWarning(Activity activity, String message) {
        try {
            String msg;
            if (!TextUtils.isEmpty(message)) {
                msg = "提示"+ "\n\n" + message;
            } else {
                msg = "\n" + "提示" + "\n";
            }
            Snacky.builder()
                    .setActivity(activity)
                    .setText(msg)
                    .setBackgroundColor(activity.getResources().getColor(R.color.colorSnackyWarning))
                    .setDuration(Snacky.LENGTH_LONG)
                    .setActionText("关闭")
                    .warning()
                    .show();
        } catch (Exception e) {

        }

    }



    /**
     * 默认提示
     *
     * @param activity
     * @param message
     */
    public static void showInfo(Activity activity, String message) {
        try {
            String msg;
            if (!TextUtils.isEmpty(message)) {
                msg = "提示" + "\n\n" + message;
            } else {
                msg = "\n" + "提示" + "\n";
            }
            Snacky.builder()
                    .setActivity(activity)
                    .setText(msg)
                    .setTextColor(activity.getResources().getColor(android.R.color.white))
                    .setBackgroundColor(activity.getResources().getColor(R.color.colorSnackyInfo))
                    .setDuration(Snacky.LENGTH_LONG)
                    .setActionText("关闭")
                    .build()
                    .show();
        } catch (Exception e) {

        }

    }

}
