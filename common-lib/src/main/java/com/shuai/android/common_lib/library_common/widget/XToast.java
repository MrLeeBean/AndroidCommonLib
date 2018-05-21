package com.shuai.android.common_lib.library_common.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuai.android.common_lib.R;
import com.shuai.android.common_lib.library_common.utils.DisplayUtils;
import com.shuai.android.common_lib.library_common.utils.ALog;


/**
 * 自定义吐司
 *
 * @author changshuai
 */
public class XToast {

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    public static final int SUCCESS = 1;
    public static final int FAIL = 3;
    public static final int INFO = 4;
    public static final int NOTHING = 5;


    public static void showInfoLong(Context context, String msg) {
        makeText(context, msg, XToast.LENGTH_LONG, XToast.INFO);
    }

    public static void showInfoShort(Context context, String msg) {
        makeText(context, msg, XToast.LENGTH_SHORT, XToast.INFO);
    }

    public static void showSuccessLong(Context context, String msg) {
        makeText(context, msg, XToast.LENGTH_LONG, XToast.SUCCESS);
    }

    public static void showSuccessShort(Context context, String msg) {
        makeText(context, msg, XToast.LENGTH_SHORT, XToast.SUCCESS);
    }

    public static void showFailLong(Context context, String msg) {
        makeText(context, msg, XToast.LENGTH_LONG, XToast.FAIL);
    }

    public static void showFailShort(Context context, String msg) {
        makeText(context, msg, XToast.LENGTH_SHORT, XToast.FAIL);
    }

    public static void showSimpleLong(Context context, String msg) {
        makeText(context, msg, XToast.LENGTH_LONG, XToast.NOTHING);
    }

    public static void showSimpleShort(Context context, String msg) {
        makeText(context, msg, XToast.LENGTH_SHORT, XToast.NOTHING);
    }

    static Toast t ;//静态变量虽然有内存泄漏问题，但是在可接受的范围内


    /**
     * 弹出自定义吐司
     *
     * @param context 上下文
     * @param msg     吐司内容
     * @param length  持续时间 SHORT=1秒，LONG=2秒
     * @param type    吐司类型
     */
    public static void makeText(Context context, String msg, int length, int type) {

        try {

            if (t==null){
                t = new Toast(context);//解决多次弹出Toast的问题
            }

            View layout = LayoutInflater.from(context).inflate(R.layout.base_x_toast_layout, null, false);

            ViewGroup contentWrap = (ViewGroup) layout.findViewById(R.id.contentWrap);

            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams imageViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(imageViewLP);

            if (SUCCESS == type) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.base_ic_x_tip_notify_done));
            } else if (FAIL == type) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.base_ic_x_tip_notify_error));
            } else if (INFO == type) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.base_ic_x_tip_notify_info));
            } else if (NOTHING == type) {

            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.base_ic_x_tip_notify_info));
            }

            contentWrap.addView(imageView);

            if (msg != null && msg.trim().length() > 0) {
                TextView tipView = new TextView(context);
                LinearLayout.LayoutParams tipViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                if (NOTHING != type) {
                    tipViewLP.topMargin = DisplayUtils.dp2px(context, 12);
                }
                tipView.setLayoutParams(tipViewLP);

                tipView.setEllipsize(TextUtils.TruncateAt.END);
                tipView.setGravity(Gravity.CENTER);
                tipView.setMaxLines(4);
                tipView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                tipView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tipView.setText(msg);

                contentWrap.addView(tipView);
            }

            t.setView(layout);//设置布局
            t.setDuration(length);//设置时长
            t.setGravity(Gravity.CENTER, 0, 0);//设置位置

            t.show();

        } catch (Exception e) {
            ALog.e("XToast弹出发生错误");
            e.printStackTrace();
        }

    }

}
