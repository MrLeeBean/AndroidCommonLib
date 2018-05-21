package com.shuai.android.common_lib.library_update.net;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.shuai.android.common_lib.library_update.UIData;
import com.shuai.android.common_lib.library_update.download.VersionFacade;
import com.shuai.android.common_lib.library_update.download.XDownload;
import com.shuai.android.common_lib.library_update.interf.IXUIDataListener;
import com.shuai.android.common_lib.library_update.interf.IXRequestUrlListener;
import com.shuai.android.common_lib.library_update.interf.XDownloadFailedListener;
import com.shuai.android.common_lib.library_update.interf.XDownloadingDialogListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.lzy.okserver.download.DownloadListener;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import okhttp3.ResponseBody;

/**
 * Created by 一米阳光 on 2018/2/27.
 */

public class UpdateRequest<T> {
    private String url;
    private IXUIDataListener ixDownloadListener;
    VersionFacade downloadBuilder;
    private Dialog dialogdownloading;
    private XDownload xDownload;

    public void setIxDownloadListener(IXUIDataListener<? extends T> ixDownloadListener) {
        this.ixDownloadListener = ixDownloadListener;
    }

    public UpdateRequest(IXUIDataListener ixDownloadListener) {
        this.ixDownloadListener = ixDownloadListener;
    }

    public UpdateRequest() {
    }

    public IXUIDataListener getIxDownloadListener() {
        return ixDownloadListener;
    }


    public UpdateRequest requestUpdateCommonData(Context context, HttpHeaders headers, HttpParams params, HttpRequestMethod method) {
        if (TextUtils.isEmpty(this.url)) {
            return null;
        }
        switch (method) {
            case GET:
                throw new RuntimeException("get请求正在完善中。。。");

            case POST:
                PostRequest<T> postRequest = OkGo.post(this.url);
                postRequest.tag(this);
                handlerHeaders(postRequest, headers);
                handlerParams(postRequest, params);
                if (downloadBuilder.getCallback() != null) {
                    postRequest.execute(downloadBuilder.getCallback());
                } else {
                    postRequest.execute(new UpdateAbsCallback());
                }
                break;
            default:
        }
        return this;
    }

    public void handlerUIData(UIData uiData) throws Exception {
        if (downloadBuilder == null) {
            throw new Exception("VersionFacade没有执行excute方法！");
        }
        if (uiData != null) {
            if (uiData.getContext() == null) {
                throw new Exception("Context不能为空！");
            }
            if (TextUtils.isEmpty(uiData.getTitle())) {
                throw new Exception("Title不能为空！");
            }
            if (TextUtils.isEmpty(uiData.getDownloadUrl())) {
                throw new Exception("DownloadUrl不能为空！");
            }
            final Activity context = (Activity) uiData.getContext();
            XDownload.Builder builder = new XDownload.Builder();
            builder.setFileName(uiData.getTitle() + ".apk");
            builder.setMaxPoolSize(1);
            builder.setPath(downloadBuilder.getDownloadAPKPath());
            builder.setPriority(1);
            builder.setDownloadListener(new XDownLoadListener(uiData.getDownloadUrl(), uiData));
            GetRequest<File> request = OkGo.get(uiData.getDownloadUrl());
            builder.setRequest(request);
            xDownload = builder.bulider();
            Dialog dialog = null;
            if (downloadBuilder.getxDialogListener() != null) {
                dialog = downloadBuilder.getxDialogListener().getCustomVersionDialog(uiData.getContext(), uiData);
            }
            if (downloadBuilder.isForceRedownload()) {
                if (dialog == null) {
                    new MaterialDialog.Builder(uiData.getContext()).title(uiData.getTitle()).content(uiData.getContent()).positiveText("升级").onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (which == DialogAction.NEUTRAL) {

                            } else if (which == DialogAction.POSITIVE) {
                                updateVersionSure();
                            } else if (which == DialogAction.NEGATIVE) {

                            }
                        }
                    }).show().setCancelable(false);
                } else {
                    dialog.show();
                }
            } else {
                new MaterialDialog.Builder(uiData.getContext()).title(uiData.getTitle()).content(uiData.getContent()).positiveText("升级").negativeText("取消").onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.NEUTRAL) {

                        } else if (which == DialogAction.POSITIVE) {
                            updateVersionSure();
                        } else if (which == DialogAction.NEGATIVE) {

                        }
                    }
                }).show();
            }
        }
    }

    public void updateVersionSure() {
        xDownload.start();
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    public class XDownLoadListener extends DownloadListener {
        Context context;
        UIData uiData;

        public XDownLoadListener(Object tag, UIData uiData) {
            super(tag);
            this.context = uiData.getContext();
            this.uiData = uiData;
        }

        @Override
        public void onStart(Progress progress) {
            XDownloadingDialogListener xDownloadingDialogListener = downloadBuilder.getxDownloadingDialogListener();
            if (xDownloadingDialogListener != null) {
                dialogdownloading = xDownloadingDialogListener.getCustomDownloadingDialog(context, progress, this.uiData);
                if (dialogdownloading != null) {
                    dialogdownloading.show();
                } else {
                    dialogdownloading = new MaterialDialog.Builder(context).content("下载中...").progress(false, 100, true).show();

                }
            } else {
                dialogdownloading = new MaterialDialog.Builder(context).content("下载中...").progress(false, 100, true).show();

            }
            if (dialogdownloading instanceof MaterialDialog) {

            }
            if (downloadBuilder.isForceRedownload()) {
                dialogdownloading.setCancelable(false);
            }
        }

        @Override
        public void onProgress(Progress progress) {

            XDownloadingDialogListener xDownloadingDialogListener = downloadBuilder.getxDownloadingDialogListener();
            if (xDownloadingDialogListener != null) {
                xDownloadingDialogListener.updateUI(dialogdownloading, progress, this.uiData);
            } else {
                if (dialogdownloading instanceof MaterialDialog) {
                    int curprogress = (int) (progress.fraction * 100);
                    MaterialDialog materialDialog = (MaterialDialog) dialogdownloading;
                    materialDialog.setProgress(curprogress);

                }
            }
        }

        @Override
        public void onError(Progress progress) {
            if (dialogdownloading != null) {
                dialogdownloading.dismiss();
            }
            String errormessage = progress.exception.getMessage();
            String message = "下载失败!!!";
            if (errormessage.contains("failed: EACCES")) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                } else {
                    verifyStoragePermissions((Activity) context);
                    return;
                }
            }
            Dialog dialog;
            XDownloadFailedListener xDownloadFailedListener = downloadBuilder.getxDownloadFailedListener();
            if (xDownloadFailedListener != null) {
                dialog = xDownloadFailedListener.getCustomDownloadFailed(context, uiData);
                if (dialog != null) {
                } else {
                    dialog = new MaterialDialog.Builder(context).title("提示").content(message).build();
                }
            } else {
                dialog = new MaterialDialog.Builder(context).title("提示").content(message).positiveText("确定").build();
                dialog.show();
            }
            dialog.show();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            if (dialogdownloading != null) {
                dialogdownloading.dismiss();
            }
            installApk(context, file);

        }

        @Override
        public void onRemove(Progress progress) {
            if (dialogdownloading != null) {
                dialogdownloading.dismiss();
            }
        }
    }

    private void installApk(final Context context, File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //7.0以上系统
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);
    }

    private void handlerHeaders(Request request, HttpHeaders headers) {
        if (headers != null) {
            Set<String> set = headers.keySet();
            for (String item : set) {
                request.headers(item, headers.get(item).toString());
            }
        }
    }

    private void handlerParams(Request request, HttpParams params) {
        if (params != null) {
            Set<String> set = params.keySet();
            for (String item : set) {
                request.params(item, params.get(item).toString());
            }
        }
    }


    public void enqueueWork(Context context, VersionFacade downloadBuilder) {
        this.downloadBuilder = downloadBuilder;
        this.url = downloadBuilder.getRequestUrl();
        this.requestUpdateCommonData(context, downloadBuilder.getHeaders(), downloadBuilder.getParams(), downloadBuilder.getHttpRequestMethod());
    }

    private class UpdateAbsCallback extends AbsCallback<T> {
        @Override
        public void onStart(Request<T, ? extends Request> request) {
            super.onStart(request);
            if (ixDownloadListener != null) {
                if (ixDownloadListener instanceof IXRequestUrlListener) {
                    ((IXRequestUrlListener) ixDownloadListener).onStart(request);
                }


            }
        }

        @Override
        public void onError(Response<T> response) {
            super.onError(response);
            if (ixDownloadListener != null) {
                if (ixDownloadListener instanceof IXRequestUrlListener) {
                    ((IXRequestUrlListener) ixDownloadListener).onError(response);
                }

            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            if (ixDownloadListener != null) {
                if (ixDownloadListener instanceof IXRequestUrlListener) {
                    ((IXRequestUrlListener) ixDownloadListener).onFinish();
                }

            }
            OkGo.getInstance().cancelTag(this);
        }

        @Override
        public void onSuccess(Response<T> response) {
            T returndata = response.body();
            UIData uiData = null;
            if (returndata == null) {
                if (ixDownloadListener != null) {
                    uiData = ixDownloadListener.onSuccess(response);
                }
            } else {
                if (ixDownloadListener != null) {
                    uiData = ixDownloadListener.handXDownloadData(returndata);
                }
            }

            try {
                handlerUIData(uiData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public T convertResponse(okhttp3.Response response) throws Throwable {
            try {
                Type genType = getClass().getGenericSuperclass();//获得带有泛型的父类。
                Type[] params = ((ParameterizedType) genType).getActualTypeArguments();//获取参数化类型的数组
                Type mType = params[0];

                return parseType(response, mType);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private T parseType(okhttp3.Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        String str = body.string();//加密的Json
        T t = new Gson().fromJson(str, type);
        response.close();
        return t;
    }
}
