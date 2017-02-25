package com.sorcerer.sorcery.iconpack.apply.appliers.xposed;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;

import com.annimon.stream.Stream;
import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

import static android.content.Context.MODE_WORLD_READABLE;
import static com.sorcerer.sorcery.iconpack.apply.appliers.xposed.Constants.REPLACEMENT_DISPOSE_PREFERENCE_KEY;
import static com.sorcerer.sorcery.iconpack.apply.appliers.xposed.Constants.XPOSED_PREFERENCE;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/9
 */

@SuppressWarnings("deprecation")
public class XposedInstaller {
    @SuppressLint({"CommitPrefEdits", "WorldReadableFiles"})
    public static Observable<List<IconReplacement>> install(Context context) {
        PackageManager pm = context.getPackageManager();
        return Observable.just(1)
                .map(o -> {
                    Stream.of(context.getFilesDir().listFiles())
                            .filter(value -> value.getName()
                                    .matches("^([a-zA-Z]+[.][a-zA-Z]+)[.]*.*_.\\d*$"))
                            .forEach(file -> file.delete());
                    return o;
                })
                .flatMap(new Function<Integer, ObservableSource<IconReplacement>>() {
                    @Override
                    public ObservableSource<IconReplacement> apply(Integer o)
                            throws Exception {
                        return Observable.fromIterable(getIconReplacementList(context));
                    }
                })
                .map(iconReplacement -> {
                    try {
                        ActivityInfo activityInfo = pm.getActivityInfo(new ComponentName(
                                        iconReplacement.getPackageName(),
                                        iconReplacement.getComponent()),
                                PackageManager.GET_META_DATA);
                        if (activityInfo != null) {
                            iconReplacement.setOriginRes(activityInfo.getIconResource());
                            return iconReplacement;
                        }
                    } catch (Exception ignore) {
                    }
                    return IconReplacement.EMPTY();
                })
                .filter(item -> !item.isEmpty())
                .map(item -> {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(
                            context.getResources(),
                            BitmapFactory.decodeResource(
                                    context.getResources(),
                                    item.getReplacementRes())
                    );
                    try {
                        cacheDrawable(context,
                                item.getPackageName(),
                                item.getOriginRes(),
                                bitmapDrawable);
                        return item;
                    } catch (Exception e) {
                        Timber.d(e);
                        return null;
                    }
                })
                .toList().toObservable()
                .map(iconReplacements -> {
                    context.getSharedPreferences(XPOSED_PREFERENCE, MODE_WORLD_READABLE).edit()
                            .putString(REPLACEMENT_DISPOSE_PREFERENCE_KEY,
                                    new Gson().toJson(iconReplacements))
                            .commit();
                    return iconReplacements;
                });
    }

    private static List<IconReplacement> getIconReplacementList(Context context) {
        List<IconReplacement> replacementList = new ArrayList<>();
        Resources resources = context.getResources();
        XmlResourceParser parser = resources.getXml(resources
                .getIdentifier("appfilter", "xml", context.getPackageName()));
        int eventType;
        try {
            eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            String componentInfo = parser.getAttributeValue(null, "component");
                            if (componentInfo.startsWith(":")) {
                                break;
                            }
                            componentInfo =
                                    componentInfo.replace("ComponentInfo{", "").replace("}", "");
                            String[] split = componentInfo.split("/");
                            String component = split[1];
                            String packageName = split[0];
                            String drawable = parser.getAttributeValue(null, "drawable");
                            int resId = resources.getIdentifier(drawable, "drawable",
                                    context.getPackageName());
                            if (resId == 0) {
                                break;
                            }
                            replacementList.add(new IconReplacement(packageName, component, resId,
                                    drawable));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return replacementList;
    }

    @SuppressLint({"SetWorldReadable", "SetWorldWritable"})
    @SuppressWarnings({"ResultOfMethodCallIgnored", "deprecation"})
    private static void cacheDrawable(Context context, String packageName, int resId,
                                      BitmapDrawable drawable) throws IOException {
        File f = new File(context.getFilesDir(), packageName + "_" + resId);
        if (f.exists()) {
            f.delete();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        drawable.getBitmap().compress(Bitmap.CompressFormat.PNG, 0, bos);
        @SuppressLint("WorldReadableFiles")
        FileOutputStream stream =
                context.openFileOutput(packageName + "_" + resId, MODE_WORLD_READABLE);
        stream.write(bos.toByteArray());
        stream.close();
    }

    public static void onPackageInstall(Context context, String packageName) {
        if (Build.VERSION.SDK_INT >= 24) {
            return;
        }
        if (!context.getSharedPreferences(Constants.XPOSED_PREFERENCE, MODE_WORLD_READABLE)
                .getBoolean(Constants.ACTIVE_PREFERENCE_KEY, false)) {
            return;
        }
        install(context).subscribe(iconReplacements -> {

        }, Timber::d);
    }

    public static void onPackageUninstall(Context context, String packageName) {
        if (Build.VERSION.SDK_INT >= 24) {
            return;
        }
        if (!context.getSharedPreferences(Constants.XPOSED_PREFERENCE, MODE_WORLD_READABLE)
                .getBoolean(Constants.ACTIVE_PREFERENCE_KEY, false)) {
            return;
        }
        Stream.of(context.getFilesDir().listFiles())
                .filter(file -> file.getName().startsWith(packageName))
                .forEach(file -> file.delete());

        install(context).subscribe(iconReplacements -> {

        }, Timber::d);
    }

    public static Observable<List<IconReplacement>> refresh(Context context) {
        return install(context);
    }
}
