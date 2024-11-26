package com.example.hsrrelicmanager.ui.db.rules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RuleDBHelper extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "HSR_RELIC_MANAGER.DB";



    // RULE TABLE
    public static final String
            TABLE_RELIC = "relic",
            _ID = "_id",
            COLUMN_SET = "relic_set",
            COLUMN_SLOT = "slot",
            COLUMN_RARITY = "rarity",
            COLUMN_LEVEL = "level",
            COLUMN_MAINSTAT = "mainstat",
            COLUMN_MAINSTAT_VAL = "mainstat_val",
            COLUMN_STATUS = "status",
            COLUMN_EQUIPPED = "equipped";

    // Creating relic table query
    private static final String CREATE_RELIC_TABLE =
            "CREATE TABLE " + TABLE_RELIC + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SET + " TEXT NOT NULL, " +
                    COLUMN_SLOT + " TEXT, " +
                    COLUMN_RARITY + " INTEGER, " +
                    COLUMN_LEVEL + " INTEGER, " +
                    COLUMN_MAINSTAT + " TEXT, " +
                    COLUMN_MAINSTAT_VAL + " TEXT, " +
                    COLUMN_STATUS + " TEXT, " +
                    COLUMN_EQUIPPED + " BOOLEAN " +
                    ");";



    public RuleDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RELIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RELIC);
        onCreate(db);
    }
}
