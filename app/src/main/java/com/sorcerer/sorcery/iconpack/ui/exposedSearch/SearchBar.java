package com.sorcerer.sorcery.iconpack.ui.exposedSearch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/5
 */

public class SearchBar extends Toolbar {
    public SearchBar(Context context) {
        super(context);
        init(context);
    }

    public SearchBar(Context context,
                     @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchBar(Context context,
                     @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private BackKeyEditText mEditText;

    private void init(Context context) {

        mEditText = new BackKeyEditText(context);
        mEditText.setBackground(null);
        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEditText.setMaxLines(1);
        mEditText.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        mEditText.setGravity(Gravity.CENTER_VERTICAL);
        mEditText.setHint("Search icon");
        addView(mEditText);

        setBackgroundColor(Color.WHITE);
        addTintedUpNavigation();
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                    mEditText.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    private void addTintedUpNavigation() {
        Drawable drawable =
                ResourceUtil.getDrawable(getContext(), R.drawable.ic_arrow_back_grey_600_24dp);
        setNavigationIcon(drawable);
    }

    public void setOnBackKeyPressedListener(OnBackKeyPressListener onBackKeyPressedListener) {
        mEditText.setOnBackKeyPressListener(onBackKeyPressedListener);
    }

    public void addTextWatcher(TextWatcher textWatcher) {
        mEditText.addTextChangedListener(textWatcher);
    }

    public void setInputType(int typeTextFlag) {
        mEditText.setInputType(typeTextFlag);
    }

    public void setText(String text) {
        mEditText.setText(text);
    }

    public void clearText() {
        mEditText.setText(null);
    }

    public boolean hasText() {
        return mEditText.length() > 0;
    }

    public void setHint(String hint) {
        mEditText.setHint(hint);
    }

}
