package com.shuai.android.common_lib.library_common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;


/**
 * 与签名文件相关的工具类
 */

public class SignUtils {

    public static final String WECHAT_SING_MD5 = "18c867f0717aa67b2ab7347505ba07ed";


    /**
     * 获取APP签名MD5值
     */
    public static String getSignMd5Str(Context context, String packageName) {

        //检测应用是否安装
        if (!PackageUtils.isPackageAvailable(context, packageName)) {
            ALog.e("应用：" + packageName + " 未安装");
            return "";
        }


        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signStr = MD5Utils.encryptMD5(sign.toByteArray());
            return signStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取APP自身签名MD5值
     */
    public static String getSelfSignMd5Str(Context context) {
        return getSignMd5Str(context, context.getPackageName());

    }

    /**
     * 获取微信签名的MD5值
     */
    public static String getWeChatSignMd5Str(Context context) {
        return getSignMd5Str(context, "com.tencent.mm");
    }


}
