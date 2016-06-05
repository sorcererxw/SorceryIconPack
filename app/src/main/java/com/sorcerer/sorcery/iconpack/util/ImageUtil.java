package com.sorcerer.sorcery.iconpack.util;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

/**
 * Created by Sorcerer on 2016/5/2.
 */
public class ImageUtil {
    public static void grayScale(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    public static void resetScale(ImageView imageView) {
        imageView.setColorFilter(null);
    }
}
