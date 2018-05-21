package com.shuai.android.common_lib.library_common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 */
public class MD5Utils {

    /**
     * 把String转换成MD5
     *
     * @param str
     * @return
     */
    public static String stringToMD5(String str) {

        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            return byteArrayToHexString(mdTemp.digest());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 把String转换成大写的MD5
     *
     * @param str
     * @return
     */

    public static String stringToUpperMD5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] newstrmd5 = md.digest(str.getBytes());

        return byteArrayToUpperHexString(newstrmd5);

    }

    private static String byteArrayToUpperHexString(byte[] byteArray) {

        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

    private static String byteArrayToHexString(byte[] md) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int j = md.length;
        char str[] = new char[j * 2];
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[2 * i] = hexDigits[byte0 >>> 4 & 0xf];
            str[i * 2 + 1] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }


    /**
     * MD5加密
     *
     * @param byteStr 需要加密的内容
     * @return byteStr的md5值
     */
    public static String encryptMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }


    //------------------------------------------------------------------------//


    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};


    /**
     * 校验文件MD5是否匹配
     *
     * @param filePath  指定文件路径
     * @param targetMd5 目标md5
     * @return
     */
    public static boolean checkFileMatchMD5(String filePath, String targetMd5) {
        File apk = new File(filePath);
        if (apk.exists() && getFileMD5Sum(apk.getAbsolutePath()).equals(targetMd5)) {
            return true;
        }
        return false;
    }


    /**
     * 获取指定文件的MD5值
     *
     * @param filePath 指定文件路径
     * @return
     */
    public static String getFileMD5Sum(String filePath) {
        InputStream fis = null;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try {
            fis = new FileInputStream(filePath);
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            return toHexString(md5.digest());
        } catch (Exception e) {
            return "";
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {

            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }


}
