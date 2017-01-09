package com.sorcerer.sorcery.iconpack.ui.others;

import android.view.MotionEvent;
import android.view.View;

import com.sorcerer.sorcery.iconpack.utils.SimpleCallback;

/**
 * @description: 连续多次点击事件的监听器
 * @author: Sorcerer
 * @date: 2016/11/12
 */

public class OnMultiTouchListener implements View.OnTouchListener {

    private int mTime;

    private SimpleCallback mCallback;

    private int mClickTime;

    private boolean mEndAfterFinish = true;

    private boolean mFinished = false;

    private long mGap = 500;

    private void setGap(long gap) {
        mGap = gap;
    }

    private long mLastClickTimeMillis;

    public OnMultiTouchListener(int time, SimpleCallback callback) {
        mTime = time;
        mClickTime = 0;
        mCallback = callback;
        mLastClickTimeMillis = 0;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mEndAfterFinish) {
            if (mFinished) {
                return false;
            }
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            long now = System.currentTimeMillis();
            if (now - mLastClickTimeMillis <= mGap) {
                mClickTime += 1;
            } else {
                mClickTime = 1;
            }
            mLastClickTimeMillis = now;
            if (mClickTime >= mTime) {
                if (mCallback != null) {
                    mCallback.call();
                    mFinished = true;
                }
            }
        }
        return true;
    }
}
