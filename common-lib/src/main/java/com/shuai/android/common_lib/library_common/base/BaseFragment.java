package com.shuai.android.common_lib.library_common.base;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }

    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }

}
