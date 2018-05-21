package com.shuai.android.common_lib.library_common.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.Nullable;


import com.shuai.android.common_lib.library_common.application.BaseApplication;
import com.shuai.android.common_lib.library_common.widget.XToast;

/**
 * language工具类
 */
public class LangUtils {

    /**
     * 判断CharSequence是否为空
     *
     * @param string
     * @return
     */
    public static boolean isNullOrEmpty(@Nullable CharSequence string) {
        return string == null || string.length() == 0;
    }

    /**
     * 判断String是否为空（内部trim校验）
     *
     * @param string
     * @return
     */
    public static boolean isStrNullOrEmpty(@Nullable String string) {
        return string == null || string.trim().length() == 0;
    }

    /**
     * 复制操作。
     *
     * @param string
     */
    public static void copyStrToClipboard(String string) {
        if (android.text.TextUtils.isEmpty(string))
            return;
        ClipboardManager clip = (ClipboardManager) BaseApplication.getInstance()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(string);
        XToast.showInfoLong(BaseApplication.getInstance(), "复制成功");
    }

}
