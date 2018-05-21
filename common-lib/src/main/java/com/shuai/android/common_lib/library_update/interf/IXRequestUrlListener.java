package com.shuai.android.common_lib.library_update.interf;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * Created by 一米阳光 on 2018/2/27.
 */

public interface IXRequestUrlListener<T>  extends IXUIDataListener<T> {
    void onStart(Request request);

    void onError(Response<T> response);
    void onFinish();
}
