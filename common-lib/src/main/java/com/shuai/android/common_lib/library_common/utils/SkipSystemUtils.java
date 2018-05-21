package com.shuai.android.common_lib.library_common.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;


/**
 * 和界面跳转（系统内部）相关的工具类
 */

public class SkipSystemUtils {

    /**
     * 跳转到微信客户端
     *
     * @param context 上下文
     */
    public static void skipWeChatApp(Context context) {

        if (!PackageUtils.isWeChatAvailable(context)) {
            ALog.e("未安装微信客户端");
            return;
        }

        try {

            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            ALog.e("跳转到微信客户端内发生异常" + e.toString());
        }

    }

    /**
     * 跳转到WiFi设置界面
     *
     * @param context 上下文
     */
    public static void skipWifiSettingIntent(Context context) {
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    /**
     * 跳转到数据流量设置界面
     *
     * @param context 上下文
     */
    public static void skipDataSettingIntent(Context context) {
        context.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
    }

    /**
     * 跳转到安全设置界面
     *
     * @param context 上下文
     */
    public static void skipSecuritySettingIntent(Context context) {
        context.startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
    }


    /**
     * 跳转到权限设置界面。
     *
     * @param context 上下文
     */
    public static void skipPermissionSettingsIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }


    /**
     * 跳转到发短信页面
     *
     * @param phoneNumber 手机号
     * @param message     信息内容
     */
    public static void skipToSendSms(Context context, String phoneNumber, String message) {

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", message);
        context.startActivity(intent);

    }

    /**
     * 跳转到拨打电话页面
     * @param context
     * @param phoneNumber
     */
    public static void skipToCallPhone(Context context, String phoneNumber){
        // 执行打电话的操作。
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        context.startActivity(intent);
    }

}
