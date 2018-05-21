package com.shuai.android.common_lib.library_common.core;

import com.hwangjr.rxbus.Bus;

/**
 * RxBus单例
 */

public final class BusHelper {
    private static Bus sBus;

    public static synchronized Bus get() {
        if (sBus == null) {
            sBus = new Bus();
        }
        return sBus;
    }
}