package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

public class ExposedSearchToolbar extends Toolbar {

    private TextView mTitleTextView;

    public ExposedSearchToolbar(Context context) {
        super(context);
        init(context);
    }

    public ExposedSearchToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExposedSearchToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.drawable.card_noshadow);

        initTitle(context);
    }

    private void initTitle(Context context) {
        super.setTitle("");
        mTitleTextView = (TextView) View.inflate(context, R.layout.layout_searchbar_logo, null);
        addView(mTitleTextView);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        mTitleTextView.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }
}
