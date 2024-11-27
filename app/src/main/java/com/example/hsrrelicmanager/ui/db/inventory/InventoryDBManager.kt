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
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

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

    // RULES
    fun insertGroup(
        filters: MutableMap<Filter.Type, Filter>,
        position: Int,
        action: Action?,
        parentId: Long? = null
    ): Long {
        val values = ContentValues().apply {
            put(InventoryDBHelper.RulesTable.COLUMN_POS, position)
            put(InventoryDBHelper.RulesTable.COLUMN_FILTERS, Json.encodeToString(filters))

            if (parentId != null) {
                put(InventoryDBHelper.RulesTable.COLUMN_PARENT_ID, parentId)
            } else {
                putNull(InventoryDBHelper.RulesTable.COLUMN_PARENT_ID)
            }

            if (action == null) {
                putNull(InventoryDBHelper.RulesTable.COLUMN_ACTION)
                putNull(InventoryDBHelper.RulesTable.COLUMN_LEVEL)
            } else if (action is EnhanceAction) {
                put(InventoryDBHelper.RulesTable.COLUMN_ACTION, "Enhance")
                put(InventoryDBHelper.RulesTable.COLUMN_LEVEL, action.targetLevel)
            } else {
                put(InventoryDBHelper.RulesTable.COLUMN_ACTION, action.toString())
                putNull(InventoryDBHelper.RulesTable.COLUMN_LEVEL)
            }
        }

        val id = database.insert(InventoryDBHelper.RulesTable.TABLE_NAME, null, values)

        return id
    }

    fun fetchGroups(): Cursor {
        val columns = arrayOf(
            InventoryDBHelper.RulesTable._ID,
            InventoryDBHelper.RulesTable.COLUMN_POS,
            InventoryDBHelper.RulesTable.COLUMN_FILTERS,
            InventoryDBHelper.RulesTable.COLUMN_PARENT_ID,
            InventoryDBHelper.RulesTable.COLUMN_ACTION,
            InventoryDBHelper.RulesTable.COLUMN_LEVEL
        )
        val cursor = database.query(
            InventoryDBHelper.RulesTable.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            InventoryDBHelper.RulesTable.COLUMN_POS
        )
        return cursor
    }

    fun updateGroup(group: ActionGroup): Int {
        val values = ContentValues().apply {
            put(InventoryDBHelper.RulesTable.COLUMN_POS, group.position)
            put(InventoryDBHelper.RulesTable.COLUMN_FILTERS, Json.encodeToString(group.filters))

            if (group.parentGroup != null) {
                put(InventoryDBHelper.RulesTable.COLUMN_PARENT_ID, group.parentGroup!!.id)
            } else {
                putNull(InventoryDBHelper.RulesTable.COLUMN_PARENT_ID)
            }

            if (group.action == null) {
                putNull(InventoryDBHelper.RulesTable.COLUMN_ACTION)
                putNull(InventoryDBHelper.RulesTable.COLUMN_LEVEL)
            } else if (group.action is EnhanceAction) {
                put(InventoryDBHelper.RulesTable.COLUMN_ACTION, "Enhance")
                put(InventoryDBHelper.RulesTable.COLUMN_LEVEL, (group.action as EnhanceAction).targetLevel)
            } else {
                put(InventoryDBHelper.RulesTable.COLUMN_ACTION, group.action.toString())
                putNull(InventoryDBHelper.RulesTable.COLUMN_LEVEL)
            }
        }

        return database.update(
            InventoryDBHelper.RulesTable.TABLE_NAME,
            values,
            "${InventoryDBHelper.RulesTable._ID} = ?",
            arrayOf(group.id.toString())
        )
    }

    fun deleteGroup(id: Long): Int {
        return database.delete(
            InventoryDBHelper.RulesTable.TABLE_NAME,
            "${InventoryDBHelper.RulesTable._ID} = ?",
            arrayOf(id.toString())
        )
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
            put(InventoryDBHelper.COLUMN_SET, relicSet)
            put(InventoryDBHelper.COLUMN_SLOT, slot)
            put(InventoryDBHelper.COLUMN_RARITY, rarity)
            put(InventoryDBHelper.COLUMN_LEVEL, level)
            put(InventoryDBHelper.COLUMN_MAINSTAT, mainStat)
            put(InventoryDBHelper.COLUMN_MAINSTAT_VAL, mainStatVal)
            put(InventoryDBHelper.COLUMN_STATUS, status)
            put(InventoryDBHelper.COLUMN_EQUIPPED, equipped)
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
            InventoryDBHelper.COLUMN_STATUS,
            InventoryDBHelper.COLUMN_EQUIPPED
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
        status: String,
        equipped: Boolean
    ): Int {
        val values = ContentValues().apply {
            put(InventoryDBHelper.COLUMN_SET, relicSet)
            put(InventoryDBHelper.COLUMN_SLOT, slot)
            put(InventoryDBHelper.COLUMN_RARITY, rarity)
            put(InventoryDBHelper.COLUMN_LEVEL, level)
            put(InventoryDBHelper.COLUMN_MAINSTAT, mainStat)
            put(InventoryDBHelper.COLUMN_MAINSTAT_VAL, mainStatVal)
            put(InventoryDBHelper.COLUMN_STATUS, status)
            put(InventoryDBHelper.COLUMN_EQUIPPED, equipped)
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

    fun findRelicId(relic: Relic): Long {
        lateinit var statusString: String

        if (Relic.Status.TRASH in relic.status) {
            statusString = "TRASH"
        } else if (Relic.Status.LOCK in relic.status) {
            statusString = "LOCK"
        } else if (Relic.Status.DEFAULT in relic.status){
            statusString = "DEFAULT"
        } else {
            throw IllegalStateException("Relic $relic does not have valid status ${relic.status}")
        }

        return findRelicId(
            relic.set.name,
            relic.slot,
            relic.rarity,
            relic.level,
            relic.mainstat,
            relic.mainstatVal,
            statusString,
            Relic.Status.EQUIPPED in relic.status,
            relic.substats
        )
    }

    fun findRelicId(
        relicSet: String,
        slot: String,
        rarity: Int,
        level: Int,
        mainStat: String,
        mainStatVal: String,
        status: String,
        equipped: Boolean,
        substats: Map<String, String>
    ): Long {
        val whereClause =
            "${InventoryDBHelper.COLUMN_SET} = ? AND " +
            "${InventoryDBHelper.COLUMN_SLOT} = ? AND " +
            "${InventoryDBHelper.COLUMN_RARITY} = ? AND " +
            "${InventoryDBHelper.COLUMN_LEVEL} = ? AND " +
            "${InventoryDBHelper.COLUMN_MAINSTAT} = ? AND " +
            "${InventoryDBHelper.COLUMN_MAINSTAT_VAL} = ? AND " +
            "${InventoryDBHelper.COLUMN_STATUS} = ? AND " +
            "${InventoryDBHelper.COLUMN_EQUIPPED} = ?"

        val whereArgs = arrayOf(
            relicSet,
            slot,
            rarity.toString(),
            level.toString(),
            mainStat,
            mainStatVal,
            status,
            (if (equipped) 1 else 0).toString()
        )

        Log.d("InventoryDBManager", "Querying database with WHERE CLAUSE\n" + whereClause)
        Log.d("InventoryDBManager", "Args: " + whereArgs.joinToString(", "))
        Log.d("InventoryDBManager", "Substats: " + substats)

        val cursor = database.query(
            InventoryDBHelper.TABLE_RELIC,
            arrayOf(InventoryDBHelper._ID),
            whereClause,
            whereArgs,
            null,
            null,
            null,
            null
        )

        var relic_id = -1L

        while (cursor.moveToNext()) {
            relic_id = cursor.getLong(cursor.getColumnIndexOrThrow(InventoryDBHelper._ID))
            if (fetchSubstatsForRelic(relic_id).equals(substats)) {
                break
            }
            relic_id = -1L
        }

        if (relic_id == -1L) {
            Log.d("InventoryDBManager", "Cursor is empty!")
        }

        //val relic_id = if (cursor.moveToNext()) cursor.getLong(cursor.getColumnIndexOrThrow(InventoryDBHelper._ID)) else -1

        cursor.close()

        return relic_id
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

    fun updateSubstatValues(relicId: Long, substats: Map<String, String>) {
        substats.forEach { (statName, statValue) ->
            database.update(
                InventoryDBHelper.TABLE_SUBSTATS,
                ContentValues().apply {
                    put(InventoryDBHelper.COLUMN_SUBSTAT_VALUE, statValue)
                },
                "${InventoryDBHelper.COLUMN_RELIC_ID} = ? AND ${InventoryDBHelper.COLUMN_SUBSTAT_NAME} = ?",
                arrayOf(relicId.toString(), statName)
            )
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
        status: String,
        equipped: Boolean
    ) {
        // Insert relic in DB
        val relic_id = insertRelic(
            set,
            slot,
            rarity,
            level,
            mainstat,
            mainstat_val,
            status,
            equipped
        )

        // Insert substats in DB
        insertSubstats(
            relic_id,
            substats
        )

        val statuses = mutableListOf<String>()
        statuses.add(status)

        // Add equipped status
        if (equipped) {
            statuses.add("EQUIPPED")
        }

        // Insert statuses in DB
        insertStatus(
            relic_id,
            statuses
        )
    }
}
