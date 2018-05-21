package com.shuai.android.common_lib.library_web.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StyleRes;

import com.shuai.android.common_lib.library_config.webview.WebViewConfig;
import com.shuai.android.common_lib.library_web.view.AgentWebFragment;
import com.shuai.android.common_lib.library_web.view.WebMainActivity;

import java.io.Serializable;

/**
 * WebView工具类
 */

public class WebViewLauncher {

    private Context context;
    private String url;
    private String title;
    private String postData;
    private Serializable extraData;
    private int theme;

    public WebViewLauncher(WebViewLauncher.Builder builder) {
        this.context = builder.context;
        this.url = builder.url;
        this.title = builder.title;
        this.postData = builder.postData;
        this.extraData = builder.extraData;
        this.theme =builder.theme;
    }

    /**
     * Default
     */
    public void go() {
        Intent i = new Intent(context, WebMainActivity.class);
        i.putExtra(WebViewConfig.KEY_TYPE, WebViewConfig.WEB_TYPE_DEFAULT);
        i.putExtra(WebViewConfig.KEY_TITLE, title);
        i.putExtra(WebViewConfig.KEY_URL, url);
        i.putExtra(WebViewConfig.KEY_POST_DATA, postData);
        i.putExtra(WebViewConfig.KEY_EXTRA_DATA, extraData);
        i.putExtra(WebViewConfig.KEY_THEME,theme);
        context.startActivity(i);
    }

    /**
     * NA-WEB
     */
    public void goNaWeb() {
        Intent i = new Intent(context, WebMainActivity.class);
        i.putExtra(WebViewConfig.KEY_TYPE, WebViewConfig.WEB_TYPE_NAWEB);
        i.putExtra(WebViewConfig.KEY_TITLE, title);
        i.putExtra(WebViewConfig.KEY_URL, url);
        i.putExtra(WebViewConfig.KEY_POST_DATA, postData);
        i.putExtra(WebViewConfig.KEY_EXTRA_DATA, extraData);
        i.putExtra(WebViewConfig.KEY_THEME,theme);
        context.startActivity(i);
    }

    /**
     * Custom
     *
     * @param aClassAgentWebFragment
     */
    public void goCustom(Class<? extends AgentWebFragment> aClassAgentWebFragment) {
        Intent i = new Intent(context, WebMainActivity.class);
        i.putExtra(WebViewConfig.KEY_TYPE, WebViewConfig.WEB_TYPE_CUSTOM);
        i.putExtra(WebViewConfig.KEY_FRAGMENT, aClassAgentWebFragment);
        i.putExtra(WebViewConfig.KEY_TITLE, title);
        i.putExtra(WebViewConfig.KEY_URL, url);
        i.putExtra(WebViewConfig.KEY_POST_DATA, postData);
        i.putExtra(WebViewConfig.KEY_EXTRA_DATA, extraData);
        i.putExtra(WebViewConfig.KEY_THEME,theme);
        context.startActivity(i);
    }


    public static class Builder {
        private Context context;
        private String url;
        private String title;
        private String postData;
        private Serializable extraData;
        private int theme;


        public Builder(Context context) {
            this.context = context;
        }

        public WebViewLauncher.Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public WebViewLauncher.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public WebViewLauncher.Builder setPostData(String postData) {
            this.postData = postData;
            return this;
        }

        public WebViewLauncher.Builder setExtraData(Serializable extraData) {
            this.extraData = extraData;
            return this;
        }

        public WebViewLauncher.Builder setTheme(@StyleRes final int theme) {
            this.theme = theme;
            return this;
        }

        public WebViewLauncher build() {
            WebViewLauncher web = new WebViewLauncher(this);
            return web;
        }
    }


}
