package com.example.hsrrelicmanager.ui.db.rules

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.relics.relicSets

class RuleDBManager(private val context: Context) {

    private lateinit var dbHelper: RuleDBHelper
    private lateinit var database: SQLiteDatabase

    @Throws(SQLException::class)
    fun open(): RuleDBManager {
        dbHelper = RuleDBHelper(context)
        database = dbHelper.writableDatabase
        return this
    }

    fun close() {
        dbHelper.close()
    }



    // INVENTORY TABLE
    fun insertRelic(
        relicSet: String,
        slot: String,
        rarity: Int,
        level: Int,
        mainStat: String,
        mainStatVal: String,
        status: String,
        equipped: Boolean
    ): Long {
        val values = ContentValues().apply {
            put(RuleDBHelper.COLUMN_SET, relicSet)
            put(RuleDBHelper.COLUMN_SLOT, slot)
            put(RuleDBHelper.COLUMN_RARITY, rarity)
            put(RuleDBHelper.COLUMN_LEVEL, level)
            put(RuleDBHelper.COLUMN_MAINSTAT, mainStat)
            put(RuleDBHelper.COLUMN_MAINSTAT_VAL, mainStatVal)
            put(RuleDBHelper.COLUMN_STATUS, status)
            put(RuleDBHelper.COLUMN_EQUIPPED, equipped)
        }
        val id = database.insert(RuleDBHelper.TABLE_RELIC, null, values)
        return id
    }

    fun fetchRelic(): Cursor {
        val columns = arrayOf(
            RuleDBHelper._ID,
            RuleDBHelper.COLUMN_SET,
            RuleDBHelper.COLUMN_SLOT,
            RuleDBHelper.COLUMN_RARITY,
            RuleDBHelper.COLUMN_LEVEL,
            RuleDBHelper.COLUMN_MAINSTAT,
            RuleDBHelper.COLUMN_MAINSTAT_VAL,
            RuleDBHelper.COLUMN_STATUS,
            RuleDBHelper.COLUMN_EQUIPPED
        )
        val cursor = database.query(
            RuleDBHelper.TABLE_RELIC,
            columns,
            null,
            null,
            null,
            null,
            null
        )
        return cursor
    }

    fun updateRelic(
        id: Long,
        relicSet: String,
        slot: String,
        rarity: Int,
        level: Int,
        mainStat: String,
        mainStatVal: String,
        status: String,
        equipped: Boolean
    ): Int {
        val values = ContentValues().apply {
            put(RuleDBHelper.COLUMN_SET, relicSet)
            put(RuleDBHelper.COLUMN_SLOT, slot)
            put(RuleDBHelper.COLUMN_RARITY, rarity)
            put(RuleDBHelper.COLUMN_LEVEL, level)
            put(RuleDBHelper.COLUMN_MAINSTAT, mainStat)
            put(RuleDBHelper.COLUMN_MAINSTAT_VAL, mainStatVal)
            put(RuleDBHelper.COLUMN_STATUS, status)
            put(RuleDBHelper.COLUMN_EQUIPPED, equipped)
        }
        return database.update(
            RuleDBHelper.TABLE_RELIC,
            values,
            "${RuleDBHelper._ID} = ?",
            arrayOf(id.toString())
        ) ?: 0
    }

    fun deleteRelic(id: Long) {
        database.delete(
            RuleDBHelper.TABLE_RELIC,
            "${RuleDBHelper._ID} = ?",
            arrayOf(id.toString())
        )
    }
}
