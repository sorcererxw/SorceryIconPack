package com.sorcerer.sorcery.iconpack.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by pqylj on 2016/5/16.
 */
public class ResourceHelper {
    public static String getString(Context context, int resId){
        return context.getString(resId);
    }

    public static int getColor(Context context,int resId){
        return ContextCompat.getColor(context,resId);
    }

    public static Drawable getDrawable(Context context,int resId){
        return ContextCompat.getDrawable(context,resId);
    }
}
