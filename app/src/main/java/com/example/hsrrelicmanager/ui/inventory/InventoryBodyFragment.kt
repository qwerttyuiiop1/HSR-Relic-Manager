package com.example.hsrrelicmanager.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.ActionPredictor
import com.example.hsrrelicmanager.ui.MainActivity
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class InventoryBodyFragment : Fragment() {
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

//        // Example "payload"
//        val relic_set = relicSets.random().name
//        val relic_slot = "Hands"
//        val relic_rarity = 5
//        val relic_level = 13
//        val relic_mainstat = "ATK%"
//        val relic_mainstat_val = "11.1"
//        val relic_status = "TRASH"
//        val relic_substats = mapOf(
//            "ATK" to "2.0",
//            "SPD" to "5.0",
//            "DEF" to "8.5",
//            "CRIT Rate" to "12.1"
//        )
//        val relic_equipped = true
//
//        // Insert inventory in DB
//        dbManager.insertInventory(
//            relic_set,
//            relic_slot,
//            relic_rarity,
//            relic_level,
//            relic_mainstat,
//            relic_mainstat_val,
//            relic_substats,
//            relic_status,
//            relic_equipped
//        )

//        dbManager.close()

//        dbManager.updateSubstatValues(5,
//            mapOf(
//                "ATK" to "3.0",
//                "SPD" to "11.0",
//                "DEF" to "1.2",
//                "CRIT Rate" to "0.8"
//            )
//        )

        // TEST: fetching from DB
//        for (relic in dbRelics) {
//            val found_id = dbManager.findRelicId(relic)
//            Log.d("InventoryBodyFragment", "Found id $found_id for relic $relic")
//
//            if (relic.id == found_id) {
//                Log.d("InventoryBodyFragment", "id matches with relic id!")
//            } else {
//                throw IllegalStateException("found_id $found_id does not match relic id ${relic.id}")
//            }
//        }

        // APPLY RULES OR SOMETHING
//        dbRelics.forEach { prevRelic ->
//            relicData.add(
//                Relic(
//                    prevRelic.id,
//                    prevRelic.set,
//                    prevRelic.slot,
//                    prevRelic.rarity,
//                    prevRelic.level,
//                    prevRelic.mainstat,
//                    prevRelic.mainstatVal,
//                    prevRelic.substats,
//                    dbManager.fetchStatusForRelic(prevRelic.id),
////                    prevRelic
//                )
//            )
//        }

        val mainActivity = requireContext() as MainActivity
        relicData.clear()
        relicData.addAll(mainActivity.cachedRelics)
        // Adapter
        val predictor = ActionPredictor(
            mainActivity.cachedGroupData,
            mainActivity.cachedManualStatus
        )
        val relicAdapter = RelicAdapter(relicData, predictor, this)

        val recyclerView: RecyclerView = view.findViewById(R.id.inventoryRecyclerView)
        recyclerView.layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.SPACE_AROUND
            flexWrap = FlexWrap.WRAP
        }
        recyclerView.adapter = relicAdapter
    }
}
