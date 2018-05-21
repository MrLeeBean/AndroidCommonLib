package com.shuai.android.common_lib.library_common.utils;

import android.text.TextUtils;

/**
 * 和字符串脱敏相关的工具类
 */

public class DesensitizeUtils {

    /**
     * 手机号脱敏（隐藏手机号中间四位）
     *
     * @param phonenumber
     * @return
     */
    public static String desensitizePhoneNumber(String phonenumber) {
        if (!TextUtils.isEmpty(phonenumber) && phonenumber.trim().length() == 11) {
            String beginsub = phonenumber.substring(0, 3);
            String endsub = phonenumber.substring(7);
            return beginsub + "****" + endsub;
        }
        return "";
    }


    /**
     * 隐藏姓名首位（姓名脱敏）
     */
    public static String desensitizeUserName(String name) {
        if (!TextUtils.isEmpty(name)) {
            String sub = name.substring(0, 1);
            return name.replace(sub, "*");

        }
        return "";
    }


    /**
     * 隐藏身份证的中间八位 140430********0018(身份证号脱敏)
     */
    public static String desensitizeIdcardNumber(String idcardnumber) {
        if (!TextUtils.isEmpty(idcardnumber)) {
            String sub = idcardnumber.substring(6, 14);
            return idcardnumber.replace(sub, "********");
        }
        return "";
    }

}
