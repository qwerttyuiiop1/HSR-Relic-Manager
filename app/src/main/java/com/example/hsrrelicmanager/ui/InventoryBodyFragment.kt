package com.example.hsrrelicmanager.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.relicSets
import com.example.hsrrelicmanager.ui.db.inventory.InventoryDBHelper
import com.example.hsrrelicmanager.ui.db.inventory.InventoryDBManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class InventoryBodyFragment : Fragment() {
    private lateinit var dbManager: InventoryDBManager

    val relicData = mutableListOf<Relic>()
    val dbRelics = mutableListOf<Relic>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inventory_body_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy relic data
//        for (i in 1..5) {
//            var rarity = (3..5).random()
//            relicData.add(
//                Relic(
//                    relicSets.random(),
//                    "Hands",
//                    rarity,
//                    (0..15).random().coerceAtMost(rarity * 3),
//                    "ATK%",
//                    "40.0",
//                    mapOf(
//                        "ATK" to "2.0",
//                        "SPD" to "5.0",
//                        "DEF" to "8.5",
//                        "CRIT Rate" to "12.1"
//                    ),
//                    listOf(Relic.Status.LOCK)
//                )
//            )
//            rarity = (3..5).random()
//            relicData.add(
//                Relic(
//                    relicSets.random(),
//                    "Body",
//                    rarity,
//                    (0..15).random().coerceAtMost(rarity * 3),
//                    "DEF",
//                    "12.3",
//                    mapOf(
//                        "ATK" to "2.0",
//                        "SPD" to "5.0",
//                        "DEF" to "8.5",
//                    ),
//                    listOf(Relic.Status.TRASH)
//                )
//            )
//        }


        dbManager = (requireContext() as MainActivity).dbManager
        dbManager.open()

        // Example "payload"
        val relic_set = relicSets.random().name
        val relic_slot = "Hands"
        val relic_rarity = 5
        val relic_level = 13
        val relic_mainstat = "ATK%"
        val relic_mainstat_val = "11.1"
        val relic_status = "TRASH"
        val relic_substats = mapOf(
            "ATK" to "2.0",
            "SPD" to "5.0",
            "DEF" to "8.5",
            "CRIT Rate" to "12.1"
        )
        val relic_equipped = true

        // Insert inventory in DB
        dbManager.insertInventory(
            relic_set,
            relic_slot,
            relic_rarity,
            relic_level,
            relic_mainstat,
            relic_mainstat_val,
            relic_substats,
            relic_status,
            relic_equipped
        )

//        dbManager.close()

        // Fetch
        val cursor = dbManager.fetchRelic()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val relic_id = cursor.getLong(cursor.getColumnIndexOrThrow(InventoryDBHelper._ID))

                val statuses = mutableListOf<Relic.Status>()
                statuses.add(Relic.Status.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_STATUS))))

                if (cursor.getInt(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_EQUIPPED)) == 1) {
                    statuses.add(Relic.Status.EQUIPPED)
                }

                dbRelics.add(
                    Relic(
                        relic_id,
                        dbManager.getRelicSetByName(cursor.getString(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_SET))),
                        cursor.getString(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_SLOT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_RARITY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_LEVEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_MAINSTAT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_MAINSTAT_VAL)),
                        dbManager.fetchSubstatsForRelic(relic_id),
                        statuses,
                    )
                )
            }
        }

        cursor.close()

        // APPLY RULES OR SOMETHING
        dbRelics.forEach { prevRelic ->
            relicData.add(
                Relic(
                    prevRelic.id,
                    prevRelic.set,
                    prevRelic.slot,
                    prevRelic.rarity,
                    prevRelic.level,
                    prevRelic.mainstat,
                    prevRelic.mainstatVal,
                    prevRelic.substats,
                    dbManager.fetchStatusForRelic(prevRelic.id),
                    prevRelic
                )
            )
        }

        // Adapter
        val relicAdapter = RelicAdapter(relicData, this)

        val recyclerView: RecyclerView = view.findViewById(R.id.inventoryRecyclerView)
        recyclerView.layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.SPACE_AROUND
            flexWrap = FlexWrap.WRAP
        }
        recyclerView.adapter = relicAdapter
    }
}
