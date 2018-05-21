package com.shuai.android.common_lib.library_common.widget;

import android.content.Context;

import com.shuai.android.common_lib.library_common.exception.AppExceptionHandler;
import com.shuai.android.common_lib.library_common.application.BaseApplication;
import com.shuai.android.common_lib.library_common.utils.LangUtils;


/**
 * Loading和提示框
 */

public class XTip {


    public static void dismiss(XTipDialog dialog) {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            AppExceptionHandler.doHandle(e, "XTip-Loading在dismiss发生错误");
        }

    }

    //--------------------Loading----------------------//

    public static XTipDialog loading(Context context) {
        return show(context, XTipDialog.Builder.ICON_TYPE_LOADING, "加载中...", false, 0, true);
    }

    public static XTipDialog loading(Context context, String msg) {
        return show(context, XTipDialog.Builder.ICON_TYPE_LOADING, msg, false, 0, true);
    }


    /**
     * 展示Loading
     *
     * @param context    上下文
     * @param msg        提示信息
     * @param cancelable 返回键是否可以取消
     * @return
     */
    public static XTipDialog loading(Context context, String msg, boolean cancelable) {
        return show(context, XTipDialog.Builder.ICON_TYPE_LOADING, msg, false, 0, cancelable);
    }


    //--------------------Tip----------------------//

    public static XTipDialog showInfoShort(Context context, String msg) {
        return show(context, XTipDialog.Builder.ICON_TYPE_INFO, msg,3000);
    }
    public static XTipDialog showInfoLong(Context context, String msg) {
        return show(context, XTipDialog.Builder.ICON_TYPE_INFO, msg,5000);
    }

    public static XTipDialog showSuccessShort(Context context, String msg) {
        return show(context, XTipDialog.Builder.ICON_TYPE_SUCCESS, msg,3000);
    }
    public static XTipDialog showSuccessLong(Context context, String msg) {
        return show(context, XTipDialog.Builder.ICON_TYPE_SUCCESS, msg,5000);
    }

    public static XTipDialog showFailShort(Context context, String msg) {
        return show(context, XTipDialog.Builder.ICON_TYPE_FAIL, msg,3000);
    }
    public static XTipDialog showFailLong(Context context, String msg) {
        return show(context, XTipDialog.Builder.ICON_TYPE_FAIL, msg,5000);
    }

    public static XTipDialog showSimpleShort(Context context, String msg) {
        return show(context, XTipDialog.Builder.ICON_TYPE_NOTHING, msg,3000);
    }
    public static XTipDialog showSimpleLong(Context context, String msg) {
        return show(context, XTipDialog.Builder.ICON_TYPE_NOTHING, msg,5000);
    }


    /*自动消失，返回键可取消。===>可设置类型、文字、时长*/
    private static XTipDialog show(Context context, @XTipDialog.Builder.IconType int iconType, String msg, long delayMillis) {
        return show(context, iconType, msg, true, delayMillis, true);
    }


    /**
     * 展示XTip
     *
     * @param context     上下文
     * @param iconType    类型
     * @param msg         提示文字
     * @param autoDismiss 是否自动消失
     * @param delayMillis 自动消失的时长
     * @param cancelable  系统返回键是否可以取消
     * @return
     */
    private static XTipDialog show(Context context, @XTipDialog.Builder.IconType int iconType, String msg, boolean autoDismiss, long delayMillis, boolean cancelable) {
        try {

            if (LangUtils.isStrNullOrEmpty(msg)) {
                msg = null;
            }

            final XTipDialog tipDialog = new XTipDialog.Builder(context)
                    .setIconType(iconType)
                    .setTipWord(msg)
                    .create(cancelable);
            tipDialog.show();

            if (autoDismiss) {

                if (delayMillis <= 0) {
                    delayMillis = 3000L;
                }

                BaseApplication.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        tipDialog.dismiss();
                    }
                }, delayMillis);
            }

            return tipDialog;
        } catch (Exception e) {
            AppExceptionHandler.doHandle(e, "XTipDialog在弹出时发生错误");
            return null;
        }
    }
}
