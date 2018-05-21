package com.shuai.android.common_lib.library_update.interf;

import android.app.Dialog;
import android.content.Context;

import com.shuai.android.common_lib.library_update.UIData;

/**
 * Created by allenliu on 2018/1/18.
 */

public interface XVersionDialogListener {
    Dialog getCustomVersionDialog(Context context, UIData versionBundle);
}
