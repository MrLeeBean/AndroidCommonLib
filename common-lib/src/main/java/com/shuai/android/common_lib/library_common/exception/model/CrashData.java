package com.shuai.android.common_lib.library_common.exception.model;


import java.io.Serializable;


/**
 * Created by changshuai on 2016/11/23.
 * Crash平台实体类
 */

public class CrashData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String appType;//app类型，恒易贷：hyd、普惠帮：phb
    private String appVersion;//app版本号
    private String appBundle;//appBundle(IOS专用)
    private String osType;//系统类型，IOS：ios、安卓：android
    private String osVersion;//系统版本
    private String rendor;//设备商
    private String deviceModel;//设备型号
    private String cpuArchi;//CPU架构(安卓)
    private String crashType;//crash类型
    private String message;//crash信息
    private String detail;//crash详情
    private String timeStr;//时间，格式：2016-11-11 18:00:00
//    private String userId;//用户ID
//    private String userName;//用户姓名

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppBundle() {
        return appBundle;
    }

    public void setAppBundle(String appBundle) {
        this.appBundle = appBundle;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getRendor() {
        return rendor;
    }

    public void setRendor(String rendor) {
        this.rendor = rendor;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getCpuArchi() {
        return cpuArchi;
    }

    public void setCpuArchi(String cpuArchi) {
        this.cpuArchi = cpuArchi;
    }

    public String getCrashType() {
        return crashType;
    }

    public void setCrashType(String crashType) {
        this.crashType = crashType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
}
