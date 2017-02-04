package com.sorcerer.sorcery.iconpack.appliers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.sorcerer.sorcery.iconpack.su.RxSU;

import java.io.File;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/2
 */

public class PixelLauncherApplier {
    private static final String APP_ICONS_PATH =
            "/data/data/com.google.android.apps.nexuslauncher/databases/app_icons.db";

    private static String TMP_PATH;

    private Context mContext;

    public PixelLauncherApplier(Context context) {
        mContext = context;
        TMP_PATH = context.getExternalCacheDir() + "/app_icons.db";
    }

    public void apply() {
        RxSU.getInstance().su().filter(grant -> grant)
                .flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Boolean grant) throws Exception {
                        return RxSU.getInstance().run(
                                "cp " + APP_ICONS_PATH + " " + TMP_PATH
                        );
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Timber.d(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        File file = new File(TMP_PATH);
                        SQLiteDatabase db = SQLiteDatabase
                                .openDatabase(file.getAbsolutePath(), null,
                                        SQLiteDatabase.OPEN_READWRITE);

                        Timber.d("" + db.isOpen());
                        Cursor cursor =
                                db.query("icons", new String[]{"label", "componentName"},
                                        null, null, null, null, null);
                        while (cursor.moveToNext()) {
                            DatabaseUtils.dumpCursor(cursor);
                        }
                        cursor.close();

                        ContentValues values = new ContentValues();

                    }
                });

    }
}
