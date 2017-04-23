package com.sorcerer.sorcery.iconpack.utils;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/4/12
 */

public class TextWeightUtil {
    public static void medium(TextView textView) {
        try {
            Typeface font = Typeface.create("sans-serif-medium", Typeface.NORMAL);
            if (font != null) {
                textView.setTypeface(font);
            }
        } catch (Exception ignore) {}
    }

    public static void regular(TextView textView) {
        try {
            Typeface font = Typeface.create("sans-serif-regular", Typeface.NORMAL);
            if (font != null) {
                textView.setTypeface(font);
            }
        } catch (Exception ignore) {}
    }

    public static void light(TextView textView) {
        try {
            Typeface font = Typeface.create("sans-serif-light", Typeface.NORMAL);
            if (font != null) {
                textView.setTypeface(font);
            }
        } catch (Exception ignore) {}
    }
}
