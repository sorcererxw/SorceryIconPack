package com.sorcerer.sorcery.iconpack.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by pqylj on 2016/5/16.
 */
public class ResourceUtil {
    public static String getString(Context context, int resId) {
        return context.getString(resId);
    }

    public static int getColor(Context context, int resId) {
        return ContextCompat.getColor(context, resId);
    }

    public static Drawable getDrawable(Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
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
            e.printStackTrace();
            return new String[]{"**load fail**"};
        }
    }

    public static String[] getStringArray(Context context, int id) {
        return context.getResources().getStringArray(id);
    }

    public static int getResourceIdFromString(Context context, String resName, String resFold) {
        return context.getResources().getIdentifier(resName, resFold, context.getPackageName());
    }
}
