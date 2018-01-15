package com.sorcerer.sorcery.iconpack.apply.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;

import com.sorcerer.sorcery.iconpack.data.db.Db;
import com.sorcerer.sorcery.iconpack.utils.ImageUtil;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.sorcerer.sorcery.iconpack.utils.RxSU;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/4
 */

abstract class DatabaseLauncherApplier {
    private String mLauncherPackage;

    private String mAppIconsPath;

    private String mTmpPath;

    private Context mContext;

    public DatabaseLauncherApplier(Context context) {
        mLauncherPackage = providePackageName();
        mAppIconsPath = "/data/data/" + providePackageName() + "/database/app_icon.db";
        mTmpPath = context.getExternalCacheDir() + "/app_icons.db";
        mContext = context;
    }

    protected abstract String providePackageName();

    public Observable<List<String>> apply() {
        return RxSU.getInstance().su().filter(new Predicate<Boolean>() {
            @Override
            public boolean test(Boolean grant) throws Exception {
                return grant;
            }
        })
                .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Boolean grant) throws Exception {
                        return RxSU.getInstance().runAll(
                                "cp " + mAppIconsPath + " " + mTmpPath
                        );
                    }
                })
                .flatMap(new Function<List<String>, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(List<String> strings)
                            throws Exception {
                        Map<String, String> componentDrawableMap =
                                PackageUtil.getComponentDrawableMap(mContext);
                        Map<String, String> todoMap = new HashMap<>();

                        File file = new File(mTmpPath);
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(
                                file.getAbsolutePath(),
                                null,
                                SQLiteDatabase.OPEN_READWRITE);
                        Cursor cursor = db.query("icons", new String[]{"componentName"},
                                null, null, null, null, null);
                        while (cursor.moveToNext()) {
                            String cp = Db.getString(cursor, "componentName");
                            if (componentDrawableMap.containsKey(cp)) {
                                todoMap.put(cp, componentDrawableMap.get(cp));
                            }
                        }
                        cursor.close();
                        for (Map.Entry<String, String> entry : todoMap.entrySet()) {
                            ContentValues values = new ContentValues();
                            byte[] bitmap = ImageUtil.flattenBitmap(BitmapFactory
                                    .decodeResource(mContext.getResources(),
                                            ResourceUtil.getDrawableRes(mContext,
                                                    entry.getValue())));
                            values.put("icon", bitmap);
                            values.put("icon_low_res", bitmap);
                            db.update("icons", values, "componentName=? and profileId=0",
                                    new String[]{entry.getKey()});
                        }
                        return RxSU.getInstance().runAll(
                                "cp " + mTmpPath + " " + mAppIconsPath,
                                "am force-stop " + mLauncherPackage);
                    }
                });
    }
}
