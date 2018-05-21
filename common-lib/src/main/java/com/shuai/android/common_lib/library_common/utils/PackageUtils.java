package com.shuai.android.common_lib.library_common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Package相关的工具类
 */

public class PackageUtils {


    /**
     * 检测手机是否装了某个应用
     *
     * @param context     上下文
     * @param packageName 包名
     * @return
     */
    public static boolean isPackageAvailable(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否安装了微信
     *
     * @param context 上下文
     * @return
     */
    public static boolean isWeChatAvailable(Context context) {
        return isPackageAvailable(context, "com.tencent.mm");
    }

    /**
     * 是否安装了QQ
     *
     * @param context 上下文
     * @return
     */
    public static boolean isQQAvailable(Context context) {
        return isPackageAvailable(context, "com.tencent.mobileqq");
    }


/**
 * 几个常用的package有：
 新浪微博（编辑界面）：
 com.sina.weibo
 com.sina.weibo.EditActivity
 腾讯微博（编辑界面）：
 com.tencent.WBlog
 com.tencent.WBlog.activity.MicroblogInput
 微信：
 com.tencent.mm
 com.tencent.mm.ui.LauncherUI
 QQ:
 com.tencent.mobileqq
 com.tencent.mobileqq.activity.HomeActivity

 作者：街角的那只喵
 链接：http://www.jianshu.com/p/1e92c2b0d773
 來源：简书
 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */


}
