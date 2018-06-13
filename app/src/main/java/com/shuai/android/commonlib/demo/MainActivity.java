package com.shuai.android.commonlib.demo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hwangjr.rxbus.Bus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.shuai.android.common_lib.library_common.core.BusHelper;
import com.shuai.android.common_lib.library_common.widget.XToast;
import com.shuai.android.common_lib.library_config.router.BusConstants;
import com.shuai.android.common_lib.library_web.helper.WebViewLauncher;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BusHelper.get().register(this);


        final String url = "https://hyd.hengchang6.com/jsd/static/html/phoneAuth/operatorlicensey.html?phoneNumber=18811123875&tokenId=ASTy61oZPrDpgXSEBgQMyaPg_tuZZ39mZD6TX92NAJZuL0XBc8TKeWXSiKJc8LMzrP8FCjVjh36BZ4Uvyr4xIdTuU9XzDz64EULYlyydnAmIEBUtkWtWvcXgQBmTwl5b22-gwAgcR70KgIcIdvfy9Y9ia9KOoY3uxw9QuRmuvazszQ1XT37BI8zS&sourceType=3&deviceCode=352709083345249&version=3.0.6&address=https://hyd.hengchang6.com&forPlat=crm&userName=";
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebViewLauncher.Builder(MainActivity.this)
                        .setUrl(url)
                        .setBarLightMode(true)
                        .setTheme(R.style.AppTheme)
                        .build()
                        .go();
//                        .goNaWeb("hengchang://puhui.com/?hc_parms=");
            }
        });
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag(value = BusConstants.BUS_EVENT_NA_WEB_ALLPARAMS)})

    public void onWebviewDataReceived(HashMap map) {//这里的s就是WebView交互的数据


        Activity ac = (Activity) map.get(BusConstants.BUS_EVENT_NA_WEB_ALLPARAMS_AC);//activity对象
        String s = (String) map.get(BusConstants.BUS_EVENT_NA_WEB_ALLPARAMS_DATA);//params

        XToast.showInfoShort(ac, ac.getClass().getName().toString());

        //如果有特殊的协议，请在这里写：

        //code...你对协议的处理写在这里。

    }
}
