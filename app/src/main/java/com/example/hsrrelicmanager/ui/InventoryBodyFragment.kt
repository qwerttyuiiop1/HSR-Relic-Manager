package com.example.hsrrelicmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.relicSets
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class InventoryBodyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inventory_body_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy relic data
        val relicData = mutableListOf<Relic>()
        for (i in 1..50) {
            var level = (3..5).random()
            relicData.add(
                Relic(
                relicSets.random(),
                "Hands",
                level,
                (0..15).random().coerceAtMost(level * 3),
                "ATK%",
                "40.0",
                mapOf(
                    "ATK" to "2.0",
                    "SPD" to "5.0",
                    "DEF" to "8.5",
                    "CRIT Rate" to "12.1"
                ),
                listOf(Relic.Status.LOCK)
            )
            )
            level = (3..5).random()
            relicData.add(
                Relic(
                    relicSets.random(),
                "Body",
                    level,
                    (0..15).random().coerceAtMost(level * 3),
                "DEF",
                "12.3",
                mapOf(
                    "ATK" to "2.0",
                    "SPD" to "5.0",
                    "DEF" to "8.5",
                ),
                listOf(Relic.Status.TRASH)
            )
            )
        }
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
