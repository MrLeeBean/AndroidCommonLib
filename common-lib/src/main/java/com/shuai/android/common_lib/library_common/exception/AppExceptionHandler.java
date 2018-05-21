package com.shuai.android.common_lib.library_common.exception;

import android.content.Context;
import android.os.Build;
import android.os.Looper;


import com.shuai.android.common_lib.library_common.application.BaseApplication;
import com.shuai.android.common_lib.library_common.exception.model.CrashData;
import com.shuai.android.common_lib.library_common.manager.ActivityManager;
import com.shuai.android.common_lib.library_common.utils.ApkUtils;
import com.shuai.android.common_lib.library_common.utils.ALog;
import com.shuai.android.common_lib.library_common.utils.FileUtilsPro;
import com.shuai.android.common_lib.library_common.utils.PreferenceHelper;
import com.shuai.android.common_lib.library_common.utils.SystemUtils;
import com.shuai.android.common_lib.library_common.widget.XToast;
import com.shuai.android.common_lib.library_config.sp.SPConstants;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * 异常捕获的处理方式
 *
 * @author changshuai
 */
public class AppExceptionHandler implements UncaughtExceptionHandler {

    private static AppExceptionHandler sInstance = new AppExceptionHandler();
    // 系统默认的异常处理 （默认情况下，系统会终止当前的异常程序）
    private UncaughtExceptionHandler mDefaultExceptionHandler;
    private Context mContext;

    // 构造私有方法。防止外部创造多种实例
    private AppExceptionHandler() {
    }

    public static AppExceptionHandler getInstance() {
        return sInstance;
    }

    // 这里主要完成初始化工作
    public void init(Context context) {
        // 获取系统默认的异常处理
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前实例设置为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 获取context . 方便内部使用
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函。当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法 thread 为出现未捕获异常的线程，ex
     * 为未捕获的异常，有了这个ex。我们就可以得到这个异常信息
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {

            if (!handleException(ex) && mDefaultExceptionHandler != null) {
                mDefaultExceptionHandler.uncaughtException(thread, ex);
            } else {
                //打印Crash日志
                ALog.e("Crash日志", getExceptionMessage(ex, "【APP崩溃】", null, null));
                //把异常信息写入文件中。
                dumpCrashMessageToFile(ex, "【APP崩溃】");
                //把异常信息写入数据库。
                dumpExceptionMessageToSP(ex, "【APP崩溃】");
                //退出应用
                ActivityManager.getInstance().exitApp(BaseApplication.getInstance());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //处理异常。
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                XToast.showFailLong(mContext,"程序出现异常即将退出...");
                Looper.loop();
            }
        }.start();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }


    //-----------------------------------------------异常写入SP-------------------------------------------------//


    public static void dumpExceptionMessageToSP(Throwable ex) {
        dumpExceptionMessageToSP(ex, null);
    }

    public static void dumpExceptionMessageToSP(Throwable ex, String typeNotice) {
        dumpExceptionMessageToSP(ex, typeNotice, null, null, null);
    }

    public static void dumpExceptionMessageToSP(String ExceptionType, String ExceptionMessage, String ExceptionDetail) {
        dumpExceptionMessageToSP(null, "自定义的异常信息写入SP", ExceptionType, ExceptionMessage, ExceptionDetail);
    }


    /**
     * 将异常信息导入sp
     *
     * @param ex               异常信息
     * @param typeNotice       手动添加提示信息
     * @param ExceptionType    类型      （ex为null时生效）
     * @param ExceptionMessage 信息      （ex为null时生效）
     * @param ExceptionDetail  详细信息  （ex为null时生效）
     */
    private static void dumpExceptionMessageToSP(Throwable ex, String typeNotice, String ExceptionType, String ExceptionMessage, String ExceptionDetail) {

        try {

            CrashData crash = new CrashData();


            long currentTime = System.currentTimeMillis();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime));

            //设置表字段。
            //crash.setAppType("hyd");  //todo 需要额外赋值
            crash.setAppVersion(ApkUtils.getVersionName(BaseApplication.getInstance()));
            crash.setOsType("android");
            crash.setOsVersion(Build.VERSION.RELEASE);
            crash.setRendor(Build.MANUFACTURER + "_" + Build.VERSION.SDK_INT);
            crash.setDeviceModel(Build.MODEL);
            crash.setCpuArchi(Build.CPU_ABI);


            if (ex == null) {//ex为空

                crash.setCrashType((typeNotice == null ? "" : typeNotice + ":") + ExceptionType);
                crash.setMessage(ExceptionMessage);
                crash.setDetail(ExceptionDetail);//异常的详细信息

            } else {//ex不为空。

                crash.setCrashType((typeNotice == null ? "" : typeNotice + ":") + ex.getClass().getSimpleName());
                crash.setMessage(ex.toString());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ex.printStackTrace(new PrintStream(baos));
                } finally {
                    baos.close();
                }
                crash.setDetail(baos.toString());//异常的详细信息
            }


            crash.setTimeStr(time);
//            crash.setUserId(AppAccount.getInstance().getUserid());//todo 需要额外赋值
//            crash.setUserName(AppAccount.getInstance().getUserName());//todo 需要额外赋值


            ArrayList<CrashData> crashList = null;

            try {
                if (PreferenceHelper.readObject(BaseApplication.getInstance(), SPConstants.CRASH_DETAIL_MESSAGE) == null) {
                    crashList = new ArrayList<>();
                } else {
                    crashList = PreferenceHelper.readObject(BaseApplication.getInstance(), SPConstants.CRASH_DETAIL_MESSAGE);
                    if (crashList.size() <= 0) {
                        crashList = new ArrayList<>();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                crashList = new ArrayList<>();
            }
            crashList.add(crash);

            PreferenceHelper.saveObject(BaseApplication.getInstance(), SPConstants.CRASH_DETAIL_MESSAGE, crashList);


        } catch (Exception e) {
            ALog.e("异常信息写入SP出错：" + e.toString());
            e.printStackTrace();

        }
    }


    //-----------------------------------------------获取异常信息-------------------------------------------------//

    /**
     * 获取异常信息
     *
     * @param ex            异常  可以为null
     * @param noticeMessage 自定义异常提示信息
     * @param customTitle   自定义额外信息Title
     * @param customMessage 自定义额外信息Message
     * @return
     */
    public static String getExceptionMessage(Throwable ex, String noticeMessage, String customTitle, String customMessage) {

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ex.printStackTrace(new PrintStream(baos));
            } catch (Exception e) {

            } finally {
                baos.close();
            }

            long currentTime = System.currentTimeMillis();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime));

            String Time = "时间：" + time + "\n";
            String ErrorDescribe = "ERROR------------" + "|" + "发生时间：" + time + "|" + "--------------ERROR" + "\n";
            String NoticeMsg = "提示信息：" + noticeMessage + "\n";
            String DeviceId = "设备IMEI：" + SystemUtils.getDeviceImeiCode(BaseApplication.getInstance()) + "\n";
//            String UserId = "用户UserId：" + AppAccount.getInstance().getUserid() + "\n";
//            String UserName = "用户姓名：" + AppAccount.getInstance().getUserName() + "\n";
//            String UserPhoneNumber = "用户手机号：" + AppAccount.getInstance().getUserPhone() + "\n";
            String AppVersion = "APP版本：" + ApkUtils.getVersionName(BaseApplication.getInstance()) + "\n";
            String AppOsType = "系统类型：" + "android" + "\n";
            String OsVersion = "系统版本：" + Build.VERSION.RELEASE + "\n";
            String Rendor = "硬件版本和SDK版本：" + Build.MANUFACTURER + "_" + Build.VERSION.SDK_INT + "\n";
            String DeviceModel = "机型：" + Build.MODEL + "\n";
            String CpuArchi = "CPU类型：" + Build.CPU_ABI + "\n";


            String CrashType = ex != null ? "Error类型：" + ex.getClass().getSimpleName() + "\n" : "Error类型：" + "\n";
            String Message = ex != null ? "Error信息：" + ex.toString() + "\n" : "Error信息：" + "\n";
            String Detail = ex != null ? "Error详情：" + baos.toString() + "\n" : "Error详情：" + "\n";


            String CustomMessage = customTitle + "：" + customMessage + "\n\n\n\n\n\n";

            String result = ErrorDescribe + Time + NoticeMsg + DeviceId + /*UserId + UserName + UserPhoneNumber +*/ AppVersion + AppOsType + OsVersion + Rendor + DeviceModel + CpuArchi + CrashType + Message + Detail + CustomMessage;
            return result;

        } catch (Exception ee) {
            ALog.e("获取异常信息时候出错：" + ee.toString());
            ee.printStackTrace();
            return null;

        }
    }

    public static String getExceptionMessage(Throwable ex, String noticeMessage) {
        return getExceptionMessage(ex, noticeMessage, null, null);
    }

    public static String getExceptionMessage(Throwable ex) {
        return getExceptionMessage(ex, null, null, null);
    }


    //-----------------------------------------------异常文件日志记录-------------------------------------------------//

    /**
     * 把异常信息导入文件
     *
     * @param ex            异常
     * @param noticeMessage 自定义写入的提示信息
     */
    public static void dumpExceptionMessageToFile(Throwable ex, String noticeMessage) {
        try {
            //写入异常文件
             FileUtilsPro.saveStrToLogPathFile(AppExceptionHandler.getExceptionMessage(ex, noticeMessage, null, null), "error_log.txt", true);
        } catch (Exception e) {
            ALog.e("异常信息写入文件出错：" + e.toString());
            e.printStackTrace();
        }
    }

    public static void dumpExceptionMessageToFile(Throwable ex) {
        dumpExceptionMessageToFile(ex, null);
    }


    /**
     * 把Crash信息写入文件。
     *
     * @param ex
     * @param noticeMessage
     */
    public static void dumpCrashMessageToFile(Throwable ex, String noticeMessage) {
        try {
            //写入奔溃文件
            FileUtilsPro.saveStrToLogPathFile(AppExceptionHandler.getExceptionMessage(ex, "【CRASH】" + noticeMessage, null, null), "crash_log.txt", true);
        } catch (Exception e) {
            ALog.e("Crash信息写入文件出错：" + e.toString());
            e.printStackTrace();
        }
    }

    public static void dumpCrashMessageToFile(Throwable ex) {

        dumpCrashMessageToFile(ex, null);
    }


    //-----------------------------------------------统一处理异常-------------------------------------------------//

    /**
     * 统一处理异常
     *
     * @param ex            异常
     * @param noticeMessage 异常信息
     */
    public static void doHandle(Throwable ex, String noticeMessage) {
        dumpExceptionMessageToFile(ex, noticeMessage);//保存异常到文件
        ALog.e(noticeMessage + getExceptionMessage(ex, noticeMessage));//打印日志

    }

    public static void doHandle(Throwable ex) {
        dumpExceptionMessageToFile(ex);//保存异常到文件
        ALog.e(getExceptionMessage(ex));//打印日志
    }

}
