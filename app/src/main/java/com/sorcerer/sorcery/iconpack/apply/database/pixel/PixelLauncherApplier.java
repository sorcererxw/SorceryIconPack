package com.sorcerer.sorcery.iconpack.apply.database.pixel;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.apply.database.base.BaseLauncherApplier;
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
 * @date: 2017/2/2
 */

class PixelLauncherApplier extends BaseLauncherApplier {

    private static final String LAUNCHER_PACKAGE = "com.google.android.apps.nexuslauncher";

    @SuppressLint("SdCardPath")
    private static final String APP_ICONS_PATH =
            "/data/data/com.google.android.apps.nexuslauncher/databases/app_icons.db";

    private static String CACHE_PATH;

    private Context mContext;

    private boolean mDisableRound = false;
    private boolean mDisableWork = false;
    private boolean mNormalizeIconSize = false;

    PixelLauncherApplier(Context context,
                         boolean disableRound,
                         boolean disableWork,
                         boolean normalizeIconSize) {
        super();
        mContext = context;
        CACHE_PATH = context.getExternalCacheDir() + "/app_icons.db";
        mDisableRound = disableRound;
        mDisableWork = disableWork;
        mNormalizeIconSize = normalizeIconSize;
    }

    public Observable<List<String>> apply() {
        return RxSU.getInstance().su().filter(grant -> {
            if (!grant) {
                throw new Exception(
                        ResourceUtil
                                .getString(mContext, R.string.apply_by_database_no_root_access));
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
                        Timber.d(icon.toString());
                        ContentValues values = new ContentValues();
                        Bitmap bitmap = null;
                        if (icon.getDrawable().isEmpty()) {
                            try {
                                Drawable iconDrawable = null;
                                if (mDisableRound) {
                                    try {
                                        iconDrawable = PackageUtil.getAppIcon(pm,
                                                icon.getPackageName(),
                                                DisplayMetrics.DENSITY_XXXHIGH);
                                    } catch (PackageManager.NameNotFoundException e) {
                                        Timber.e(e);
                                    }
                                } else {
                                    try {
                                        iconDrawable = PackageUtil.getAppRoundIcon(pm,
                                                icon.getPackageName(),
                                                DisplayMetrics.DENSITY_XXXHIGH);
                                    } catch (PackageManager.NameNotFoundException e) {
                                        Timber.e(e);
                                    }
                                }
                                if (iconDrawable != null) {
                                    bitmap = ImageUtil.drawableToBitmap(iconDrawable);

                                    if (mNormalizeIconSize) {
                                        bitmap = ImageUtil.getResizedBitmap(
                                                ImageUtil.normalizeIconSize(bitmap)
                                                        .copy(Bitmap.Config.ARGB_8888, true),
                                                192, 192);
                                    } else {
                                        bitmap = ImageUtil.getResizedBitmap(
                                                bitmap.copy(Bitmap.Config.ARGB_8888, true), 192,
                                                192);
                                    }

                                }
                            } catch (Exception e) {
                                Timber.e(e);
                            }
                        } else {
                            bitmap = ImageUtil.getResizedBitmap(
                                    BitmapFactory.decodeResource(mContext.getResources(),
                                            ResourceUtil
                                                    .getDrawableRes(mContext, icon.getDrawable()))
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
                        } else {
                            db.delete("icons", "componentName=? and profileId=?",
                                    new String[]{icon.getComponent(), icon.getProfileId() + ""});
                        }
                        Boolean skip = mPrefs.pixelIconSkip(icon.getPackageName()).get();

                        if (skip != null && skip) {
                            db.delete("icons",
                                    "componentName LIKE '" + icon.getPackageName() + "%'",
                                    new String[]{});
                        }
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
        private String mPackageName;

        IconReplacement(String component, String drawable, int profileId) {
            mComponent = component;
            mDrawable = drawable;
            mProfileId = profileId;
            mPackageName = component.split("/")[0];
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
