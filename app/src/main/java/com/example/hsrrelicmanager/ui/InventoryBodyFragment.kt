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


        dbManager = InventoryDBManager(requireContext())
        dbManager.open()

        // Example "payload"
        val relic_set = relicSets.random().name
        val relic_slot = "Hands"
        val relic_rarity = 5
        val relic_level = 15
        val relic_mainstat = "ATK%"
        val relic_mainstat_val = "11.1"
        val relic_status = listOf(
            "TRASH",
            "UPGRADE"
        )
        val relic_substats = mapOf(
            "ATK" to "2.0",
            "SPD" to "5.0",
            "DEF" to "8.5",
        )

        // Insert inventory in DB
        dbManager.insertInventory(
            relic_set,
            relic_slot,
            relic_rarity,
            relic_level,
            relic_mainstat,
            relic_mainstat_val,
            relic_substats,
            relic_status
        )

//        dbManager.close()

        // Fetch
        val cursor = dbManager.fetchRelic()
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                relicData.add(
                    Relic(
                        dbManager.getRelicSetByName(cursor.getString(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_SET))),
                        cursor.getString(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_SLOT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_RARITY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_LEVEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_MAINSTAT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(InventoryDBHelper.COLUMN_MAINSTAT_VAL)),
                        dbManager.fetchSubstatsForRelic(cursor.getLong(cursor.getColumnIndexOrThrow(InventoryDBHelper._ID))),
                        dbManager.fetchStatusForRelic(cursor.getLong(cursor.getColumnIndexOrThrow(InventoryDBHelper._ID))),
                    )
                )
            }
        }
        cursor.close()
        dbManager.close()

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
