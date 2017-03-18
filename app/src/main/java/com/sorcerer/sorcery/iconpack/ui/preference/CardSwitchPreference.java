package com.sorcerer.sorcery.iconpack.ui.preference;

import android.content.Context;
import android.support.v14.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/14
 */

public class CardSwitchPreference extends SwitchPreference {
    public CardSwitchPreference(Context context, AttributeSet attrs,
                                int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CardSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CardSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardSwitchPreference(Context context) {
        super(context);
    }
}
