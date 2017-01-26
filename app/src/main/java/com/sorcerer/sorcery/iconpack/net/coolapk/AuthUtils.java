package com.sorcerer.sorcery.iconpack.net.coolapk;

public class AuthUtils {
    public static native String getAS(String str);

    static {
        System.loadLibrary("a");
    }
}
