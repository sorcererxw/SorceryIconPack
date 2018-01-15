package com.sorcerer.sorcery.iconpack.apply.database.googleNow;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.apply.database.base.ILauncherApplier;
import com.sorcerer.sorcery.iconpack.data.db.Db;
import com.sorcerer.sorcery.iconpack.utils.ImageUtil;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.sorcerer.sorcery.iconpack.utils.RxSU;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/4
 */

class GoogleNowLauncherApplier implements ILauncherApplier {
    private static final String LAUNCHER_PACKAGE = "com.google.android.googlequicksearchbox";

    private static final String APP_ICONS_PATH =
            "/data/data/" + LAUNCHER_PACKAGE + "/databases/app_icons.db";

    private static final String APP_ICONS_JOURNAL_PATH =
            "/data/data/" + LAUNCHER_PACKAGE + "/databases/app_icons.db-journal";

    private static final String APP_ICONS_USER0_PATH
            = "/data/user/0/com.google.android.googlequicksearchbox/databases/app_icons.db";

    private static String CACHE_PATH;

    private Context mContext;

    private boolean mDisableWork;

    public GoogleNowLauncherApplier(Context context, boolean disableWork) {
        mContext = context;
        CACHE_PATH = context.getExternalCacheDir() + "/app_icons.db";
        mDisableWork = disableWork;
    }

    public Observable<List<String>> apply() {
        return RxSU.getInstance().su().filter(grant -> grant)
                .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Boolean grant) throws Exception {
                        return RxSU.getInstance().runAll(
                                "cp " + APP_ICONS_PATH + " " + CACHE_PATH
                        );
                    }
                })
                .flatMap(list -> {
                    PackageManager pm = mContext.getPackageManager();

                    Map<String, String> componentDrawableMap =
                            PackageUtil.getComponentDrawableMap(mContext);
                    List<IconReplacement> todoList = new ArrayList<>();

                    File file = new File(CACHE_PATH);
                    if (!file.exists()) {
                        throw new Exception("No cache file found");
                    }
                    SQLiteDatabase db = SQLiteDatabase
                            .openDatabase(file.getAbsolutePath(), null,
                                    SQLiteDatabase.OPEN_READWRITE);
                    Cursor cursor = db.query("icons", new String[]{"componentName", "profileId"},
                            null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        String cp = Db.getString(cursor, "componentName");
                        int pi = Db.getInt(cursor, "profileId");

                        if (componentDrawableMap.containsKey(cp)) {
                            todoList.add(new IconReplacement(cp, componentDrawableMap.get(cp), pi));
                        } else {
                            todoList.add(new IconReplacement(cp, "", pi));
                        }
                    }
                    cursor.close();

                    Bitmap androidWorkIndicator = ImageUtil.getResizedBitmap(
                            BitmapFactory.decodeResource(
                                    mContext.getResources(), R.drawable.android_for_work_indicator)
                                    .copy(Bitmap.Config.ARGB_8888, true),
                            192, 192);

                    Stream.of(todoList).forEach(icon -> {
                        ContentValues values = new ContentValues();

                        Bitmap bitmap = null;
                        if (icon.getDrawable().isEmpty()) {
                            try {
                                bitmap = ImageUtil.drawableToBitmap(PackageUtil
                                        .getAppIcon(pm, icon.getPackageName(),
                                                DisplayMetrics.DENSITY_XXXHIGH));
                                bitmap = ImageUtil.getResizedBitmap(
                                        bitmap.copy(Bitmap.Config.ARGB_8888, true), 192, 192);
                            } catch (PackageManager.NameNotFoundException e) {
                                Timber.d(e);
                            }
                        } else {
                            bitmap = ImageUtil.getResizedBitmap(
                                    BitmapFactory.decodeResource(mContext.getResources(),
                                            ResourceUtil.getDrawableRes(mContext,
                                                    icon.getDrawable()))
                                            .copy(Bitmap.Config.ARGB_8888, true),
                                    192, 192);
                        }
                        if (bitmap != null) {
                            if (icon.getProfileId() != 0 && !mDisableWork) {
                                bitmap = ImageUtil.overlay(bitmap, androidWorkIndicator);
                            }
                            byte[] flatBitmap = ImageUtil.flattenBitmap(bitmap);
                            values.put("icon", flatBitmap);
                            values.put("icon_low_res", flatBitmap);
                            db.update("icons", values, "componentName=? and profileId=?",
                                    new String[]{icon.getComponent(), icon.getProfileId() + ""});
                        }
                    });
                    db.close();

                    return RxSU.getInstance().runAll(
                            "cp " + CACHE_PATH + " " + APP_ICONS_PATH,
                            "rm -f " + CACHE_PATH,
                            "rm -f " + APP_ICONS_JOURNAL_PATH,
                            "am force-stop " + LAUNCHER_PACKAGE,
                            "am force-stop " + "com.google.android.launcher");
                });
    }

    public Observable<List<String>> restore() {
        return RxSU.getInstance().su().filter(grant -> grant)
                .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Boolean aBoolean) throws Exception {
                        return RxSU.getInstance().runAll(
                                "rm -f " + APP_ICONS_PATH,
//                                "rm -f " + APP_ICONS_USER0_PATH,
                                "am force-stop " + LAUNCHER_PACKAGE,
                                "am force-stop " + "com.google.android.launcher");
                    }
                });
    }

    private static class IconReplacement {
        private String mComponent;
        private String mDrawable;
        private int mProfileId;
        private String mPackageName;

        public IconReplacement(String component, String drawable, int profileId) {
            mComponent = component;
            mDrawable = drawable;
            mProfileId = profileId;
            mPackageName = mComponent.split("/")[0];
        }

        public String getDrawable() {
            return mDrawable;
        }

        public void setDrawable(String drawable) {
            mDrawable = drawable;
        }

        public int getProfileId() {
            return mProfileId;
        }

        public void setProfileId(int profileId) {
            mProfileId = profileId;
        }

        public String getComponent() {
            return mComponent;
        }

        public void setComponent(String component) {
            mComponent = component;
        }

        @Override
        public String toString() {
            return "IconReplacement{" +
                    "\nmComponent='" + mComponent + '\'' +
                    "\n, mDrawable='" + mDrawable + '\'' +
                    "\n, mProfileId=" + mProfileId +
                    "\n}";
        }

        public String getPackageName() {
            return mPackageName;
        }
    }
}
