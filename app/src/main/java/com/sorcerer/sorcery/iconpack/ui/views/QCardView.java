package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

/**
 * Created by Sorcerer on 2016/2/4 0004.
 */
public class QCardView extends CardView {

    private float mOrginX;
    private float mOrginY;
    private float sumX;
    private float sumY;
    private int lastX;
    private int lastY;
    private Scroller mScroller;
    private TouchCallBack mTouchCallBack;
    private float mDx;
    private float mDy;

    private boolean mTouchable = false;

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

    public boolean isTouchable() {
        return mTouchable;
    }

    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    public interface TouchCallBack {
        void onDown();

        void onUp();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mTouchable) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTouchCallBack != null) {
                    mTouchCallBack.onDown();
                }
                lastX = x;
                lastY = y;
                mDx = 0;
                mDy = 0;
                mOrginX = getX();
                mOrginY = getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;

                layout((int) getX() + offsetX,
                        (int) getY() + offsetY,
                        (int) getX() + getWidth() + offsetX,
                        (int) getY() + getHeight() + offsetY);

                mDx += offsetX;
                mDy += offsetY;
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchCallBack != null) {
                    mTouchCallBack.onUp();
                }
                layout((int) mOrginX,
                        (int) mOrginY,
                        (int) mOrginX + getWidth(),
                        (int) mOrginY + getHeight());

                invalidate();
                break;
        }
        return true;
    }

}
