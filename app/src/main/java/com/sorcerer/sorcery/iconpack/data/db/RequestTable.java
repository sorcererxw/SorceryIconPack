package com.sorcerer.sorcery.iconpack.data.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/4/27
 */

public class RequestTable {
    public static final String TABLE = "request";

    public static final String COMPONENT = "component";

    public static final String REQUESTED = "requested";

    public static final String REQUEST_TIME = "requestTime";

    public static final String SELECTED = "selected";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(new TableBuilder(TABLE)
                .addTextColumn(COMPONENT, TableBuilder.FLAG_NOT_NULL)
                .addBooleanColumn(REQUESTED)
//                .addPrimaryKeyColumn(COMPONENT)
//                .addBooleanColumn(SELECTED)
                .build()
        );
    }
}
