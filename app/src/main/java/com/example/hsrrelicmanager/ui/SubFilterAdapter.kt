package com.example.hsrrelicmanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.components.FilterItem

class SubFilterAdapter(private val items: FilterItem) : RecyclerView.Adapter<SubFilterAdapter.SubFilterViewHolder>() {

    class SubFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filterImage: ImageView = itemView.findViewById(R.id.chosenFilterImage)
        val filterText: TextView = itemView.findViewById(R.id.chosenFilterText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubFilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.relicset_chosen_filter, parent, false)
        return SubFilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubFilterViewHolder, position: Int) {
        val title = items.title

        if (title == "Relic Set") {
            val relicSet = items.RelicSet[position]
            holder.filterText.text = relicSet.name
            holder.filterImage.setImageResource(relicSet.icon)
        } else if (title == "Rarity") {
            val rarity = items.rarityList[position]
            holder.filterText.text = rarity
            (holder.filterText.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
            holder.filterImage.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return if (items.title == "Relic Set") {
            items.RelicSet.size
        } else {
            items.rarityList.size
        }
    }
}