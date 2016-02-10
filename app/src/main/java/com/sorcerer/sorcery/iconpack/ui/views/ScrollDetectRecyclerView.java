package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Sorcerer on 2016/2/8 0008.
 */
public class ScrollDetectRecyclerView extends RecyclerView {
    private int mDownY;

    private ScrollCallback mScrollCallback;

    public interface ScrollCallback {
        void onScrollUp();

        void onScrollDown();
    }

    public void setScrollCallback(ScrollCallback scrollCallback) {
        mScrollCallback = scrollCallback;
    }

    public ScrollDetectRecyclerView(Context context) {
        super(context);
    }

    public ScrollDetectRecyclerView(Context context,
                                    @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollDetectRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int y = (int)e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if(y-mDownY>5 && mScrollCallback!=null){
                    mScrollCallback.onScrollDown();
                }else if(mDownY-y>5 && mScrollCallback!=null){
                    mScrollCallback.onScrollUp();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(e);
    }
}
