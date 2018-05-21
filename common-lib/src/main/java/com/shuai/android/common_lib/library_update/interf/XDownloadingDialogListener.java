package com.shuai.android.common_lib.library_update.interf;

import android.app.Dialog;
import android.content.Context;

import com.shuai.android.common_lib.library_update.UIData;
import com.lzy.okgo.model.Progress;

/**
 * Created by allenliu on 2018/1/18.
 */

public interface XDownloadingDialogListener {
    Dialog getCustomDownloadingDialog(Context context, Progress progress, UIData versionBundle);

    void updateUI(Dialog dialog, Progress progress, UIData versionBundle);
}
