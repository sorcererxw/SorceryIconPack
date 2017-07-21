package com.sorcerer.sorcery.iconpack.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.annimon.stream.Stream;
import com.squareup.sqlbrite2.BriteDatabase;

import java.util.List;

import io.reactivex.Observable;

import static com.sorcerer.sorcery.iconpack.data.db.RequestTable.COMPONENT;
import static com.sorcerer.sorcery.iconpack.data.db.RequestTable.REQUESTED;
import static com.sorcerer.sorcery.iconpack.data.db.RequestTable.TABLE;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/4/27
 */

public class RequestDbManager {
    private BriteDatabase mDatabase;

    RequestDbManager(BriteDatabase database) {
        mDatabase = database;
    }

//    public Observable<Boolean> isRequest(String component) {
//        return RxJavaInterop.toV2Observable(
//                mDatabase.createQuery(TABLE,
//                        "SELECT * FROM " + TABLE + " where " + COMPONENT + " = ?", component)
//                        .mapToList(cursor -> Db.getBoolean(cursor, REQUESTED))
//                        .map(list -> list.get(0))
//        );
//    }

    public Observable<Boolean> isRequest(String component) {
        return mDatabase.createQuery(TABLE,
                "SELECT * FROM " + TABLE + " where " + COMPONENT + " = ?", component)
                .mapToList(cursor -> Db.getBoolean(cursor, REQUESTED))
                .map(list -> list.get(0));
    }

    public void saveComponent(List<String> components) {
        try (BriteDatabase.Transaction transaction = mDatabase.newTransaction()) {
            Stream.of(components).map(component -> {
                ContentValues value = new ContentValues();
                value.put(COMPONENT, component);
                value.put(REQUESTED, 0);
                return value;
            }).forEach(contentValues -> mDatabase
                    .insert(TABLE, contentValues, SQLiteDatabase.CONFLICT_IGNORE)
            );
            transaction.markSuccessful();
        }
    }

    public void setRequested(List<String> components) {
        Stream.of(components).forEach(component -> {
            ContentValues value = new ContentValues();
            value.put(REQUESTED, 1);
            mDatabase.update(TABLE, value, COMPONENT + " = ?", component);
        });
    }

    public boolean isContain(String component) {
        try (Cursor cursor = mDatabase
                .query("SELECT * FROM " + TABLE + " WHERE " + COMPONENT + "=?", component)) {
            return cursor.moveToNext();
        }
    }
}
