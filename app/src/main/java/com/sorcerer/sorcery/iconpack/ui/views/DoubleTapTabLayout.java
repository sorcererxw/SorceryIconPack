package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/10
 */
public class DoubleTapTabLayout extends TabLayout {
    public DoubleTapTabLayout(Context context) {
        super(context);
        init();
    }

    public DoubleTapTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DoubleTapTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private boolean mTaped = false;

    public void setOnTabDoubleTapListener(
            OnTabDoubleTapListener onTabDoubleTapListener) {
        mOnTabDoubleTapListener = onTabDoubleTapListener;
    }

    public interface OnTabDoubleTapListener {
        void onDoubleTap();
    }

    private OnTabDoubleTapListener mOnTabDoubleTapListener;

    private void init() {
        addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                mTaped = false;
            }

            @Override
            public void onTabUnselected(Tab tab) {
                mTaped = false;
            }

            @Override
            public void onTabReselected(Tab tab) {
                if (mTaped) {
                    if (mOnTabDoubleTapListener != null) {
                        mOnTabDoubleTapListener.onDoubleTap();
                    }
                    mTaped = false;
                } else {
                    mTaped = true;
                    postDelayed(() -> mTaped = false, 300);
                }
            }
        });
    }
}
