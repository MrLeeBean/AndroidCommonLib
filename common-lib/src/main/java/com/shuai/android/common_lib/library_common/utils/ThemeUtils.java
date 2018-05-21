package com.shuai.android.common_lib.library_common.utils;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

import com.shuai.android.common_lib.R;


/**
 * 和主题相关的工具类
 */

public class ThemeUtils {

    /**
     * 获取主题色ColorPrimary
     *
     * @param context
     * @return
     */
    public static @ColorInt
    int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取主题色ColorPrimaryDark
     *
     * @param context
     * @return
     */
    public static @ColorInt
    int getColorPrimaryDark(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取主题色ColorAccent
     *
     * @param context
     * @return
     */
    public static @ColorInt
    int getColorAccent(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

}
