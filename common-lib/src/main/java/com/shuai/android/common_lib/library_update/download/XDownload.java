package com.shuai.android.common_lib.library_update.download;

import android.os.Environment;
import android.text.TextUtils;

import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.base.Request;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor;

import java.io.File;

/**
 * Created by 一米阳光 on 2018/2/26.
 */

public class XDownload implements XExecutor.OnAllTaskEndListener {

    OkDownload okDownload ;
    private Request request;
    private String taskTag = "download";
    private String fileName;
    private int priority;
    private DownloadListener downloadListener;
 static
 {

 }
    public XDownload(Builder builder) {
        okDownload = OkDownload.getInstance();
        request = builder.getRequest();
        downloadListener=builder.getDownloadListener();
        if(builder.getTaskTag()!=null) {
            taskTag = builder.getTaskTag();
        }
        fileName = builder.getFileName();
        priority = builder.getPriority();
        okDownload.setFolder(builder.getPath());
        okDownload.getThreadPool().setCorePoolSize(builder.getMaxPoolSize());
        okDownload.addOnAllTaskEndListener(this);

    }

    @Override
    public void onAllTaskEnd() {

    }

    public void start() {
        if(request!=null) {
            DownloadTask tast = OkDownload.request(taskTag, request);
            if (!TextUtils.isEmpty(fileName)) {
                tast.fileName(fileName);

            }

            tast.priority(priority);
            tast.save();
            if(downloadListener!=null)
            {
                tast.register(downloadListener);
            }
            tast.start();
        }
        else
        {

        }
    }


    /**
     * 下载管理器构建着
     */
    public static class Builder {
        private String path = Environment.getExternalStorageDirectory().getPath() + "/download/";
        private int maxPoolSize = 1;
        private Request request;
        private String taskTag;
        private String fileName;
        private int priority = 1;
        private DownloadListener downloadListener;

        public DownloadListener getDownloadListener() {
            return downloadListener;
        }

        public void setDownloadListener(DownloadListener downloadListener) {
            this.downloadListener = downloadListener;
        }
        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setTaskTag(String taskTag) {
            this.taskTag = taskTag;
        }

        public String getTaskTag() {
            if(TextUtils.isEmpty(taskTag))
            {
                return "";
            }
            return taskTag;
        }

        public void setRequest(GetRequest<File> request) {
            this.request = request;
        }

        public Request getRequest() {
            return request;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public XDownload bulider() {
            return new XDownload(this);
        }
    }
}
