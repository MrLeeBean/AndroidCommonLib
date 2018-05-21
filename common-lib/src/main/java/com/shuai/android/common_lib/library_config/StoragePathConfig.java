package com.shuai.android.common_lib.library_config;

import android.os.Environment;

import java.io.File;

/**
 * 存储路径配置
 */

public class StoragePathConfig {


    /**
     * 基本外部存储路径
     */
    public static String AppBathPath =
            Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                    Environment.getExternalStorageDirectory().toString() + File.separator + "MyApp"//SD卡正常的情况下
                    :
                    Environment.getDataDirectory().getPath() + File.separator + "MyApp";//SD卡异常的情况下


    private static final String StrLogPath = "log";
    private static final String StrImagePath = "image";
    private static final String StrDownloadPath = "download";

    private static String getPath(String sonPath) {
        if (sonPath == null) {
            sonPath = "";
        }
        return AppBathPath + File.separator + sonPath;
    }

    /**
     * 获取基本路径（结尾无separator）
     *
     * @return
     */
    public static final String getBastPath() {
        return AppBathPath;
    }

    /**
     * Log日志存储路径（结尾无separator）
     *
     * @return
     */
    public static String getLogPath() {
        return getPath(StrLogPath);
    }


    /**
     * Log详细日志存储路径（结尾无separator）
     *
     * @return
     */
    public static String getDetailLogPath() {
        return getPath(StrLogPath) + File.separator + "detail";
    }

    /**
     * Image图片储存路径（结尾无separator）
     *
     * @return
     */
    public static String getImagePath() {
        return getPath(StrImagePath);
    }


    /**
     * Download下载存储路径（结尾无separator）
     *
     * @return
     */
    public static String getDownloadPath() {
        return getPath(StrDownloadPath);
    }
}
