package com.sorcerer.sorcery.iconpack.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/2/9 0009
 */
public class MyFloatingActionButton extends FloatingActionButton {

    private boolean mShow;

    public MyFloatingActionButton(Context context) {
        super(context);
    }

    public MyFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isShow() {
        return mShow;
    }

    @Override
    public void show() {
        if (!mShow) {
            if (getVisibility() != VISIBLE) {
                setScaleX(0);
                setScaleY(0);
            }
            animate().scaleY(1).scaleX(1).setDuration(200).setListener(
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            if (getVisibility() != VISIBLE) {
                                setScaleX(0);
                                setScaleY(0);
                                setVisibility(VISIBLE);
                            }
                        }
                    }).start();
        }
        mShow = true;
    }

    @Override
    public void hide() {
        if (mShow) {
            animate().scaleY(0).scaleX(0).setDuration(200).start();
        }
        mShow = false;
    }
}
