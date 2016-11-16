package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

public class ExposedSearchToolbar extends Toolbar {

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

    private TextView mTitleTextView;

    private void initTitle(Context context) {
        super.setTitle("");
        mTitleTextView = new TextView(context);
        mTitleTextView.setTextColor(ResourceUtil.getColor(context, R.color.grey_500));
        mTitleTextView.setTextSize(20);
        mTitleTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mTitleTextView.setTypeface(Typeface.createFromAsset(context.getAssets(), "RockwellStd.otf"));
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
