package com.sorcerer.sorcery.iconpack.apply.appliers.database.smartisan;

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
 * @date: 2017/2/10
 */

public class SmartisanLauncherApplier {

    private static final String LAUNCHER_PACKAGE = "com.smartisanos.home";

    private static final String LAUNCHER_PATH =
            "/data/data/com.smartisanos.home/databases/launcher.db";


    private static String CACHE_PATH;

    private Context mContext;

    public SmartisanLauncherApplier(Context context) {
        mContext = context;
        CACHE_PATH = context.getExternalCacheDir() + "/launcher.db";
        Timber.d(CACHE_PATH);
    }

    public Observable<List<String>> apply() {
        return RxSU.getInstance().su().filter(grant -> {
            if (!grant) {
                throw new Exception(ResourceUtil
                        .getString(mContext, R.string.apply_by_database_no_root_access));
            }
            return true;
        })
                .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Boolean grant) throws Exception {
                        return RxSU.getInstance().runAll(
                                "cp " + LAUNCHER_PATH + " " + CACHE_PATH
                        );
                    }
                })
                .flatMap(list -> {
                    Map<String, String> componentDrawableMap =
                            PackageUtil.getComponentDrawableMap(mContext);


                    File file = new File(CACHE_PATH);
                    if (!file.exists()) {
                        throw new Exception("No cache file found");
                    }
                    SQLiteDatabase db = SQLiteDatabase
                            .openDatabase(file.getAbsolutePath(), null,
                                    SQLiteDatabase.OPEN_READWRITE);

                    replaceRedirectIcon(db, componentDrawableMap);
                    replaceTableItem(db, componentDrawableMap);

                    db.close();
                    return RxSU.getInstance().runAll(
                            "cp " + CACHE_PATH + " " + LAUNCHER_PATH,
                            "am force-stop " + LAUNCHER_PACKAGE);
                });
    }

    private void replaceRedirectIcon(SQLiteDatabase db, Map<String, String> componentDrawableMap) {
        List<RedirectIconReplacement> redirectIconList = new ArrayList<>();

        Cursor cursor =
                db.query("redirect_icons", new String[]{"packageName", "componentName"},
                        null, null, null, null, null);
        while (cursor.moveToNext()) {
            String pk = Db.getString(cursor, "packageName");
            String cp = Db.getString(cursor, "componentName");
            Timber.d(cp);
            if (componentDrawableMap.containsKey(pk + "/" + cp)) {
                redirectIconList.add(new RedirectIconReplacement(pk, cp,
                        componentDrawableMap.get(pk + "/" + cp)));
            }
        }
        cursor.close();

        Stream.of(redirectIconList).forEach(icon -> {
            Timber.d(icon.toString());
            ContentValues values = new ContentValues();
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                    ResourceUtil.getDrawableRes(mContext, icon.getDrawable()))
                    .copy(Bitmap.Config.ARGB_8888, true);
            byte[] flatBitmap = ImageUtil.flattenBitmap(bitmap);
            values.put("icon", flatBitmap);
            db.update("redirect_icons", values, "packageName=? and componentName=?",
                    new String[]{icon.getPackageName(), icon.getComponent()});
        });
    }

    private void replaceTableItem(SQLiteDatabase db, Map<String, String> componentDrawableMap) {
        List<TableItemReplacement> replacementList = new ArrayList<>();

        Cursor cursor =
                db.query("table_iteminfos", new String[]{"packageName", "componentName", "_id"},
                        null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = Db.getInt(cursor, "_id");
            String pk = Db.getString(cursor, "packageName");
            String cp = Db.getString(cursor, "componentName");
            Timber.d(cp);
            if (componentDrawableMap.containsKey(pk + "/" + cp)) {
                replacementList.add(new TableItemReplacement(id,
                        componentDrawableMap.get(pk + "/" + cp)));
            }
        }
        cursor.close();

        Stream.of(replacementList).forEach(icon -> {
            Timber.d(icon.toString());
            ContentValues values = new ContentValues();
            Bitmap bitmap = ImageUtil.getMarginBitmap(
                    BitmapFactory.decodeResource(mContext.getResources(),
                            ResourceUtil.getDrawableRes(mContext, icon.getDrawable()))
                            .copy(Bitmap.Config.ARGB_8888, true),
                    14, 26, 38, 26);
            byte[] flatBitmap = ImageUtil.flattenBitmap(bitmap);
            values.put("dark_icon", flatBitmap);
            values.put("light_icon", flatBitmap);

            db.update("table_icons", values, "owner=?", new String[]{icon.getId() + ""});
        });
    }

    public Observable<List<String>> restore() {
        return RxSU.getInstance().su().filter(grant -> {
            if (!grant) {
                throw new Exception(ResourceUtil
                        .getString(mContext, R.string.apply_by_database_no_root_access));
            }
            return true;
        })
                .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Boolean grant) throws Exception {
                        return RxSU.getInstance().runAll(
                                "cp " + LAUNCHER_PATH + " " + CACHE_PATH
                        );
                    }
                })
                .flatMap(list -> {
                    File file = new File(CACHE_PATH);
                    if (!file.exists()) {
                        throw new Exception("No cache file found");
                    }
                    SQLiteDatabase db = SQLiteDatabase
                            .openDatabase(file.getAbsolutePath(), null,
                                    SQLiteDatabase.OPEN_READWRITE);

                    db.execSQL("DROP TABLE redirect_icons");
                    db.execSQL("DROP TABLE table_icons");

                    db.close();
                    return RxSU.getInstance().runAll(
                            "cp " + CACHE_PATH + " " + LAUNCHER_PATH,
                            "am force-stop " + LAUNCHER_PACKAGE);
                });
    }

    private static class TableItemReplacement {
        private int mId;
        private String mDrawable;

        public TableItemReplacement(int id, String drawable) {
            mId = id;
            mDrawable = drawable;
        }

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public String getDrawable() {
            return mDrawable;
        }

        public void setDrawable(String drawable) {
            mDrawable = drawable;
        }
    }

    private static class RedirectIconReplacement {
        private String mPackageName;
        private String mComponent;
        private String mDrawable;

        public RedirectIconReplacement(String packageName, String component, String drawable) {
            mPackageName = packageName;
            mComponent = component;
            mDrawable = drawable;
        }

        public String getDrawable() {
            return mDrawable;
        }

        public void setDrawable(String drawable) {
            mDrawable = drawable;
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
                    "\n}";
        }

        public String getPackageName() {
            return mPackageName;
        }

        public void setPackageName(String packageName) {
            mPackageName = packageName;
        }
    }

}
