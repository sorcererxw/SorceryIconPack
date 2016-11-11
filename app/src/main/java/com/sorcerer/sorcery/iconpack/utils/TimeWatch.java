package com.sorcerer.sorcery.iconpack.utils;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/9/10
 */
public class TimeWatch {
    private long mLastTime;

    public TimeWatch() {
        resetTime();
    }

    public void resetTime() {
        mLastTime = System.currentTimeMillis();
    }

    public long consumeTime(boolean resetLastTime) {
        long res = System.currentTimeMillis() - mLastTime;
        if (resetLastTime) {
            resetTime();
        }
        return res;
    }
}
