package com.sorcerer.sorcery.iconpack.data.db;

import android.database.Cursor;

import java.util.Date;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/13
 */

public class Db {
//    private BriteDatabase mDatabase;
//
//    public Db(Context context) {
//        mDatabase = new SqlBrite.Builder()
//                .logger(Timber::d)
//                .build()
//                .wrapDatabaseHelper(new DbOpenHelper(context), Schedulers.immediate());
//    }
//
    /* --------------- Some DB getter ---------------- */

    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static String getString(Cursor cursor, String columnName, String fallbackValue) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex > -1) {
            return cursor.getString(columnIndex);
        }
        return fallbackValue;
    }

    public static boolean getBoolean(Cursor cursor, String columnName) {
        return getInt(cursor, columnName) == 1;
    }

    public static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public static long getLong(Cursor cursor, String columnName, long fallbackValue) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex > -1) {
            return cursor.getLong(columnIndex);
        }
        return fallbackValue;
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static int getInt(Cursor cursor, String columnName, int fallbackValue) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex > -1) {
            return cursor.getInt(columnIndex);
        }
        return fallbackValue;
    }

    public static Date getDate(Cursor cursor, String columnName) {
        return new Date(cursor.getLong(cursor.getColumnIndex(columnName)));
    }

    public static final String DB_STRING_ARRAY_SEPARATOR = "__,__";

    public static String[] getStringArray(Cursor cursor, String columnName) {
        return getString(cursor, columnName).split(DB_STRING_ARRAY_SEPARATOR);
    }
}