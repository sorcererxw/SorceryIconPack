package com.sorcerer.sorcery.iconpack.apply.appliers.database.pixel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.data.db.Db;
import com.sorcerer.sorcery.iconpack.su.RxSU;
import com.sorcerer.sorcery.iconpack.utils.ImageUtil;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

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
 * @date: 2017/2/2
 */

public class PixelLauncherApplier {

    private static final String LAUNCHER_PACKAGE = "com.google.android.apps.nexuslauncher";

    private static final String LAUNCHER_PATH =
            "/data/data/com.google.android.apps.nexuslauncher/databases/launcher.db";

    private static final String APP_ICONS_PATH =
            "/data/data/com.google.android.apps.nexuslauncher/databases/app_icons.db";

    private static String CACHE_PATH;

    private Context mContext;

    public PixelLauncherApplier(Context context) {
        mContext = context;
        CACHE_PATH = context.getExternalCacheDir() + "/app_icons.db";
    }

    public Observable<List<String>> apply() {
        return RxSU.getInstance().su().filter(grant -> {
            if (!grant) {
                throw new Exception(
                        ResourceUtil.getString(mContext, R.string.apply_by_database_no_root_access));
            }
            return true;
        })
                .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Boolean grant) throws Exception {
                        return RxSU.getInstance().runAll(
                                "cp " + APP_ICONS_PATH + " " + CACHE_PATH
                        );
                    }
                })
                .flatMap(list -> {
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
                        }
                    }
                    cursor.close();

                    Bitmap androidWorkIndicator = ImageUtil.getResizedBitmap(
                            BitmapFactory.decodeResource(
                                    mContext.getResources(), R.drawable.android_for_work_indicator)
                                    .copy(Bitmap.Config.ARGB_8888, true),
                            192, 192);

                    Stream.of(todoList).forEach(icon -> {
                        Timber.d(icon.toString());
                        ContentValues values = new ContentValues();
                        Bitmap bitmap = ImageUtil.getResizedBitmap(
                                BitmapFactory.decodeResource(mContext.getResources(),
                                        ResourceUtil.getDrawableRes(mContext, icon.getDrawable()))
                                        .copy(Bitmap.Config.ARGB_8888, true),
                                192, 192);
                        if (icon.getProfileId() == 10) {
                            bitmap = ImageUtil.overlay(bitmap, androidWorkIndicator);
                        }
                        byte[] flatBitmap = ImageUtil.flattenBitmap(bitmap);
                        values.put("icon", flatBitmap);
                        values.put("icon_low_res", flatBitmap);
                        db.update("icons", values, "componentName=? and profileId=?",
                                new String[]{icon.getComponent(), icon.getProfileId() + ""});
                    });

                    db.close();
                    return RxSU.getInstance().runAll(
                            "cp " + CACHE_PATH + " " + APP_ICONS_PATH,
                            "am force-stop " + LAUNCHER_PACKAGE);
                });
    }

    public Observable<List<String>> restore() {
        return RxSU.getInstance().su().filter(grant -> grant)
                .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Boolean aBoolean) throws Exception {
                        return RxSU.getInstance().runAll("rm -f " + APP_ICONS_PATH,
                                "am force-stop " + LAUNCHER_PACKAGE);
                    }
                });
    }

    private static class IconReplacement {
        private String mComponent;
        private String mDrawable;
        private int mProfileId;

        public IconReplacement(String component, String drawable, int profileId) {
            mComponent = component;
            mDrawable = drawable;
            mProfileId = profileId;
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
    }
}
