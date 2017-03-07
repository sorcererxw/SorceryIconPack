package com.sorcerer.sorcery.iconpack.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.annimon.stream.Stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/5/2
 */
public class ImageUtil {

    // 812 : 1024
    public static Bitmap normalizeIconSize(Bitmap originIcon) {
        if (!isNeedNormalize(originIcon)) {
            return originIcon;
        }

        originIcon = ImageUtil.getResizedBitmap(
                originIcon.copy(Bitmap.Config.ARGB_8888, true),
                812, 812);

        Bitmap result = Bitmap.createBitmap(1024, 1024, originIcon.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(originIcon, 106f, 106f, new Paint());
        return result;
    }

    private static boolean isNeedNormalize(Bitmap originIcon) {
        int height = originIcon.getHeight();
        int width = originIcon.getWidth();

        boolean yNeed = Stream.range(0, height)
                .map(y -> originIcon.getPixel(0, y))
                .filter(value -> Color.alpha(value) != 0)
                .count() >= (height / 2);

        boolean xNeed = Stream.range(0, height)
                .map(x -> originIcon.getPixel(x, 0))
                .filter(value -> Color.alpha(value) != 0)
                .count() >= (width / 2);

        return yNeed && xNeed;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable grayScale(Drawable drawable) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(filter);

        return drawable;
    }

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
        if (bitmap == null) {
            return null;
        }
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
