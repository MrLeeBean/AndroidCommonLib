package com.shuai.android.common_lib.library_common.utils;

import android.text.TextUtils;

import com.shuai.android.common_lib.library_config.StoragePathConfig;

import java.io.File;

/**
 * 关于文件处理的工具类
 */

public class FileUtilsPro {


    /**
     * 将字符串写入 初始化指定目录 的根目录
     *
     * @param str      字符串内容
     * @param fileName 文件名
     * @param isAppend 是否追加
     * @return
     */
    public static File saveStrToBathPathFile(String str, String fileName, boolean isAppend) {
        return FileUtils.saveStrToFile(str, StoragePathConfig.getBastPath(), fileName, isAppend);
    }

    /**
     * 将字符串写入初始化指定目录/Log目录
     *
     * @param str      字符串内容
     * @param fileName 文件名
     * @param isAppend 是否追加
     * @return
     */
    public static File saveStrToLogPathFile(String str, String fileName, boolean isAppend) {
        return FileUtils.saveStrToFile(str, StoragePathConfig.getLogPath(), fileName, isAppend);
    }

    /**
     * 将字节数组写入初始化指定目录的根目录
     *
     * @param b        字节数组内容
     * @param fileName 文件名
     * @param isAppend 是否追加
     * @return
     */
    public static File saveBytesToBathPathFile(byte[] b, String fileName, boolean isAppend) {
        return FileUtils.saveBytesToFile(b, StoragePathConfig.getBastPath(), fileName, isAppend);
    }


    /**
     * 重命名文件
     *
     * @param oldFile     旧的文件
     * @param newFileName 新文件名
     * @return
     */
    public static File renameFile(File oldFile, String newFileName) {

        try {
            if (oldFile != null && oldFile.exists() && oldFile.isFile()) {//oldfile存在。

                if (newFileName != null && !TextUtils.isEmpty(newFileName.trim())) {

                    String oldname = oldFile.getName();
                    String parentPath = oldFile.getParent();

                    File newfile = new File(parentPath + "/" + newFileName);

                    if (!oldname.equals(newFileName)) {
                        oldFile.renameTo(newfile);
                        ALog.d("FileUtilsPro:", "文件名修改成功！");
                        return newfile;

                    } else {
                        ALog.e("FileUtilsPro:", "新旧文件名一致");
                        return oldFile;
                    }
                } else {
                    ALog.e("FileUtilsPro:", "新文件名为null、或为空");
                    return oldFile;
                }

            } else {
                ALog.e("FileUtilsPro:", "旧文件为null、或者不存在、或不是文件");
                return oldFile;
            }

        } catch (Exception e) {
            return oldFile;
        }
    }

}
