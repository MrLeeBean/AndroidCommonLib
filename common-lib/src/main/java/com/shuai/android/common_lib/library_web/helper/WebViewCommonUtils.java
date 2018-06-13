package com.shuai.android.common_lib.library_web.helper;

import android.support.v4.app.Fragment;

import com.shuai.android.common_lib.library_common.core.BusHelper;
import com.shuai.android.common_lib.library_config.router.BusConstants;
import com.shuai.android.common_lib.library_config.webview.WebViewConfig;

import java.io.Serializable;

/**
 * Created by changshuai on 2018/6/13.
 */

public class WebViewCommonUtils {

    /**
     * 退出WebView
     */
    public static void ExitWebView() {
        BusHelper.get().post(BusConstants.BUS_EVENT_CLOSE_WEBVIEW, "");
    }


    /**
     * 获取ExtraData
     *
     * @param fragment 当前Fragment
     * @return
     */
    public static Serializable getWebViewExtraData(Fragment fragment) {
        if (fragment == null) {
            return null;
        }
        Serializable serializable = fragment.getArguments().getSerializable(WebViewConfig.KEY_EXTRA_DATA);
        return serializable;
    }
}
