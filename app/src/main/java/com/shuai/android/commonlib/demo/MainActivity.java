package com.shuai.android.commonlib.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.shuai.android.common_lib.library_web.helper.WebViewLauncher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebViewLauncher.Builder(MainActivity.this)
                        .setUrl("http://www.baidu.com")
                        .build()
                        .goNaWeb();
            }
        });
    }
}
