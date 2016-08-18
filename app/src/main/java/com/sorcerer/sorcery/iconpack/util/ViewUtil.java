package com.sorcerer.sorcery.iconpack.util;

import android.view.View;

/**
 * Created by Sorcerer on 2016/4/27.
 */
public class ViewUtil {
    public static boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        return (x > viewX && x < (viewX + view.getWidth()))
                && (y > viewY && y < (viewY + view.getHeight()));
    }

    public static boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        if (differenceX > 5/* =5 */ || differenceY > 5) {
            return false;
        }
        return true;
    }

}
