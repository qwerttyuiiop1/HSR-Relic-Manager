package com.example.hsrrelicmanager.ui.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.core.database.getLongOrNull
import com.example.hsrrelicmanager.model.relics.Mainstat
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.relics.Slot
import com.example.hsrrelicmanager.model.relics.Substat
import com.example.hsrrelicmanager.model.relics.relicSets
import com.example.hsrrelicmanager.model.rules.ActionGroup
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.FilterMap
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DBManager(private val context: Context) {

    private lateinit var dbHelper: DBHelper
    private lateinit var database: SQLiteDatabase

    @Throws(SQLException::class)
    fun open(): DBManager {
        dbHelper = DBHelper(context)
        database = dbHelper.writableDatabase
        return this
    }

    fun close() {
        dbHelper.close()
    }



    /* RULES TABLE */
    fun insertGroup(group: ActionGroup) {
        insertGroup(group.filters, group.position, group.action, group.parentGroup?.id)
    }
    fun insertGroup(
        filters: MutableMap<Filter.Type, Filter>,
        position: Int?,
        action: Action?,
        parentId: Long? = null
    ): Long {
        val values = ContentValues().apply {
            put(DBHelper.RulesTable.COLUMN_POS, position)
            put(DBHelper.RulesTable.COLUMN_FILTERS, Json.encodeToString(filters))

            if (parentId != null) {
                put(DBHelper.RulesTable.COLUMN_PARENT_ID, parentId)
            } else {
                putNull(DBHelper.RulesTable.COLUMN_PARENT_ID)
            }

            if (action == null) {
                putNull(DBHelper.RulesTable.COLUMN_ACTION)
                putNull(DBHelper.RulesTable.COLUMN_LEVEL)
            } else if (action is EnhanceAction) {
                put(DBHelper.RulesTable.COLUMN_ACTION, "Enhance")
                put(DBHelper.RulesTable.COLUMN_LEVEL, action.targetLevel)
            } else if (action is StatusAction) {
                put(DBHelper.RulesTable.COLUMN_ACTION, action.targetStatus.name)
                putNull(DBHelper.RulesTable.COLUMN_LEVEL)
            } else {
                throw IllegalArgumentException("Invalid action type: $action")
            }
        }

        val id = database.insert(DBHelper.RulesTable.TABLE_NAME, null, values)

        return id
    }

    fun fetchGroups(): Cursor {
        val columns = arrayOf(
            DBHelper.RulesTable._ID,
            DBHelper.RulesTable.COLUMN_POS,
            DBHelper.RulesTable.COLUMN_FILTERS,
            DBHelper.RulesTable.COLUMN_PARENT_ID,
            DBHelper.RulesTable.COLUMN_ACTION,
            DBHelper.RulesTable.COLUMN_LEVEL
        )
        val cursor = database.query(
            DBHelper.RulesTable.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            DBHelper.RulesTable.COLUMN_POS
        )
        return cursor
    }

    fun listGroups(): MutableList<ActionGroup> {
        val cursor = fetchGroups()
        val groups = mutableMapOf<
            Long,
            Pair<Long?, ActionGroup>
        >()
        val rootViews = mutableListOf<ActionGroup>()

        if (cursor.moveToFirst()) {
            do {
                var action: Action? = null
                if (!cursor.isNull(cursor.getColumnIndexOrThrow(DBHelper.RulesTable.COLUMN_ACTION))) {
                    val actionDb =
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RulesTable.COLUMN_ACTION))

                    action = if (actionDb == "Enhance") {
                        EnhanceAction(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.RulesTable.COLUMN_LEVEL)))
                    } else {
                        StatusAction(Relic.Status.valueOf(actionDb.uppercase()))
                    }
                }
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.RulesTable._ID))
                val filters = Json.decodeFromString<FilterMap>(
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RulesTable.COLUMN_FILTERS))
                )
                val pos = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.RulesTable.COLUMN_POS))
                val parentId = cursor.getLongOrNull(cursor.getColumnIndexOrThrow(DBHelper.RulesTable.COLUMN_PARENT_ID))
                val children = mutableListOf<ActionGroup>()

                val grouo = ActionGroup(
                    id,
                    filters,
                    pos,
                    null,
                    children,
                    action,
                )
                groups[id] = Pair(parentId, grouo)
            } while (cursor.moveToNext())
        }
        cursor.close()

        groups.forEach { (_, foo) ->
            val (parentId, group) = foo
            if (parentId == null) {
                rootViews.add(group)
            } else {
                groups[parentId]?.second?.groupList?.add(group)
            }
        }
        groups.forEach { (_, foo) ->
            foo.second.groupList.sortBy {
                it.position
            }
        }
        rootViews.sortBy {
            it.position
        }
        return rootViews
    }

    fun updateGroup(group: ActionGroup): Int {
        val values = ContentValues().apply {
            put(DBHelper.RulesTable.COLUMN_POS, group.position)
            put(DBHelper.RulesTable.COLUMN_FILTERS, Json.encodeToString(group.filters))

            if (group.parentGroup != null) {
                put(DBHelper.RulesTable.COLUMN_PARENT_ID, group.parentGroup!!.id)
            } else {
                putNull(DBHelper.RulesTable.COLUMN_PARENT_ID)
            }

            if (group.action == null) {
                putNull(DBHelper.RulesTable.COLUMN_ACTION)
                putNull(DBHelper.RulesTable.COLUMN_LEVEL)
            } else if (group.action is EnhanceAction) {
                put(DBHelper.RulesTable.COLUMN_ACTION, "Enhance")
                put(DBHelper.RulesTable.COLUMN_LEVEL, (group.action as EnhanceAction).targetLevel)
            } else {
                put(DBHelper.RulesTable.COLUMN_ACTION, group.action!!.name)
                putNull(DBHelper.RulesTable.COLUMN_LEVEL)
            }
        }
        Log.d("TEST", "UPDATED")

        return database.update(
            DBHelper.RulesTable.TABLE_NAME,
            values,
            "${DBHelper.RulesTable._ID} = ?",
            arrayOf(group.id.toString())
        )
    }

    fun deleteGroup(id: Long): Int {
        Log.d("TEST", "DELETED")
        return database.delete(
            DBHelper.RulesTable.TABLE_NAME,
            "${DBHelper.RulesTable._ID} = ?",
            arrayOf(id.toString())
        )
    }



    /* INVENTORY TABLE */
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
            put(DBHelper.COLUMN_SET, relicSet)
            put(DBHelper.COLUMN_SLOT, slot)
            put(DBHelper.COLUMN_RARITY, rarity)
            put(DBHelper.COLUMN_LEVEL, level)
            put(DBHelper.COLUMN_MAINSTAT, mainStat)
            put(DBHelper.COLUMN_MAINSTAT_VAL, mainStatVal)
            put(DBHelper.COLUMN_STATUS, status)
            put(DBHelper.COLUMN_EQUIPPED, equipped)
        }
        val id = database.insert(DBHelper.TABLE_RELIC, null, values)
        return id
    }

    fun fetchRelic(): Cursor {
        val columns = arrayOf(
            DBHelper._ID,
            DBHelper.COLUMN_SET,
            DBHelper.COLUMN_SLOT,
            DBHelper.COLUMN_RARITY,
            DBHelper.COLUMN_LEVEL,
            DBHelper.COLUMN_MAINSTAT,
            DBHelper.COLUMN_MAINSTAT_VAL,
            DBHelper.COLUMN_STATUS,
            DBHelper.COLUMN_EQUIPPED
        )
        val cursor = database.query(
            DBHelper.TABLE_RELIC,
            columns,
            null,
            null,
            null,
            null,
            null
        )
        return cursor
    }
    fun listRelics(): MutableList<Relic> {
        val cursor = fetchRelic()
        val relics = mutableListOf<Relic>()

        if (cursor.moveToFirst()) {
            do {
                val relic = extractRelic(cursor)
                relics.add(relic)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return relics
    }

    private fun extractRelic(cursor: Cursor): Relic {
        val relic_id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper._ID))

        val statuses = mutableListOf<Relic.Status>()
        statuses.add(
            Relic.Status.valueOf(
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        DBHelper.COLUMN_STATUS
                    )
                )
            )
        )

        if (cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EQUIPPED)) == 1) {
            statuses.add(Relic.Status.EQUIPPED)
        }


        val relic = Relic(
            relic_id,
            getRelicSetByName(
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        DBHelper.COLUMN_SET
                    )
                )
            ),
            Slot.fromName(
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        DBHelper.COLUMN_SLOT
                    )
                )
            )!!,
            cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_RARITY)),
            cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_LEVEL)),
            Mainstat.fromName(
                cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        DBHelper.COLUMN_MAINSTAT
                    )
                )
            )!!,
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_MAINSTAT_VAL)),
            fetchSubstatsForRelic(relic_id).map {
                Substat.fromName(it.key)!!.copy(value = it.value)
            }.toSet(),
            statuses,
        )
        return relic
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
            put(DBHelper.COLUMN_SET, relicSet)
            put(DBHelper.COLUMN_SLOT, slot)
            put(DBHelper.COLUMN_RARITY, rarity)
            put(DBHelper.COLUMN_LEVEL, level)
            put(DBHelper.COLUMN_MAINSTAT, mainStat)
            put(DBHelper.COLUMN_MAINSTAT_VAL, mainStatVal)
            put(DBHelper.COLUMN_STATUS, status)
            put(DBHelper.COLUMN_EQUIPPED, equipped)
        }
        return database.update(
            DBHelper.TABLE_RELIC,
            values,
            "${DBHelper._ID} = ?",
            arrayOf(id.toString())
        ) ?: 0
    }

    fun deleteRelic(id: Long) {
        database.delete(
            DBHelper.TABLE_RELIC,
            "${DBHelper._ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun findRelicIds(relic: Relic): List<Long> {
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

        return findRelicIds(
            relic.set.name,
            relic.slot.name,
            relic.rarity,
            relic.level,
            relic.mainstat.name,
            relic.mainstatVal,
            statusString,
            Relic.Status.EQUIPPED in relic.status,
            relic.substats.map {
                it.name to it.value
            }.toMap()
        )
    }

    fun findRelicIds(
        relicSet: String,
        slot: String,
        rarity: Int,
        level: Int,
        mainStat: String,
        mainStatVal: String,
        status: String,
        equipped: Boolean,
        substats: Map<String, String>
    ): List<Long> {
        val whereClause =
            "${DBHelper.COLUMN_SET} = ? AND " +
            "${DBHelper.COLUMN_SLOT} = ? AND " +
            "${DBHelper.COLUMN_RARITY} = ? AND " +
            "${DBHelper.COLUMN_LEVEL} = ? AND " +
            "${DBHelper.COLUMN_MAINSTAT} = ? AND " +
            "${DBHelper.COLUMN_MAINSTAT_VAL} = ? AND " +
            "${DBHelper.COLUMN_STATUS} = ? AND " +
            "${DBHelper.COLUMN_EQUIPPED} = ?"

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

        Log.d("DBManager", "Querying database with WHERE CLAUSE\n" + whereClause)
        Log.d("DBManager", "Args: " + whereArgs.joinToString(", "))
        Log.d("DBManager", "Substats: " + substats)

        val cursor = database.query(
            DBHelper.TABLE_RELIC,
            arrayOf(DBHelper._ID),
            whereClause,
            whereArgs,
            null,
            null,
            null,
            null
        )

        var relic_id = -1L
        val ids = mutableListOf<Long>()

        if (cursor.moveToFirst()) {
            do {
                relic_id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper._ID))
                if (fetchSubstatsForRelic(relic_id).equals(substats)) {
                    ids.add(relic_id)
                }
            } while (cursor.moveToNext())
        }

        //val relic_id = if (cursor.moveToNext()) cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper._ID)) else -1

        cursor.close()

        return ids
    }

    fun getRelicSetByName(relicSet: String): RelicSet {
        return relicSets.find { it.name == relicSet }
            ?: throw IllegalArgumentException("Relic set not found: $relicSet")
    }

    fun deleteAllRelics() {
        arrayOf(
            DBHelper.TABLE_RELIC,
            DBHelper.TABLE_SUBSTATS,
            DBHelper.TABLE_MANUAL_STATUS
        ).forEach {
            database.delete(
                it,
                null,
                null
            )
        }
    }



    /* SUBSTATS TABLE */

    fun insertSubstats(relicId: Long, substats: Map<String, String>) {
        val values = ContentValues()

        substats.forEach { (statName, statValue) ->
            values.put(DBHelper.COLUMN_RELIC_ID, relicId)
            values.put(DBHelper.COLUMN_SUBSTAT_NAME, statName)
            values.put(DBHelper.COLUMN_SUBSTAT_VALUE, statValue)
            database.insert(DBHelper.TABLE_SUBSTATS, null, values)
        }
    }

    fun updateSubstatValues(relicId: Long, substats: Map<String, String>) {
        substats.forEach { (statName, statValue) ->
            database.update(
                DBHelper.TABLE_SUBSTATS,
                ContentValues().apply {
                    put(DBHelper.COLUMN_SUBSTAT_VALUE, statValue)
                },
                "${DBHelper.COLUMN_RELIC_ID} = ? AND ${DBHelper.COLUMN_SUBSTAT_NAME} = ?",
                arrayOf(relicId.toString(), statName)
            )
        }
    }

    fun fetchSubstatsForRelic(relicId: Long): Map<String, String> {
        val substats = mutableMapOf<String, String>()

        val columns = arrayOf(
            DBHelper.COLUMN_SUBSTAT_NAME,
            DBHelper.COLUMN_SUBSTAT_VALUE
        )
        val cursor = database.query(
            DBHelper.TABLE_SUBSTATS,
            columns,
            "${DBHelper.COLUMN_RELIC_ID} = ?",
            arrayOf(relicId.toString()),
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val statName =
                    it.getString(it.getColumnIndexOrThrow(DBHelper.COLUMN_SUBSTAT_NAME))
                val statValue =
                    it.getString(it.getColumnIndexOrThrow(DBHelper.COLUMN_SUBSTAT_VALUE))
                substats[statName] = statValue
            }
        }

        return substats
    }



    /* MANUAL STATUS TABLE */

    fun setManualStatus(relic: Relic) {
        deleteStatus(relic)
        insertStatus(relic.id, relic.status.map{it.name})
    }
    fun insertStatus(relicId: Long, statuses: List<String>) {
        statuses.forEach {
            val values = ContentValues().apply {
                put(DBHelper.COLUMN_RELIC_ID, relicId)
                put(DBHelper.COLUMN_NEW_STATUS, it)
            }
            database.insert(DBHelper.TABLE_MANUAL_STATUS, null, values)
        }
    }

    fun fetchStatusForRelic(relicId: Long): List<Relic.Status> {
        val statuses = mutableListOf<Relic.Status>()

        val columns = arrayOf(DBHelper.COLUMN_NEW_STATUS)
        val cursor = database.query(
            DBHelper.TABLE_MANUAL_STATUS,
            columns,
            "${DBHelper.COLUMN_RELIC_ID} = ?",
            arrayOf(relicId.toString()),
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val statusValue = it.getString(it.getColumnIndexOrThrow(DBHelper.COLUMN_NEW_STATUS))
                val status = Relic.Status.valueOf(statusValue)
                statuses.add(status)
            }
        }

        return statuses
    }

    fun listManualStatuses(): MutableList<Pair<Relic, List<Relic.Status>>> {
        val allManualStatus = mutableListOf<Pair<Long, Relic.Status>>()
        val cursor = database.query(
            DBHelper.TABLE_MANUAL_STATUS,
            arrayOf(DBHelper.COLUMN_RELIC_ID, DBHelper.COLUMN_NEW_STATUS),
            null,
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val relicId = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_RELIC_ID))
                val status = Relic.Status.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NEW_STATUS)))
                allManualStatus.add(Pair(relicId, status))
            } while (cursor.moveToNext())
        }
        cursor.close()

        val relicIds = allManualStatus.map { it.first }.distinct()
        val relics = listRelics().filter { relicIds.contains(it.id) }

        return relics.map { relic ->
            Pair(relic, allManualStatus.filter { it.first == relic.id }.map { it.second })
        }.toMutableList()
    }

    fun deleteStatus(relic: Relic) {
        database.delete(
            DBHelper.TABLE_MANUAL_STATUS,
            "${DBHelper.COLUMN_RELIC_ID} = ?",
            arrayOf(relic.id.toString())
        )
    }
    fun deleteStatus(relicId: Long, statuses: List<String>) {
        statuses.forEach {
            database.delete(
                DBHelper.TABLE_MANUAL_STATUS,
                "${DBHelper.COLUMN_RELIC_ID} = ? AND ${DBHelper.COLUMN_NEW_STATUS} = ?",
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
//        insertStatus(
//            relic_id,
//            statuses
//        )
    }

    fun insertInventory(relic: Relic) {
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

        insertInventory(
            relic.set.name,
            relic.slot.name,
            relic.rarity,
            relic.level,
            relic.mainstat.name,
            relic.mainstatVal,
            relic.substats.map {
                it.name to it.value
            }.toMap(),
            statusString,
            Relic.Status.EQUIPPED in relic.status
        )
    }

    fun updateInventory(relic: Relic) {
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

        updateRelic(
            relic.id,
            relic.set.name,
            relic.slot.name,
            relic.rarity,
            relic.level,
            relic.mainstat.name,
            relic.mainstatVal,
            statusString,
            Relic.Status.EQUIPPED in relic.status
        )

        updateSubstatValues(relic.id, relic.substats.map {
            it.name to it.value
        }.toMap())

        deleteStatus(relic.id, fetchStatusForRelic(relic.id).map{it.name})
        insertStatus(relic.id, relic.status.map{it.name})
    }
}
