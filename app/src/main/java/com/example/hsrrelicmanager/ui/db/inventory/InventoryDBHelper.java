package com.example.hsrrelicmanager.ui.db.inventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDBHelper extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "HSR_RELIC_MANAGER.DB";



    // RELIC TABLE
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



    // SUBSTATS TABLE
    public static final String
            TABLE_SUBSTATS = "substats",
            COLUMN_RELIC_ID = "relic_id",
            COLUMN_SUBSTAT_NAME = "substat_name",
            COLUMN_SUBSTAT_VALUE = "substat_value";

    // Creating SUBSTATS TABLE query
    private static final String CREATE_SUBSTATS_TABLE =
            "CREATE TABLE " + TABLE_SUBSTATS + " (" +
                    COLUMN_RELIC_ID + " INTEGER NOT NULL, " +
                    COLUMN_SUBSTAT_NAME + " TEXT, " +
                    COLUMN_SUBSTAT_VALUE + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_RELIC_ID + ") REFERENCES " + TABLE_RELIC + " (" + _ID + "));";



    // MANUAL STATUS TABLE
    public static final String
            TABLE_MANUAL_STATUS = "manual_status",
//            COLUMN_RELIC_ID = "relic_id",  // same as in SUBSTATS TABLE
            COLUMN_NEW_STATUS = "new_status";

    // Creating STATUS TABLE query
    private static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + TABLE_MANUAL_STATUS + " (" +
                    COLUMN_RELIC_ID + " INTEGER NOT NULL, " +
                    COLUMN_NEW_STATUS + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_RELIC_ID + ") REFERENCES " + TABLE_RELIC + " (" + _ID + "));";



    public InventoryDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RELIC_TABLE);
        db.execSQL(CREATE_SUBSTATS_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RELIC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBSTATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANUAL_STATUS);
        onCreate(db);
    }
}
