package com.shuai.android.common_lib.library_common.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;


/**
 * 和界面跳转（外部）相关的工具类
 */

public class SkipExtraUtils {

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


}
