package com.shuai.android.common_lib.library_common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;


/**
 * 和APK相关的工具类
 */

public class ApkUtils {


    /**
     * 获取当前APK版本号
     *
     * @param context 上下文
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return versionName;
        } catch (Exception e) {

            return null;
        }

    }

    /**
     * 获取APK的VersionCode
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            int versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            return versionName;
        } catch (Exception e) {

            return 0;
        }

    }

    /**
     * 获取APK的TargetSDKVersion
     *
     * @param context
     * @return
     */
    public static int getTargetSdkVersion(Context context) {
        int targetSdkVersion = 0;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return targetSdkVersion;
    }


    /**
     * 获取 AndroidManifest.xml中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

}
