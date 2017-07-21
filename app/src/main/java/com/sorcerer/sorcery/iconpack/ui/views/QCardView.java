package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewPropertyAnimator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

import com.mikepenz.materialize.util.UIUtils;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/2/4 0004
 */
public class QCardView extends CardView {

    private int lastX;
    private int lastY;
    private Scroller mScroller;
    private TouchCallBack mTouchCallBack;

    private boolean mTouchable = false;

    public QCardView(Context context) {
        this(context, null);
    }

    public QCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        setTranslationZ(UIUtils.convertDpToPixel(2, context));

    }

    public void setTouchCallBack(TouchCallBack touchCallBack) {
        mTouchCallBack = touchCallBack;
    }

    public boolean isTouchable() {
        return mTouchable;
    }

    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
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

//                if (Build.VERSION.SDK_INT >= 21) {
//                    animate().translationZ(DisplayUtil.dip2px(getContext(), 8))
//                            .setDuration(200)
//                            .setInterpolator(new OvershootInterpolator())
//                            .start();
//                }

                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;

                setTranslationY(getTranslationY() + offsetY);
                setTranslationX(getTranslationX() + offsetX);
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchCallBack != null) {
                    mTouchCallBack.onUp();
                }

                ViewPropertyAnimator animatorUp = animate().translationX(0)
                        .translationY(0);
//                if (Build.VERSION.SDK_INT >= 21) {
//                    animatorUp.translationZ(DisplayUtil.dip2px(getContext(), 2));
//                }
                animatorUp.setDuration(500)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
                break;
        }
        return true;
    }

    public interface TouchCallBack {
        void onDown();

        void onUp();
    }

}
