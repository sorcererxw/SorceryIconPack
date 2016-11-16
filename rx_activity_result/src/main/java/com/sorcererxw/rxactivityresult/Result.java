package com.sorcererxw.rxactivityresult;

import android.app.Activity;
import android.content.Intent;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/15
 */

public class Result<T> {
    private final T mActivity;
    private final int mResultCode;
    private final Intent mData;

    public Result(T activity, int resultCode, Intent data) {
        mActivity = activity;
        mResultCode = resultCode;
        mData = data;
    }

    public int getResultCode() {
        return mResultCode;
    }

    public Intent getData() {
        return mData;
    }

    public T getActivity(){
        return mActivity;
    }
}
