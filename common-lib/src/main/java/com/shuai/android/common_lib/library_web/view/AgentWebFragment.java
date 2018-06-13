package com.shuai.android.common_lib.library_web.view;


import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuai.android.common_lib.R;
import com.shuai.android.common_lib.library_common.application.BaseApplication;
import com.shuai.android.common_lib.library_common.utils.ALog;
import com.shuai.android.common_lib.library_common.utils.DisplayUtils;
import com.shuai.android.common_lib.library_common.utils.StatusBarUtils;
import com.shuai.android.common_lib.library_common.widget.XToast;
import com.shuai.android.common_lib.library_config.webview.WebViewConfig;
import com.shuai.android.common_lib.library_web.common.FragmentKeyDown;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;

import org.apache.http.util.EncodingUtils;


/**
 * 默认的WebView
 */
public class AgentWebFragment extends Fragment implements FragmentKeyDown {

    protected AgentWeb mAgentWeb;
    protected TextView mToolbarTitle;//ToolBar标题
    protected ImageView mToolbarBack;//ToolBar返回
    protected ImageView mToolbarClose;//ToolBar关闭

    protected DownloadingService mDownloadingService;

    public static final String TAG = AgentWebFragment.class.getSimpleName();


    /**
     * 初始化AgentWebFragment
     *
     * @param bundle 从上层传递过来的Bundle参数
     */
    public static AgentWebFragment getInstance(Bundle bundle) {

        AgentWebFragment mAgentWebFragment = new AgentWebFragment();
        if (bundle != null)
            mAgentWebFragment.setArguments(bundle);

        return mAgentWebFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boolean barLightMode = this.getArguments().getBoolean(WebViewConfig.KEY_BAR_LIGHT_MODE);
        if (barLightMode){
            StatusBarUtils.setStatusBarLightMode(getActivity());//设置状态栏颜色字体为深色
            return inflater.inflate(R.layout.base_layout_web_fragment_agentweb_light, container, false);
        }
        return inflater.inflate(R.layout.base_layout_web_fragment_agentweb, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAgentWeb(view);//初始化AgentWeb
        initView(view);//初始化页面View
        initPost();//初始化post请求
        initExtraWebSettings(mAgentWeb.getWebCreator().getWebView());//初始化其他settings //mAgentWeb.getWebCreator().getWebView()  -->   获取WebView

    }

    /**
     * 初始化AgentWeb
     *
     * @param view
     */
    protected void initAgentWeb(View view) {
        //初始化AgentWeb
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((ViewGroup) view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件
                .useDefaultIndicator(-1, 3)          //设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())             //设置 IAgentWebSettings。
                .setWebViewClient(mWebViewClient)                  //WebViewClient,与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebChromeClient(mWebChromeClient)              //WebChromeClient
                .setPermissionInterceptor(mPermissionInterceptor)  //权限拦截
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)   //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setMainFrameErrorView(R.layout.base_layout_web_layout_error, -1)   //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                //.useMiddleWareWebChrome(getMiddleWareWebChrome())    //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
                //.useMiddleWareWebClient(getMiddleWareWebClient())    //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)  //打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl()                                          //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()           //创建AgentWeb
                .ready()                    //设置 WebSettings
                .go(getGetOrPostUrl());     //WebView载入该url地址的页面并显示。如果post请求，则为null
        //AgentWebConfig.debug();

    }

    /**
     * 初始化UI页面
     *
     * @param view
     */
    protected void initView(View view) {
        mToolbarBack = (ImageView) view.findViewById(R.id.toolbar_back);
        mToolbarClose = (ImageView) view.findViewById(R.id.toolbar_close);
        mToolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);

        mToolbarClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        mToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mAgentWeb.back())
                    getActivity().finish();
            }
        });


    }

    /**
     * AgentWeb 没有把WebView的功能全面覆盖,所以某些设置 AgentWeb 没有提供,请从WebView方面入手设置。
     *
     * @param webView
     */
    protected void initExtraWebSettings(WebView webView) {
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
    }

    /**
     * 初始化post请求
     */
    protected void initPost() {
        //满足Post条件。
        if (isPost()) {
            ALog.d("WebView的Post请求提交参数postData：" + this.getArguments().getString(WebViewConfig.KEY_POST_DATA));

            if (!(this.getArguments().getString(WebViewConfig.KEY_URL) != null && !TextUtils.isEmpty(this.getArguments().getString(WebViewConfig.KEY_URL).trim()))) {
                XToast.showInfoLong(BaseApplication.getInstance(), "网页链接为空");
            }

            mAgentWeb.getWebCreator().getWebView().postUrl(this.getArguments().getString(WebViewConfig.KEY_URL), this.getArguments().getString(WebViewConfig.KEY_POST_DATA) != null ? EncodingUtils.getBytes(this.getArguments().getString(WebViewConfig.KEY_POST_DATA), "BASE64") : new byte[]{});
        }

    }

    /**
     * 是否是post请求
     *
     * @return
     */
    protected boolean isPost() {
        return this.getArguments().getString(WebViewConfig.KEY_POST_DATA) != null && !TextUtils.isEmpty(this.getArguments().getString(WebViewConfig.KEY_POST_DATA).trim());
    }


    /**
     * 获取url
     * target指的是返回的最终的url值
     * keyUrl指的是getExtra获取到的WebViewConfig.KEY_URL
     * 如果是post请求，这里target会为null
     * 如果是get请求，这里target为WebViewConfig.KEY_URL
     */
    public String getGetOrPostUrl() {
        String keyUrl = this.getArguments().getString(WebViewConfig.KEY_URL);
        String target = "";

        if (isPost()) {
            ALog.d("WebView请求方式：POST请求。" + "WebView首次打开的页面URL：" + keyUrl);

            target = null;

        } else {
            ALog.d("WebView请求方式：GET请求。" + "WebView首次打开的页面URL：" + keyUrl);

            if (TextUtils.isEmpty(target = keyUrl == null ? null : keyUrl.trim())) {
                //target = "about:black";
                XToast.showInfoLong(BaseApplication.getInstance(), "网页链接为空");
            }

        }

        return target;
    }


    /**
     * 权限拦截
     */
    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        //AgentWeb 在触发某些敏感的 Action 时候会回调该方法， 比如定位触发 。
        //例如 https//:www.baidu.com 该 Url 需要定位权限， 返回false ，如果版本大于等于23 ， agentWeb 会动态申请权限 ，true 该Url对应页面请求定位失败。
        //该方法是每次都会优先触发的 ， 开发者可以做一些敏感权限拦截 。

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * AgentWeb 是用自己的权限机制的 ，true 该Url对应页面请求定位权限拦截 ，false 默认允许。
         * @param url
         * @param permissions
         * @param action
         * @return
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            ALog.i(TAG, "url:" + url + "  permission:" + permissions + " action:" + action);
            return false;
        }
    };


    /**
     * 设置Settings
     *
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        DefaultDownloadImpl
                                .create((Activity) webView.getContext(),
                                        webView,
                                        mDownloadListenerAdapter,
                                        mDownloadListenerAdapter,
                                        this.mAgentWeb.getPermissionInterceptor()));
            }
        };
    }

    /**
     * 下载监听。更新于 AgentWeb  4.0.0
     */
    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {

        /**
         *
         * @param url                下载链接
         * @param userAgent          UserAgent
         * @param contentDisposition ContentDisposition
         * @param mimetype           资源的媒体类型
         * @param contentLength      文件长度
         * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
         * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
         */
        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            ALog.i(TAG, "onStart:" + url);
            extra.setOpenBreakPointDownload(false)
                    .setIcon(R.drawable.ic_file_download_black_24dp)
                    .setConnectTimeOut(6000)
                    .setBlockMaxTime(2000)
                    .setDownloadTimeOut(Long.MAX_VALUE) // 下载超时
                    .setParallelDownload(false)  // 串行下载更节省资源哦
                    .setEnableIndicator(true)  // false 关闭进度通知
                    .setDownloadTimeOut(60L * 5L * 1000L)
                    .addHeader("Cookie", "xx") // 自定义请求头
                    .setAutoOpen(true) // 下载完成自动打开
                    .setForceDownload(false); // 强制下载，不管网络网络类型
            return false;
        }

        /**
         *
         * 不需要暂停或者停止下载该方法可以不必实现
         * @param url
         * @param downloadingService  用户可以通过 DownloadingService#shutdownNow 终止下载
         */
        @Override
        public void onBindService(String url, DownloadingService downloadingService) {
            super.onBindService(url, downloadingService);
            mDownloadingService = downloadingService;
            ALog.i(TAG, "onBindService:" + url + "  DownloadingService:" + downloadingService);
        }

        /**
         * 回调onUnbindService方法，让用户释放掉 DownloadingService。
         * @param url
         * @param downloadingService
         */
        @Override
        public void onUnbindService(String url, DownloadingService downloadingService) {
            super.onUnbindService(url, downloadingService);
            mDownloadingService = null;
            ALog.i(TAG, "onUnbindService:" + url);
        }

        /**
         *
         * @param url  下载链接
         * @param loaded  已经下载的长度
         * @param length    文件的总大小
         * @param usedTime   耗时 ，单位ms
         * 注意该方法回调在子线程 ，线程名 AsyncTask #XX 或者 AgentWeb # XX
         */
        @Override
        public void onProgress(String url, long loaded, long length, long usedTime) {
            int mProgress = (int) ((loaded) / Float.valueOf(length) * 100);
            ALog.i(TAG, "onProgress:" + mProgress);
            super.onProgress(url, loaded, length, usedTime);
        }

        /**
         *
         * @param path 文件的绝对路径
         * @param url  下载地址
         * @param throwable    如果异常，返回给用户异常
         * @return true 表示用户处理了下载完成后续的事件 ，false 默认交给AgentWeb 处理
         */
        @Override
        public boolean onResult(String path, String url, Throwable throwable) {
            if (null == throwable) { //下载成功
                //do you work
            } else {//下载失败

            }
            return false; // true  不会发出下载完成的通知 , 或者打开文件
        }
    };


    /**
     * WebChromeClient
     */
    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //  super.onProgressChanged(view, newProgress);
            //Log.i(TAG,"onProgressChanged:"+newProgress+"  view:"+view);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

            //自定义了Title且数据里不为null
            if (AgentWebFragment.this.getArguments().getString(WebViewConfig.KEY_TITLE) != null) {
                String tit = AgentWebFragment.this.getArguments().getString(WebViewConfig.KEY_TITLE);
                if (tit.length() > 14) {
                    tit = tit.substring(0, 14).concat("...");
                }
                if (mToolbarTitle != null) {
                    mToolbarTitle.setText(tit);
                }
                return;
            }

            //没有自定义Title或者数据里为null-采取<title>标签对中的数据
            if (mToolbarTitle != null && !TextUtils.isEmpty(title)) {
                if (title.length() > 14)
                    title = title.substring(0, 14).concat("...");
            }
            if (mToolbarTitle != null) {
                mToolbarTitle.setText(title);
            }


        }
    };

    /**
     * WebViewClient
     */
    protected WebViewClient mWebViewClient = new WebViewClient() {

        //加载https信任。
        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            //sslErrorHandler.cancel();//默认的处理方式，WebView变成空白页
            sslErrorHandler.proceed();//接受证书，解决部分非https页面证书问题导致的空白。

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            ALog.d("WebView页面内响应的URL:" + url);

            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?...package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://") && url.contains("com.youku.phone"))
                return true;
            /*else if (isAlipay(view, url))   //1.2.5开始不用调用该方法了 ，只要引入支付宝sdk即可 ， DefaultWebClient 默认会处理相应url调起支付宝
              return true;*/


            return false;
        }

    };


    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toCleanWebCache();//退出自动清除缓存。
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();

    }

    /**
     * 清除 WebView 缓存
     */
    private void toCleanWebCache() {

        try {
            if (this.mAgentWeb != null) {

                //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
                this.mAgentWeb.clearWebCache();
                ALog.d("WebView已清理缓存");

                //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
                //AgentWebConfig.clearDiskCache(this.getContext());
            }

        } catch (Exception e) {

        }


    }

}
