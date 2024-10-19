package com.example.hsrrelicmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
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
            relicData.add(Relic(
                "Musketeer of Wild Wheat",
                "Hands",
                5,
                15,
                "ATK%",
                "40.0",
                mapOf(
                    "ATK" to "2.0",
                    "SPD" to "5.0",
                    "DEF" to "8.5",
                    "CRIT Rate" to "12.1"
                ),
                listOf(Relic.Status.LOCK),
                R.drawable.musketeer_of_wild_wheat
            ))
            relicData.add(Relic(
                "Thief of Shooting Meteor",
                "Body",
                2,
                0,
                "DEF",
                "12.3",
                mapOf(
                    "ATK" to "2.0",
                    "SPD" to "5.0",
                    "DEF" to "8.5",
                ),
                listOf(Relic.Status.TRASH),
                R.drawable.thief_of_shooting_meteor
            ))
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
