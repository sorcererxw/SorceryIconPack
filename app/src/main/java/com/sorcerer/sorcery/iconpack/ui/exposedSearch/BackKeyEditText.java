package com.sorcerer.sorcery.iconpack.ui.exposedSearch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/5
 */

public class BackKeyEditText extends EditText {
    public BackKeyEditText(Context context) {
        super(context);
    }

    public BackKeyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackKeyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private OnBackKeyPressListener mOnBackKeyPressListener;

    public void setOnBackKeyPressListener(OnBackKeyPressListener onBackKeyPressListener) {
        mOnBackKeyPressListener = onBackKeyPressListener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK
                && mOnBackKeyPressListener != null
                && mOnBackKeyPressListener.onBackKeyPressed())
                || super.onKeyPreIme(keyCode, event);
    }
}
