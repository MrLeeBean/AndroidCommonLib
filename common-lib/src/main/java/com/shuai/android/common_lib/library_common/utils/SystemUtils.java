package com.shuai.android.common_lib.library_common.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

/**
 * 设备、获取相关信息工具类
 */
public class SystemUtils {

    /**
     * 获取设备的IMEI号
     *
     * @param context
     * @return
     */
    public static String getDeviceImeiCode(Context context) {

        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ALog.e("获取IMEI权限拒绝");
                return "";
            }
            String szImei = TelephonyMgr.getDeviceId();
            return szImei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
