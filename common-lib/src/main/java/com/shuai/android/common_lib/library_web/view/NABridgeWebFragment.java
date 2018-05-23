package com.shuai.android.common_lib.library_web.view;


import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.shuai.android.common_lib.R;
import com.shuai.android.common_lib.library_common.exception.AppExceptionHandler;
import com.shuai.android.common_lib.library_common.core.BusHelper;
import com.shuai.android.common_lib.library_common.utils.ALog;
import com.shuai.android.common_lib.library_common.utils.LangUtils;
import com.shuai.android.common_lib.library_config.router.BusConstants;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.shuai.android.common_lib.library_config.webview.WebViewConfig;

import java.net.URLDecoder;


/**
 * NA-WEB协议
 */

public class NABridgeWebFragment extends AgentWebFragment {


    public static NABridgeWebFragment getInstance(Bundle bundle) {

        NABridgeWebFragment mBounceWebFragment = new NABridgeWebFragment();
        if (mBounceWebFragment != null)
            mBounceWebFragment.setArguments(bundle);

        return mBounceWebFragment;
    }


    @Override
    protected void initAgentWeb(View view) {

        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((ViewGroup) view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebViewClient(mNABridgeWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebChromeClient(mWebChromeClient) //WebChromeClient
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setMainFrameErrorView(R.layout.base_layout_web_layout_error, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                //.useMiddleWareWebChrome(getMiddleWareWebChrome()) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
                //.useMiddleWareWebClient(getMiddleWareWebClient()) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb
                .ready()//设置 WebSettings
                .go(getGetOrPostUrl()); //WebView载入该url地址的页面并显示。

    }

    /**
     * NA-WEB -- > WebViewClient
     */
    protected WebViewClient mNABridgeWebViewClient = new WebViewClient() {

        //加载https信任。
        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            //sslErrorHandler.cancel();//默认的处理方式，WebView变成空白页
            sslErrorHandler.proceed();//接收证书

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }


        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {

            ALog.d("WebView页面内响应的URL:"+url);
            String nawebInterceptStr = NABridgeWebFragment.this.getArguments().getString(WebViewConfig.KEY_NAWEB_INTERCEPT_STR);
            if (LangUtils.isNullOrEmpty(nawebInterceptStr)){
                return false;
            }

            //NA-WEB 协议内容处理
            if (url.contains(/*"hengchang://puhui.com/?hc_parms="*/nawebInterceptStr)) {
                if (!url.trim().equals("")) {
                    try {

                        int last = url.lastIndexOf(nawebInterceptStr);
                        String lastStr = url.substring(last + nawebInterceptStr.length());
                        String params = URLDecoder.decode(lastStr, "UTF-8");
                        ALog.d("NA-Web交互数据:" + params);

                        BusHelper.get().post(BusConstants.BUS_EVENT_NA_WEB_PARAMS, params);

                        return true;
                    } catch (Exception e) {

                        AppExceptionHandler.doHandle(e,"WebView解析NA-WEB协议发生异常");
                        return false;

                    }
                }
                return true;
            }
            return false;
        }
    };



}
