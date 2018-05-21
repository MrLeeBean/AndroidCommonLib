package com.shuai.android.common_lib.library_update.download;

import android.content.Context;
import android.support.annotation.NonNull;

import com.shuai.android.common_lib.library_update.UIData;
import com.shuai.android.common_lib.library_update.interf.IXUIDataListener;
import com.shuai.android.common_lib.library_update.interf.XDownloadFailedListener;
import com.shuai.android.common_lib.library_update.interf.XDownloadingDialogListener;
import com.shuai.android.common_lib.library_update.interf.XVersionDialogListener;
import com.shuai.android.common_lib.library_update.net.HttpHeaders;
import com.shuai.android.common_lib.library_update.net.HttpParams;
import com.shuai.android.common_lib.library_update.net.HttpRequestMethod;
import com.shuai.android.common_lib.library_update.net.UpdateRequest;
import com.shuai.android.common_lib.library_update.notification.NotificationBuilder;
import com.shuai.android.common_lib.library_update.util.FileHelper;
import com.lzy.okgo.callback.Callback;

/**
 * Created by 一米阳光 on 2018/2/27.
 */
public class VersionFacade<T> {
    private String downloadUrl;
    private String requestUrl;
    private String downloadAPKPath;//存储地址
    private boolean isSilentDownload;//是否静默下载，暂时无用
    private boolean isShowDownloadingDialog;
    private boolean isShowNotification;
    private boolean isShowDownloadFailDialog;
    private boolean isForceRedownload;
    private XDownloadFailedListener xDownloadFailedListener;
    private XDownloadingDialogListener xDownloadingDialogListener;
    private XVersionDialogListener xDialogListener;
    private UIData versionBundle;
    private UpdateRequest updateRequest;
    private HttpHeaders headers;
    private HttpParams params;
    private HttpRequestMethod httpRequestMethod = HttpRequestMethod.POST;
    private HttpRequestMethod apkDownMethod = HttpRequestMethod.GET;
    private Callback callback;
    private NotificationBuilder notificationBuilder;

    public VersionFacade() {
        UpdateRequest<String> updateRequest = new UpdateRequest<String>();
        this.updateRequest = updateRequest;
        initialize();
    }

    public VersionFacade(IXUIDataListener ixDownloadListener) {
        UpdateRequest<String> updateRequest = new UpdateRequest<String>(ixDownloadListener);
        this.updateRequest = updateRequest;
        initialize();
    }

    public void updateVersionSure() {
        this.updateRequest.updateVersionSure();
    }

    public void setIXUIDataListener(IXUIDataListener ixDownloadListener) {
        this.updateRequest.setIxDownloadListener(ixDownloadListener);
    }

    private void initialize() {
        isSilentDownload = false;
        downloadAPKPath = FileHelper.getDownloadApkCachePath();
        isForceRedownload = false;
        isShowDownloadingDialog = true;
        isShowNotification = true;
        isShowDownloadFailDialog = true;
        notificationBuilder=NotificationBuilder.create();
    }

    public void handlerUIData(UIData uiData) throws Exception {

        this.updateRequest.handlerUIData(uiData);
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public VersionFacade setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean success = true;
            if (grantResults != null) {

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == -1) {
                        success=false;
                        break;
                    }
                }
            }
            if(success) {
                updateVersionSure();
            }

        }

    }

    public String getDownloadAPKPath() {
        return downloadAPKPath;
    }

    public VersionFacade setDownloadAPKPath(String downloadAPKPath) {
        this.downloadAPKPath = downloadAPKPath;
        return this;
    }

    public boolean isSilentDownload() {
        return isSilentDownload;
    }

    public VersionFacade setSilentDownload(boolean silentDownload) {
        isSilentDownload = silentDownload;
        return this;
    }

    public boolean isShowDownloadingDialog() {
        return isShowDownloadingDialog;
    }

    public VersionFacade setShowDownloadingDialog(boolean showDownloadingDialog) {
        isShowDownloadingDialog = showDownloadingDialog;
        return this;
    }

    public boolean isShowNotification() {
        return isShowNotification;
    }

    public VersionFacade setShowNotification(boolean showNotification) {
        isShowNotification = showNotification;
        return this;
    }

    public boolean isShowDownloadFailDialog() {
        return isShowDownloadFailDialog;
    }

    public VersionFacade setShowDownloadFailDialog(boolean showDownloadFailDialog) {
        isShowDownloadFailDialog = showDownloadFailDialog;
        return this;
    }

    public boolean isForceRedownload() {
        return isForceRedownload;
    }

    public VersionFacade setForceRedownload(boolean forceRedownload) {
        isForceRedownload = forceRedownload;
        return this;
    }

    public XDownloadFailedListener getxDownloadFailedListener() {
        return xDownloadFailedListener;
    }

    public VersionFacade setxDownloadFailedListener(XDownloadFailedListener xDownloadFailedListener) {
        this.xDownloadFailedListener = xDownloadFailedListener;
        return this;
    }

    public XDownloadingDialogListener getxDownloadingDialogListener() {
        return xDownloadingDialogListener;
    }

    public VersionFacade setxDownloadingDialogListener(XDownloadingDialogListener xDownloadingDialogListener) {
        this.xDownloadingDialogListener = xDownloadingDialogListener;
        return this;
    }

    public XVersionDialogListener getxDialogListener() {
        return xDialogListener;
    }

    public VersionFacade setxDialogListener(XVersionDialogListener xDialogListener) {
        this.xDialogListener = xDialogListener;
        return this;
    }

    public UIData getVersionBundle() {
        return versionBundle;
    }

    public VersionFacade setVersionBundle(UIData versionBundle) {
        this.versionBundle = versionBundle;
        return this;
    }

    public UpdateRequest getUpdateRequest() {
        return updateRequest;
    }

    public VersionFacade setUpdateRequest(UpdateRequest updateRequest) {
        this.updateRequest = updateRequest;
        return this;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public VersionFacade setHeaders(HttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    public HttpParams getParams() {
        return params;
    }

    public VersionFacade setParams(HttpParams params) {
        this.params = params;
        return this;
    }

    public HttpRequestMethod getHttpRequestMethod() {
        return httpRequestMethod;
    }

    public VersionFacade setHttpRequestMethod(HttpRequestMethod httpRequestMethod) {
        this.httpRequestMethod = httpRequestMethod;
        return this;
    }

    public HttpRequestMethod getApkDownMethod() {
        return apkDownMethod;
    }

    public VersionFacade setApkDownMethod(HttpRequestMethod apkDownMethod) {
        this.apkDownMethod = apkDownMethod;
        return this;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public VersionFacade setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    public Callback getCallback() {
        return callback;
    }

    public VersionFacade setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }
    public NotificationBuilder getNotificationBuilder() {
        return notificationBuilder;
    }

    public VersionFacade<T> setNotificationBuilder(NotificationBuilder notificationBuilder) {
        this.notificationBuilder = notificationBuilder;
        return this;
    }
    public void excute(Context context) {
        updateRequest.enqueueWork(context, this);
    }
}
