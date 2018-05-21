package com.shuai.android.common_lib.library_common.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 和IO流相关的工具类
 */

public class IOUtils {

    /**
     * 关流
     *
     * @param c
     */
    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
