package com.example.hsrrelicmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
            relicData.add(Relic(
                "Musketeer of Wild Wheat",
                "Hands",
                5,
                15,
                "ATK%",
                40.0,
                mapOf(
                    "ATK" to 2.0,
                    "SPD" to 5.0,
                    "DEF" to 8.5,
                    "CRIT Rate" to 12.1
                ),
                Relic.Status.LOCK,
                R.drawable.musketeer_of_wild_wheat
            ))
            relicData.add(Relic(
                "Thief of Shooting Meteor",
                "Body",
                2,
                12,
                "DEF",
                12.3,
                mapOf(
                    "ATK" to 2.0,
                    "SPD" to 5.0,
                    "DEF" to 8.5,
                ),
                Relic.Status.TRASH,
                R.drawable.thief_of_shooting_meteor
            ))
        }
        val relicAdapter = RelicAdapter(relicData)

        val recyclerView: RecyclerView = view.findViewById(R.id.inventoryRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = relicAdapter
    }
}
