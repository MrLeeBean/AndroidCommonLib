package com.shuai.android.common_lib.library_update.download;

import com.shuai.android.common_lib.library_update.net.HttpParams;
import com.shuai.android.common_lib.library_update.net.HttpRequestMethod;
import com.lzy.okgo.model.HttpHeaders;

/**
 * Created by 一米阳光 on 2018/2/26.
 */

public class XDownRequest   {
    private HttpRequestMethod requestMethod;
    private HttpHeaders httpHeaders;
    private HttpParams requestParams;
    private String requestUrl;
    public XDownRequest() {
        requestMethod=HttpRequestMethod.POST;
    }

    public HttpRequestMethod getRequestMethod() {
        return requestMethod;
    }

    public XDownRequest setRequestMethod(HttpRequestMethod requestMethod) {
        this.requestMethod = requestMethod;
        return  this;
    }
}
