package com.sorcerer.sorcery.iconpack.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/5/2
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

    public static Bitmap getMarginBitmap(Bitmap origin, int marginTop, int marginRight,
                                         int marginBottom, int marginLeft) {
        Bitmap bmOverlay = Bitmap.createBitmap(
                origin.getWidth() + marginLeft + marginRight,
                origin.getHeight() + marginTop + marginBottom,
                origin.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(origin, marginLeft, marginTop, null);
        return bmOverlay;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static byte[] flattenBitmap(Bitmap bitmap) {
        // Try go guesstimate how much space the icon will take when serialized
        // to avoid unnecessary allocations/copies during the write.
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            Timber.e("Could not write icon");
            Timber.e(e);
            return null;
        }
    }

    public static Bitmap overlay(Bitmap bottom, Bitmap overlay) {
        Bitmap bmOverlay =
                Bitmap.createBitmap(bottom.getWidth(), bottom.getHeight(), bottom.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bottom, new Matrix(), null);
        canvas.drawBitmap(overlay, new Matrix(), null);
        return bmOverlay;
    }

}
