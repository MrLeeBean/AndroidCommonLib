package com.shuai.android.common_lib.library_common.application;

import android.app.Application;
import android.os.Handler;

import com.shuai.android.common_lib.library_common.exception.AppExceptionHandler;
import com.shuai.android.common_lib.library_common.utils.ALog;
import com.shuai.android.common_lib.library_config.AppConfig;
import com.shuai.android.common_lib.library_config.StoragePathConfig;
import com.shuai.android.common_lib.library_update.util.FileHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okserver.OkDownload;

/**
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 */
public class BaseApplication extends Application {

    private static BaseApplication sInstance;
    private static int mainTid;//主线程ID
    private static Handler baseHandler;//全局Handler


    /**
     * 获取单例对象
     */
    public static BaseApplication getInstance() {
        return sInstance;
    }

    /**
     * 获取主线程id
     */
    public static int getMainTid() {
        return mainTid;
    }

    /**
     * 获取Handler
     */
    public static Handler getHandler() {
        return baseHandler;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mainTid = android.os.Process.myTid();
        baseHandler = new Handler();

        initLog();//初始化日志系统
        initOkgo();//初始化OkGo网络框架
        initCrashHandler();//初始化CrashHandler异常处理系统。
    }

    /**
     * 初始化OkGo网络框架。如果这样的配置不合适，请覆盖此方法。
     */
    public void initOkgo() {

        OkGo.getInstance().init(this);
        OkDownload.getInstance().setFolder(FileHelper.getDownloadApkCachePath());

    }


    /**
     * 初始化Log日志记录系统。如果这样的配置不合适，请覆盖此方法。
     */
    public ALog.Config initLog() {

        return ALog.init(this)
                .setLogSwitch(AppConfig.IS_LOG_SHOW)// 设置log总开关，包括输出到控制台和文件，默认开
                .setLog2FileSwitch(AppConfig.IS_LOG_SHOW)// 打印log时是否存到文件的开关，默认关
                .setFilePrefix("detail_log")//设置文件名前缀。
                .setDir(StoragePathConfig.getDetailLogPath());//将所有日志存储进文件。

    }


    /**
     * 初始化CrashHandler异常处理系统。
     */
    public void initCrashHandler() {

        //应用异常的捕获和处理
        AppExceptionHandler exHandler = AppExceptionHandler.getInstance();
        exHandler.init(this);

    }

}
