package com.sorcerer.sorcery.iconpack.ui.preferences;

import android.content.Context;
import android.support.v7.preference.PreferenceCategory;
import android.util.AttributeSet;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/26
 */

public class SorceryPreferenceCategroy extends PreferenceCategory {
    public SorceryPreferenceCategroy(Context context, AttributeSet attrs,
                                     int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SorceryPreferenceCategroy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SorceryPreferenceCategroy(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SorceryPreferenceCategroy(Context context) {
        super(context);
    }
}
