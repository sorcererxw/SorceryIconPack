package com.sorcerer.sorcery.iconpack.ui.preferences;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/11
 */

public class SorcerySwitchPreference extends SwitchPreference {

    public SorcerySwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SorcerySwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SorcerySwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected void onClick() {

    }
}
