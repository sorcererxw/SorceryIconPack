package com.sorcerer.sorcery.iconpack.apply.appliers.xposed;

import android.database.sqlite.SQLiteDatabase;

import com.sorcerer.sorcery.iconpack.utils.su.RxSU;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/24
 */

class Utils {
    public static Observable<Boolean> isSeLinuxPermissive() {
        return Observable.just(1)
                .map(integer -> {
                    try {
                        Timber.d("isSeLinuxPermissive");
                        Runtime runtime = Runtime.getRuntime();

                        Process process = runtime.exec("getenforce");
                        InputStream inputStream = process.getInputStream();
                        InputStreamReader ireader = new InputStreamReader(inputStream);
                        BufferedReader breader = new BufferedReader(ireader);
                        String s;
                        while ((s = breader.readLine()) != null) {
                            if (s.trim().toLowerCase().equals("permissive")) {
                                return true;
                            }
                        }
                    } catch (IOException e) {
                        Timber.e(e);
                    }
                    return false;
                });
    }

    public static Observable<List<String>> enableSeLinux(boolean enable) {
        return RxSU.getInstance()
                .runAll("su 0 setenforce " + (enable ? 1 : 0));
    }

    public static Observable<List<String>> clearNovaCache() {
        return RxSU.getInstance()
                .runAll("if [ -f /data/data/com.teslacoilsw.launcher/databases/launcher.db ]; then cat /data/data/com.teslacoilsw.launcher/databases/launcher.db > /sdcard/nova_tmp.db; fi;")
                .flatMap(new Function<List<String>, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(List<String> list)
                            throws Exception {
                        SQLiteDatabase db =
                                SQLiteDatabase.openDatabase("/sdcard/nova_tmp.db", null, 0);
                        db.execSQL("update allapps set icon = null; ");
                        db.close();
                        return RxSU.getInstance().runAll(
                                "cat /sdcard/nova_tmp.db > /data/data/com.teslacoilsw.launcher/databases/launcher.db; owner=$(stat -c %u /data/data/com.teslacoilsw.launcher/databases/launcher.db-journal);chown $owner:$owner /data/data/com.teslacoilsw.launcher/databases/launcher.db; chmod 660 /data/data/com.teslacoilsw.launcher/databases/launcher.db; rm /sdcard/nova_tmp.db*;",
                                "am force-stop com.teslacoilsw.launcher"
                        );
                    }
                });
    }

    public static Observable<List<String>> reboot() {
        return RxSU.getInstance()
                .runAll("reboot");
    }
}
