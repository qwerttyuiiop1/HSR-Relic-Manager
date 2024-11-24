package com.example.hsrrelicmanager.ui.db.inventory

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.relics.relicSets

class InventoryDBManager(private val context: Context) {

    private lateinit var dbHelper: InventoryDBHelper
    private lateinit var database: SQLiteDatabase

    @Throws(SQLException::class)
    fun open(): InventoryDBManager {
        dbHelper = InventoryDBHelper(context)
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
    ): Long {
        val values = ContentValues().apply {
            put(InventoryDBHelper.COLUMN_SET, relicSet)
            put(InventoryDBHelper.COLUMN_SLOT, slot)
            put(InventoryDBHelper.COLUMN_RARITY, rarity)
            put(InventoryDBHelper.COLUMN_LEVEL, level)
            put(InventoryDBHelper.COLUMN_MAINSTAT, mainStat)
            put(InventoryDBHelper.COLUMN_MAINSTAT_VAL, mainStatVal)
            put(InventoryDBHelper.COLUMN_STATUS, status)
        }
        val id = database.insert(InventoryDBHelper.TABLE_RELIC, null, values)
        return id
    }

    fun fetchRelic(): Cursor {
        val columns = arrayOf(
            InventoryDBHelper._ID,
            InventoryDBHelper.COLUMN_SET,
            InventoryDBHelper.COLUMN_SLOT,
            InventoryDBHelper.COLUMN_RARITY,
            InventoryDBHelper.COLUMN_LEVEL,
            InventoryDBHelper.COLUMN_MAINSTAT,
            InventoryDBHelper.COLUMN_MAINSTAT_VAL,
            InventoryDBHelper.COLUMN_STATUS
        )
        val cursor = database.query(
            InventoryDBHelper.TABLE_RELIC,
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
        status: String
    ): Int {
        val values = ContentValues().apply {
            put(InventoryDBHelper.COLUMN_SET, relicSet)
            put(InventoryDBHelper.COLUMN_SLOT, slot)
            put(InventoryDBHelper.COLUMN_RARITY, rarity)
            put(InventoryDBHelper.COLUMN_LEVEL, level)
            put(InventoryDBHelper.COLUMN_MAINSTAT, mainStat)
            put(InventoryDBHelper.COLUMN_MAINSTAT_VAL, mainStatVal)
            put(InventoryDBHelper.COLUMN_STATUS, status)
        }
        return database.update(
            InventoryDBHelper.TABLE_RELIC,
            values,
            "${InventoryDBHelper._ID} = ?",
            arrayOf(id.toString())
        ) ?: 0
    }

    fun deleteRelic(id: Long) {
        database.delete(
            InventoryDBHelper.TABLE_RELIC,
            "${InventoryDBHelper._ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun getRelicSetByName(relicSet: String): RelicSet {
        return relicSets.find { it.name == relicSet }
            ?: throw IllegalArgumentException("Relic set not found: $relicSet")
    }



    // SUBSTATS TABLE
    fun insertSubstats(relicId: Long, substats: Map<String, String>) {
        val values = ContentValues()

        substats.forEach { (statName, statValue) ->
            values.put(InventoryDBHelper.COLUMN_RELIC_ID, relicId)
            values.put(InventoryDBHelper.COLUMN_SUBSTAT_NAME, statName)
            values.put(InventoryDBHelper.COLUMN_SUBSTAT_VALUE, statValue)
            database.insert(InventoryDBHelper.TABLE_SUBSTATS, null, values)
        }
    }

    fun fetchSubstatsForRelic(relicId: Long): Map<String, String> {
        val substats = mutableMapOf<String, String>()

        val columns = arrayOf(
            InventoryDBHelper.COLUMN_SUBSTAT_NAME,
            InventoryDBHelper.COLUMN_SUBSTAT_VALUE
        )
        val cursor = database.query(
            InventoryDBHelper.TABLE_SUBSTATS,
            columns,
            "${InventoryDBHelper.COLUMN_RELIC_ID} = ?",
            arrayOf(relicId.toString()),
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val statName =
                    it.getString(it.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_SUBSTAT_NAME))
                val statValue =
                    it.getString(it.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_SUBSTAT_VALUE))
                substats[statName] = statValue
            }
        }

        return substats
    }



    // MANUAL STATUS TABLE
    fun insertStatus(relicId: Long, statuses: List<String>) {
        statuses.forEach {
            val values = ContentValues().apply {
                put(InventoryDBHelper.COLUMN_RELIC_ID, relicId)
                put(InventoryDBHelper.COLUMN_NEW_STATUS, it)
            }
            database.insert(InventoryDBHelper.TABLE_MANUAL_STATUS, null, values)
        }
    }

    fun fetchStatusForRelic(relicId: Long): List<Relic.Status> {
        val statuses = mutableListOf<Relic.Status>()

        val columns = arrayOf(InventoryDBHelper.COLUMN_NEW_STATUS)
        val cursor = database.query(
            InventoryDBHelper.TABLE_MANUAL_STATUS,
            columns,
            "${InventoryDBHelper.COLUMN_RELIC_ID} = ?",
            arrayOf(relicId.toString()),
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val statusValue = it.getString(it.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_NEW_STATUS))
                val status = Relic.Status.valueOf(statusValue)
                statuses.add(status)
            }
        }

        return statuses
    }

    fun deleteStatus(relicId: Long, statuses: List<String>) {
        statuses.forEach {
            database.delete(
                InventoryDBHelper.TABLE_MANUAL_STATUS,
                "${InventoryDBHelper.COLUMN_RELIC_ID} = ? AND ${InventoryDBHelper.COLUMN_NEW_STATUS} = ?",
                arrayOf(relicId.toString(), it)
            )
        }
    }



    // INSERT INVENTORY -  all in one, call to insert OLD relic scanned from OCR
    fun insertInventory(
        set: String,
        slot: String,
        rarity: Int,
        level: Int,
        mainstat: String,
        mainstat_val: String,
        substats: Map<String, String>,
        status: String
    ) {
        // Insert relic in DB
        val relic_id = insertRelic(
            set,
            slot,
            rarity,
            level,
            mainstat,
            mainstat_val,
            status
        )

        // Insert substats in DB
        insertSubstats(
            relic_id,
            substats
        )

        // Insert status in DB
        insertStatus(
            relic_id,
            listOf(status)
        )
    }
}
