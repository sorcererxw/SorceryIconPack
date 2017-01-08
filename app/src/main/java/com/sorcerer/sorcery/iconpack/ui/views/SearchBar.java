package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.sorcerer.sorcery.iconpack.utils.SimpleTextWatcher;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/5
 */

public class SearchBar extends Toolbar {
    public SearchBar(Context context) {
        super(context);
        init();
    }

    public SearchBar(Context context,
                     @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchBar(Context context,
                     @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @BindView(R.id.editText)
    EditText mEditText;

    @BindView(R.id.imageView)
    ImageView mActionIcon;

    @BindView(R.id.indicatorView)
    AVLoadingIndicatorView mIndicatorView;

    private void init() {

        View view = View.inflate(getContext(), R.layout.layout_search_bar, null);

        addView(view);
        ButterKnife.bind(this, this);

        setBackgroundColor(Color.WHITE);
        addTintedUpNavigation();
        mEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                mEditText.requestFocus();
                return true;
            }
            return false;
        });

        mEditText.addTextChangedListener(new SimpleTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    mActionIcon.setVisibility(VISIBLE);
                } else {
                    mActionIcon.setVisibility(GONE);
                }
                if (mSearchListener != null) {
                    mSearchListener.search(s == null ? "" : s.toString());
                }
            }
        });

        mActionIcon.setVisibility(GONE);

        mActionIcon.setImageDrawable(
                new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_clear)
                        .color(Color.BLACK)
                        .alpha(52)
                        .sizeDp(14)
        );
        mActionIcon.setOnClickListener(v -> mEditText.setText(""));
    }

    public void setSearching(final boolean Searching) {
        post(() -> {
            if (Searching) {
                mIndicatorView.setVisibility(VISIBLE);
                mActionIcon.setVisibility(GONE);
            } else {
                mIndicatorView.setVisibility(GONE);
                if (mEditText.getText() != null && mEditText.getText().length() > 0) {
                    mActionIcon.setVisibility(VISIBLE);
                } else {
                    mActionIcon.setVisibility(GONE);
                }
            }
        });
    }

    public void requestEditTextFocus() {
        InputMethodManager imm =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void addTintedUpNavigation() {
        Drawable drawable =
                new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_arrow_back)
                        .sizeDp(24)
                        .paddingDp(4)
                        .color(ResourceUtil.getColor(getContext(), R.color.palette_grey_600));
        setNavigationIcon(drawable);
    }

    public interface SearchListener {
        void search(String text);
    }

    private SearchListener mSearchListener;

    public void setSearchListener(SearchListener searchListener) {
        mSearchListener = searchListener;
    }

    public String getText() {
        return mEditText.getText().toString();
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
