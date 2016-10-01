package com.sorcerer.sorcery.iconpack.util;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.AbsListView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/31 0031
 */
public class ToolbarOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    private AbsListView mScrollTarget;

    public interface DoubleTapListener {
        void onDoubleTap();
    }

    private DoubleTapListener mDoubleTapListener;

    public ToolbarOnGestureListener() {
        super();
    }

    public ToolbarOnGestureListener(AbsListView scrollTarget) {
        super();
        mScrollTarget = scrollTarget;
    }

    public ToolbarOnGestureListener(DoubleTapListener doubleTapListener) {
        super();
        mDoubleTapListener = doubleTapListener;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (mDoubleTapListener != null) {
            mDoubleTapListener.onDoubleTap();
        } else if (mScrollTarget != null) {
            mScrollTarget.smoothScrollToPosition(0);
        }
        return super.onDoubleTap(e);
    }
}
