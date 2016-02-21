package com.sorcerer.sorcery.iconpack.xposed;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.TypedValue;

import com.sorcerer.sorcery.iconpack.xposed.theme.IconMaskItem;
import com.sorcerer.sorcery.iconpack.xposed.theme.IconShader;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.Command;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class Utils {

    private static String PACKAGE_NAME = "com.sorcerer.sorcery.iconpack";
    private static String TAG = "SIP/Utils";

    public static void copyFdToFile(FileDescriptor src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    public static void copyAsset(Context context, String filename, String dest) {
        IOException e;
        AssetManager assetManager = context.getAssets();
        try {
            String[] files = assetManager.list("");
        } catch (IOException e2) {
            Log.e("tag", "Failed to get asset file list.", e2);
        }
        try {
            InputStream in = assetManager.open(filename);
            OutputStream out = new FileOutputStream(dest);
            try {
                copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch (IOException e3) {
                e3.printStackTrace();
//                e2 = e3;
                OutputStream outputStream = out;
//                Log.e("tag", "Failed to copy asset file: " + filename, e2);
            }
        } catch (IOException e4) {
            e4.printStackTrace();
//            e2 = e4;
//            Log.e("tag", "Failed to copy asset file: " + filename, e2);
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int read = in.read(buffer);
            if (read != -1) {
                out.write(buffer, 0, read);
            } else {
                return;
            }
        }
    }

    public static String getLogcat() {
        String output = "";
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(Runtime.getRuntime()
                            .exec("logcat -v -d time IconThemer *:S").getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                output = output + line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static File flushLogcatToFile(String filename) {
        try {
            File f = new File(filename);
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new FileOutputStream(f).write(getLogcat().getBytes());
            f.setReadable(true, false);
            f.setWritable(true, false);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkIfModuleIsActivated(String modulePackageName) {
        try {
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(Runtime.getRuntime()
                    .exec("cat /data/xposed/modules.whitelist").getInputStream()));
            String s;
            do {
                s = stdInput.readLine();
                if (s == null) {
                    return false;
                }
            } while (!s.contains(modulePackageName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap themeIcon(Resources srcRes, Resources themeRes, int displayDpi,
                                   int iconRes, int backRes, int maskRes, int upRes, float scale) {
        Bitmap sourceImage = getBitmapForDensity(srcRes, displayDpi, iconRes);
        Bitmap iconBack = null;
        if (backRes != 0) {
            iconBack = getBitmapForDensity(themeRes, displayDpi, backRes);
        }
        Bitmap alphaMask = null;
        if (maskRes != 0) {
            alphaMask = getBitmapForDensity(themeRes, displayDpi, maskRes);
        }
        Bitmap iconUpOn = null;
        if (upRes != 0) {
            iconUpOn = getBitmapForDensity(themeRes, displayDpi, upRes);
        }
        int origWidth = (int) (0.3d * ((double) displayDpi));
        int origHeight = (int) (0.3d * ((double) displayDpi));
        if (scale < 0.4f) {
            scale = 0.4f;
        } else if (scale > 1.5f) {
            scale = 1.5f;
        }
        Bitmap bitmapOut = Bitmap.createBitmap(origWidth, origHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapOut);
        Paint paint = new Paint(1);
        paint.setFilterBitmap(true);
        Paint xferPaint = new Paint(1);
        xferPaint.setFilterBitmap(true);
        xferPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        if (iconBack != null) {
            canvas.drawBitmap(iconBack,
                    new Rect(0, 0, iconBack.getWidth(), iconBack.getHeight()),
                    new Rect(0, 0, bitmapOut.getWidth(), bitmapOut.getHeight()),
                    paint);
        }
        float scaledLeft = (((float) origWidth) - (scale * ((float) origWidth))) / 2.0f;
        float scaledTop = (((float) origHeight) - (scale * ((float) origHeight))) / 2.0f;
        canvas.drawBitmap(sourceImage,
                new Rect(0, 0, sourceImage.getWidth(), sourceImage.getHeight()),
                new RectF(scaledLeft,
                        scaledTop,
                        ((float) origWidth) - scaledLeft,
                        ((float) origHeight) - scaledTop),
                paint);
        if (alphaMask != null) {
            canvas.drawBitmap(alphaMask,
                    new Rect(0, 0, alphaMask.getWidth(), alphaMask.getHeight()),
                    new Rect(0, 0, bitmapOut.getWidth(), bitmapOut.getHeight()),
                    xferPaint);
        }
        if (iconUpOn != null) {
            canvas.drawBitmap(iconUpOn,
                    new Rect(0, 0, iconUpOn.getWidth(), iconUpOn.getHeight()),
                    new Rect(0, 0, bitmapOut.getWidth(), bitmapOut.getHeight()),
                    paint);
        }
        if (iconBack != null) {
            iconBack.recycle();
        }
        if (alphaMask != null) {
            alphaMask.recycle();
        }
        if (iconUpOn != null) {
            iconUpOn.recycle();
        }
        sourceImage.recycle();
        return bitmapOut;
    }

    public static boolean cacheDrawable(String packageName, int resId, BitmapDrawable drawable) {
        try {
            File f = new File("/data/data/" + PACKAGE_NAME + "/cache/icons/",
                    packageName + "_" + resId);
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            drawable.getBitmap().compress(CompressFormat.PNG, 0, bos);
            new FileOutputStream(f).write(bos.toByteArray());
            f.setReadable(true, false);
            f.setWritable(true, false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getCacheFilePath(String packageName, int resId) {
        return "/data/data/" + PACKAGE_NAME + "/cache/icons/" + packageName + "_" + resId;
    }

    public static BitmapDrawable getCachedIcon(Resources res, String packageName, int resId) {
        return getCachedIcon(res, packageName, resId, null);
    }

    public static BitmapDrawable getCachedIcon(Resources res, String packageName, int resId,
                                               Options opts) {
        int displayDpi = res.getDisplayMetrics().densityDpi;
        Bitmap bitmap = BitmapFactory.decodeFile(getCacheFilePath(packageName, resId), opts);
        if (bitmap == null) {
            return null;
        }
        bitmap.setDensity(displayDpi);
        return new BitmapDrawable(res, bitmap);
    }

    public static Bitmap getBitmapForDensity(Resources res, int displayDpi, int resId) {
        try {
            TypedValue value = new TypedValue();
            res.getValueForDensity(resId, displayDpi, value, true);
            AssetFileDescriptor fd =
                    res.getAssets().openNonAssetFd(value.assetCookie, value.string.toString());
            Options opt = new Options();
            opt.inTargetDensity = displayDpi;
            Bitmap bitmap = BitmapFactory
                    .decodeResourceStream(res, value, fd.createInputStream(), null, opt);
            bitmap.setDensity(res.getDisplayMetrics().densityDpi);
            fd.close();
            return bitmap;
        } catch (Exception e) {
            return BitmapFactory.decodeResource(res, resId);
        }
    }
}
