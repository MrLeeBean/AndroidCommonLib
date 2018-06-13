package com.shuai.android.common_lib.library_web.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.shuai.android.common_lib.R;
import com.shuai.android.common_lib.library_common.base.BaseActivity;
import com.shuai.android.common_lib.library_common.core.BusHelper;
import com.shuai.android.common_lib.library_common.exception.AppExceptionHandler;
import com.shuai.android.common_lib.library_common.utils.ALog;
import com.shuai.android.common_lib.library_config.router.BusConstants;
import com.shuai.android.common_lib.library_config.webview.WebViewConfig;
import com.shuai.android.common_lib.library_web.common.FragmentKeyDown;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * @author changshuai
 * @Description: WebView封装
 */

public class WebMainActivity extends BaseActivity {

    private FrameLayout mFrameLayout;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            int theme = getIntent().getIntExtra(WebViewConfig.KEY_THEME,R.style.BaseAppTheme);
            setTheme(theme);//设置主题
        }catch (Exception e){
            AppExceptionHandler.doHandle(e,"设置WebView出题发生异常");
        }


        setContentView(R.layout.base_layout_web_activity_common);

        BusHelper.get().register(this);

        mFrameLayout = this.findViewById(R.id.container_framelayout);

        String key = getIntent().getStringExtra(WebViewConfig.KEY_TYPE);
        Class<? extends AgentWebFragment> aClassAgentWebFragment = (Class<? extends AgentWebFragment>) getIntent().getSerializableExtra(WebViewConfig.KEY_FRAGMENT);

        //如果key空，或者有误，则为默认
        if (key == null || TextUtils.isEmpty(key) /*|| aClassAgentWebFragment == null*/) {
            key = WebViewConfig.WEB_TYPE_DEFAULT;
        }

        //如果key为custom，且aClassAgentWebFragment为空，则为默认。
        if (WebViewConfig.WEB_TYPE_CUSTOM.equals(key) && aClassAgentWebFragment == null){
            key = WebViewConfig.WEB_TYPE_DEFAULT;
        }


        String url = getIntent().getStringExtra(WebViewConfig.KEY_URL);
        String title = getIntent().getStringExtra(WebViewConfig.KEY_TITLE);
        String postData = getIntent().getStringExtra(WebViewConfig.KEY_POST_DATA);
        Serializable extraData = getIntent().getSerializableExtra(WebViewConfig.KEY_EXTRA_DATA);
        boolean barLightMode = getIntent().getBooleanExtra(WebViewConfig.KEY_BAR_LIGHT_MODE,false);

        String nawebInterceptStr = getIntent().getStringExtra(WebViewConfig.KEY_NAWEB_INTERCEPT_STR);

        mFragmentManager = this.getSupportFragmentManager();

        openFragment(key, url, title, postData, aClassAgentWebFragment, extraData,nawebInterceptStr,barLightMode);

    }


    private AgentWebFragment mAgentWebFragment;

    private void openFragment(String key, String url, String title, String postData, Class<? extends AgentWebFragment> aClassAgentWebFragment, Serializable extraData,String nawebInterceptStr ,boolean barLightMode) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;

        ALog.d("WebView类型：" + key);
        switch (key) {

            /*默认Fragment 使用AgenWeb*/
            case WebViewConfig.WEB_TYPE_DEFAULT:
                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
                mBundle.putString(WebViewConfig.KEY_URL, url);
                mBundle.putString(WebViewConfig.KEY_TITLE, title);
                mBundle.putString(WebViewConfig.KEY_POST_DATA, postData);
                mBundle.putSerializable(WebViewConfig.KEY_EXTRA_DATA, extraData);
                mBundle.putBoolean(WebViewConfig.KEY_BAR_LIGHT_MODE, barLightMode);
                break;


            /*NA-WEB*/
            case WebViewConfig.WEB_TYPE_NAWEB:
                ft.add(R.id.container_framelayout, mAgentWebFragment = NABridgeWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
                mBundle.putString(WebViewConfig.KEY_URL, url);
                mBundle.putString(WebViewConfig.KEY_TITLE, title);
                mBundle.putString(WebViewConfig.KEY_POST_DATA, postData);
                mBundle.putSerializable(WebViewConfig.KEY_EXTRA_DATA, extraData);
                mBundle.putSerializable(WebViewConfig.KEY_NAWEB_INTERCEPT_STR, nawebInterceptStr);
                mBundle.putBoolean(WebViewConfig.KEY_BAR_LIGHT_MODE, barLightMode);
                break;

            /*自定义*/
            case WebViewConfig.WEB_TYPE_CUSTOM:

                Method[] method = aClassAgentWebFragment.getDeclaredMethods();

                for (Method m : method) {

                    if ("getInstance".equals(m.getName())) {//id不设置空。
                        try {
                            mAgentWebFragment = (AgentWebFragment) m.invoke(null, new Object[]{mBundle = new Bundle()});
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                ft.add(R.id.container_framelayout, mAgentWebFragment, AgentWebFragment.class.getName());
                mBundle.putString(WebViewConfig.KEY_URL, url);
                mBundle.putString(WebViewConfig.KEY_TITLE, title);
                mBundle.putString(WebViewConfig.KEY_POST_DATA, postData);
                mBundle.putSerializable(WebViewConfig.KEY_EXTRA_DATA, extraData);
                mBundle.putBoolean(WebViewConfig.KEY_BAR_LIGHT_MODE, barLightMode);
                break;


        }
        ft.commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        AgentWebFragment mAgentWebFragment = this.mAgentWebFragment;
        if (mAgentWebFragment != null) {
            FragmentKeyDown mFragmentKeyDown = mAgentWebFragment;
            if (mFragmentKeyDown.onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    /**
     * 提供关闭当前WebView的方法。
     *
     * @param s
     */
    @Subscribe(tags = {@Tag(value = BusConstants.BUS_EVENT_CLOSE_WEBVIEW)})
    public void onCloseEventReceived(String s) {

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusHelper.get().unregister(this);

    }
}

