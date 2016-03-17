package com.sorcerer.sorcery.iconpack.util;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sorcerer.sorcery.iconpack.R;

/**
 * Created by Sorcerer on 2016/3/17 0017.
 */
public class TintUtils {
    private static final int[] TINT_ATTRS = {
            R.attr.backgroundTint,      //in v7
            R.attr.backgroundTintMode,  //in v7
    };

    public static void supportTint(View view, AttributeSet attrs) {
        TypedArray a = view.getContext().obtainStyledAttributes(attrs, TINT_ATTRS);
        if (a.hasValue(0)){
            //set backgroundTint
            ColorStateList colorStateList = a.getColorStateList(0);
            ViewCompat.setBackgroundTintList(view, colorStateList);
        }
        if (a.hasValue(1)){
            //set backgroundTintMode
            int mode = a.getInt(1, -1);
            ViewCompat.setBackgroundTintMode(view, parseTintMode(mode, null));
        }
        a.recycle();
    }

    /**
     * Parses a {@link android.graphics.PorterDuff.Mode} from a tintMode
     * attribute's enum value.
     *
     * @hide
     */
    public static PorterDuff.Mode parseTintMode(int value, PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3:
                return PorterDuff.Mode.SRC_OVER;
            case 5:
                return PorterDuff.Mode.SRC_IN;
            case 9:
                return PorterDuff.Mode.SRC_ATOP;
            case 14:
                return PorterDuff.Mode.MULTIPLY;
            case 15:
                return PorterDuff.Mode.SCREEN;
            case 16:
                return PorterDuff.Mode.ADD;
            default:
                return defaultMode;
        }
    }
}
