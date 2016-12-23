package com.sorcerer.sorcery.iconpack.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;

import java.util.Random;

import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/5/16
 */
public class ResourceUtil {
    private static Integer[] mColors = new Integer[]{
            R.color.red_50,
            R.color.red_100,
            R.color.red_200,
            R.color.red_300,
            R.color.red_400,
            R.color.red_500,
            R.color.red_600,
            R.color.red_700,
            R.color.red_800,
            R.color.red_900,

            R.color.deep_purple_50,
            R.color.deep_purple_100,
            R.color.deep_purple_200,
            R.color.deep_purple_300,
            R.color.deep_purple_400,
            R.color.deep_purple_500,
            R.color.deep_purple_600,
            R.color.deep_purple_700,
            R.color.deep_purple_800,
            R.color.deep_purple_900,

            R.color.light_blue_50,
            R.color.light_blue_100,
            R.color.light_blue_200,
            R.color.light_blue_300,
            R.color.light_blue_400,
            R.color.light_blue_500,
            R.color.light_blue_600,
            R.color.light_blue_700,
            R.color.light_blue_800,
            R.color.light_blue_900,

            R.color.green_50,
            R.color.green_100,
            R.color.green_200,
            R.color.green_300,
            R.color.green_400,
            R.color.green_500,
            R.color.green_600,
            R.color.green_700,
            R.color.green_800,
            R.color.green_900,

            R.color.yellow_50,
            R.color.yellow_100,
            R.color.yellow_200,
            R.color.yellow_300,
            R.color.yellow_400,
            R.color.yellow_500,
            R.color.yellow_600,
            R.color.yellow_700,
            R.color.yellow_800,
            R.color.yellow_900,

            R.color.deep_orange_50,
            R.color.deep_orange_100,
            R.color.deep_orange_200,
            R.color.deep_orange_300,
            R.color.deep_orange_400,
            R.color.deep_orange_500,
            R.color.deep_orange_600,
            R.color.deep_orange_700,
            R.color.deep_orange_800,
            R.color.deep_orange_900,

            R.color.blue_grey_50,
            R.color.blue_grey_100,
            R.color.blue_grey_200,
            R.color.blue_grey_300,
            R.color.blue_grey_400,
            R.color.blue_grey_500,
            R.color.blue_grey_600,
            R.color.blue_grey_700,
            R.color.blue_grey_800,
            R.color.blue_grey_900,

            R.color.pink_50,
            R.color.pink_100,
            R.color.pink_200,
            R.color.pink_300,
            R.color.pink_400,
            R.color.pink_500,
            R.color.pink_600,
            R.color.pink_700,
            R.color.pink_800,
            R.color.pink_900,

            R.color.indigo_50,
            R.color.indigo_100,
            R.color.indigo_200,
            R.color.indigo_300,
            R.color.indigo_400,
            R.color.indigo_500,
            R.color.indigo_600,
            R.color.indigo_700,
            R.color.indigo_800,
            R.color.indigo_900,

            R.color.cyan_50,
            R.color.cyan_100,
            R.color.cyan_200,
            R.color.cyan_300,
            R.color.cyan_400,
            R.color.cyan_500,
            R.color.cyan_600,
            R.color.cyan_700,
            R.color.cyan_800,
            R.color.cyan_900,

            R.color.light_green_50,
            R.color.light_green_100,
            R.color.light_green_200,
            R.color.light_green_300,
            R.color.light_green_400,
            R.color.light_green_500,
            R.color.light_green_600,
            R.color.light_green_700,
            R.color.light_green_800,
            R.color.light_green_900,

            R.color.amber_50,
            R.color.amber_100,
            R.color.amber_200,
            R.color.amber_300,
            R.color.amber_400,
            R.color.amber_500,
            R.color.amber_600,
            R.color.amber_700,
            R.color.amber_800,
            R.color.amber_900,

            R.color.brown_50,
            R.color.brown_100,
            R.color.brown_200,
            R.color.brown_300,
            R.color.brown_400,
            R.color.brown_500,
            R.color.brown_600,
            R.color.brown_700,
            R.color.brown_800,
            R.color.brown_900,

            R.color.purple_50,
            R.color.purple_100,
            R.color.purple_200,
            R.color.purple_300,
            R.color.purple_400,
            R.color.purple_500,
            R.color.purple_600,
            R.color.purple_700,
            R.color.purple_800,
            R.color.purple_900,

            R.color.blue_50,
            R.color.blue_100,
            R.color.blue_200,
            R.color.blue_300,
            R.color.blue_400,
            R.color.blue_500,
            R.color.blue_600,
            R.color.blue_700,
            R.color.blue_800,
            R.color.blue_900,

            R.color.teal_50,
            R.color.teal_100,
            R.color.teal_200,
            R.color.teal_300,
            R.color.teal_400,
            R.color.teal_500,
            R.color.teal_600,
            R.color.teal_700,
            R.color.teal_800,
            R.color.teal_900,

            R.color.lime_50,
            R.color.lime_100,
            R.color.lime_200,
            R.color.lime_300,
            R.color.lime_400,
            R.color.lime_500,
            R.color.lime_600,
            R.color.lime_700,
            R.color.lime_800,
            R.color.lime_900,

            R.color.orange_50,
            R.color.orange_100,
            R.color.orange_200,
            R.color.orange_300,
            R.color.orange_400,
            R.color.orange_500,
            R.color.orange_600,
            R.color.orange_700,
            R.color.orange_800,
            R.color.orange_900,

            R.color.grey_50,
            R.color.grey_100,
            R.color.grey_200,
            R.color.grey_300,
            R.color.grey_400,
            R.color.grey_500,
            R.color.grey_600,
            R.color.grey_700,
            R.color.grey_800,
            R.color.grey_900,
    };

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
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            return new String[]{"**load fail**"};
        }
    }

    public static String[] getStringArray(Context context, int id) {
        return context.getResources().getStringArray(id);
    }

    public static int getResourceIdFromString(Context context, String resName, String resFold) {
        return context.getResources().getIdentifier(resName, resFold, context.getPackageName());
    }

    public static int getRandomColor(Context context, int seed, int deep) {

        Random random = new Random(seed);
        int a = Math.abs(random.nextInt()) % 19;
        return ContextCompat.getColor(context, mColors[a * 10 + deep]);

    }
}
