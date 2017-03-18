package com.sorcerer.sorcery.iconpack.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/5/16
 */
public class ResourceUtil {

    public static String getString(Context context, @StringRes int resId) {
        return context.getString(resId);
    }

    public static String getStringFromResString(Context context, String resString) {
        try {
            int id = context.getResources()
                    .getIdentifier(resString, "string", context.getPackageName());
            return context.getResources().getString(id);
        } catch (Exception e) {
            Timber.e(e);
            return "load fail";
        }
    }

    public static int getColor(Context context, @ColorRes int resId) {
        return ContextCompat.getColor(context, resId);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int resId) {
        return ContextCompat.getDrawable(context, resId);
    }

    @DrawableRes
    public static int getDrawableRes(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public static int getResourceId(Context context, String name) {
        return context.getResources()
                .getIdentifier(name, "drawable", context.getPackageName());
    }

    /**
     * @param context context
     * @param resId   resource id
     * @param alpha   0~255
     * @return final drawable
     */
    public static Drawable getDrawableWithAlpha(Context context, int resId, int alpha) {
        Drawable drawable = getDrawable(context, resId);
        drawable.setAlpha(alpha);
        return drawable;
    }

    public static Drawable getDrawableWithAlpha(Context context, int resId, float alpha) {
        return getDrawableWithAlpha(context, resId, alpha * 255);
    }

    /**
     * @param context to get resource
     * @param resName if need "R.array.example", resName is "example"
     * @return string array
     */
    public static String[] getStringArray(Context context, String resName) {
        try {
            int id = getResourceIdFromString(context, resName, "array");
            return context.getResources().getStringArray(id);
        } catch (Exception e) {
            Timber.e(e);
            return new String[]{"**load fail**"};
        }
    }

    public static String[] getStringArray(Context context, int id) {
        return context.getResources().getStringArray(id);
    }

    public static int getResourceIdFromString(Context context, String resName, String resFold) {
        return context.getResources().getIdentifier(resName, resFold, context.getPackageName());
    }

    public static Drawable getAttrDrawable(Context context, int id) {
        int[] attrs = new int[]{id};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0);
        ta.recycle();
        return drawableFromTheme;
    }

    public static int getAttrColor(Context context, int attrId) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attrId, typedValue, true);
        return typedValue.data;

    }
}
