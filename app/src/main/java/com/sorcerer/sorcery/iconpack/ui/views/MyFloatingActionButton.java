package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

/**
 * Created by Sorcerer on 2016/2/9 0009.
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

    public void setShow(boolean show) {
        mShow = show;
    }

    /**
     * Shows the button.
     * <p>This method will animate the button show if the view has already been laid out.</p>
     */
    @Override
    public void show() {
        if (mShow) {
            super.show();
        }
    }

    /**
     * Hides the button.
     * <p>This method will animate the button hide if the view has already been laid out.</p>
     */
    @Override
    public void hide() {
        super.hide();
    }
}
