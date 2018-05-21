package com.shuai.android.common_lib.library_common.utils.collect;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.shuai.android.common_lib.library_common.application.BaseApplication;
import com.shuai.android.common_lib.library_common.exception.AppExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 获取应用列表
 */

public class AppInstalledAppReader {
    private static final AppInstalledAppReader ourInstance = new AppInstalledAppReader();

    public static AppInstalledAppReader getInstance() {
        return ourInstance;
    }

    private AppInstalledAppReader() {
    }
    public List<AppBean> getInstalledAppList(){
        List<AppBean> appList = new ArrayList<>();
        try {
            PackageManager pm = BaseApplication.getInstance().getPackageManager();
            // 查询所有已经安装的应用程序
            List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            /*排序*/
            Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(pm));

            for (ApplicationInfo app : listAppcations) {
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    AppBean appBean = new AppBean();
                    appBean.setName((String) app.loadLabel(pm));
                    appBean.setPackageName(app.packageName);
                    appList.add(appBean);
                }
            }
        } catch (Exception e) {
            AppExceptionHandler.doHandle(e,"获取应用列表：发生了异常");
        }
        return appList;
    }

    public class AppBean {
        private String name;
        private String packageName;

        public AppBean() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }
    }
}
