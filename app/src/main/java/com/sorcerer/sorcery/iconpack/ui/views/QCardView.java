package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by Sorcerer on 2016/2/4 0004.
 */
public class QCardView extends CardView {

    private float orginX;
    private float orginY;
    private float sumX;
    private float sumY;
    private int lastX;
    private int lastY;
    private Scroller mScroller;
    private TouchCallBack mTouchCallBack;

    public void setTouchCallBack(TouchCallBack touchCallBack) {
        mTouchCallBack = touchCallBack;
    }

    public QCardView(Context context) {
        this(context, null);
    }

    public QCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    public interface TouchCallBack {
        void onDown();

        void onUp();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        View view = ((View) getParent());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTouchCallBack != null) {
                    mTouchCallBack.onDown();
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                // 通过layout方法来重新设置view的坐标
                ((View) getParent()).scrollBy(-offsetX, -offsetY);
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchCallBack != null) {
                    mTouchCallBack.onUp();
                }
                mScroller.startScroll(
                        view.getScrollX(),
                        view.getScrollY(),
                        -view.getScrollX(),
                        -view.getScrollY());
                invalidate();
                break;
        }
        return true;
    }

}
