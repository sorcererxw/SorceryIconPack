package com.sorcererxw.rxactivityresult;

import android.content.Intent;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/15
 */

public class Request {
    private final Intent mIntent;
    private OnResult mOnResult;

    public Request(Intent intent) {
        mIntent = intent;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public OnResult getOnResult() {
        return mOnResult;
    }

    public void setOnResult(OnResult onResult) {
        mOnResult = onResult;
    }
}
