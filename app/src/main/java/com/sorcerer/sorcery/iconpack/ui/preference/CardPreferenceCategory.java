package com.sorcerer.sorcery.iconpack.ui.preference;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/13
 */

public class CardPreferenceCategory extends PreferenceCategory {
    public CardPreferenceCategory(Context context, AttributeSet attrs,
                                  int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CardPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CardPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardPreferenceCategory(Context context) {
        super(context);
    }
}
