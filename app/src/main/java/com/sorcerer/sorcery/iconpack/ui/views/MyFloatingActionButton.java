package com.sorcerer.sorcery.iconpack.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/2/9 0009
 */
public class MyFloatingActionButton extends FloatingActionButton {

    private boolean mShow;

    public MyFloatingActionButton(Context context) {
        super(context);
        init();
    }

    public MyFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundTintList(ColorStateList.valueOf(
                ResourceUtil.getAttrColor(getContext(), android.R.attr.colorAccent)));
    }

    public boolean isShow() {
        return mShow;
    }

    @Override
    public void show() {
        setClickable(true);
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
        mShow = true;
    }

    @Override
    public void hide() {
        setClickable(false);
        animate().scaleY(0).scaleX(0).setDuration(200).start();
        mShow = false;
    }
}
