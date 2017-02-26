package com.coolapk.market.util;

import android.support.annotation.Keep;

@Keep
public class AuthUtils {
    @Keep
    public static native String getAS(String str);

    static {
        System.loadLibrary("a");
    }
}
