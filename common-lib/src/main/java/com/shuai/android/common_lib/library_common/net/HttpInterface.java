package com.shuai.android.common_lib.library_common.net;

/**
 * Created by dell-7020 on 2017/11/20.
 */

public interface HttpInterface {
    interface DataCallback<T> {
        void onCallback(boolean bResult, Object data, Object tagData);
    }
}
