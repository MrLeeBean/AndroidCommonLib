package com.shuai.android.common_lib.library_common.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 关于校验的工具类
 *
 */
public class ValidateUtils {


    /**
     * 正则校验
     *
     * @param
     * @param pattern
     * @return
     */
    public static boolean matcher(String str, String pattern) {
        try {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(str);
            return m.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 校验密码（密码由6-12位字符组成，必须包含数字和字母。）
     */
    public static boolean isPassword_1(String password) {
        String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";
        return matcher(password, pattern);
    }


    /***
     * 是否是数字
     * @param str
     * @return
     */
    public static boolean isDecimal(String str) {
        String pattern = "([1-9]+[0-9]*|0)(\\.[\\d]+)?";
        return matcher(str, pattern);

    }

    /**
     * 是否包含数字
     *
     * @param str
     * @return
     */
    public static boolean containNum(String str) {
        String pattern = "^(?=.*[0-9]).*$";
        return matcher(str, pattern);
    }


    /**
     * 是否为手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobileNO(String str) {
        String pattern = "^[1][3-8]\\d{9}";
        return matcher(str, pattern);
    }

    /***
     * 校验身份证号
     * @param str
     * @return
     */
    public static boolean isIdCard(String str) {
        String pattern = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        return matcher(str, pattern);
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean isBankNo(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0 || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    /**
     * 判断字符串是否全部为中文字符组成
     *
     * @param str 检测的文字
     * @return true：为中文字符串，false:含有非中文字符
     */
    public static boolean isChineseStr(String str) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            Matcher matcher = pattern.matcher(String.valueOf(c[i]));
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

}
