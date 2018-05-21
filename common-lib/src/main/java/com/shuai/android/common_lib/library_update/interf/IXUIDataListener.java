package com.shuai.android.common_lib.library_update.interf;

import com.shuai.android.common_lib.library_update.UIData;
import com.lzy.okgo.model.Response;

/**
 * Created by 一米阳光 on 2018/2/27.
 */

public interface IXUIDataListener<T>  {
    UIData handXDownloadData(T data);
    UIData onSuccess(Response<?> response);

}
