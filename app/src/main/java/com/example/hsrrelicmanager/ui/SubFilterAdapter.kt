package com.example.hsrrelicmanager.ui

import android.annotation.SuppressLint
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

    @SuppressLint("SetTextI18n")
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
        } else if (title == "Level") {
            val level = items.levelNum
            val type = items.levelType
            if (type == true) {
                holder.filterText.text = "At Least $level"
            } else {
                holder.filterText.text = "At Most $level"
            }
            (holder.filterText.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
            holder.filterImage.visibility = View.GONE
        } else if (title == "Status") {
            val status = items.statusList[position]
            holder.filterText.text = status.name
            holder.filterImage.setImageResource(status.image)
        } else if (title == "Slot") {
            val slot = items.Slot[position]
            holder.filterText.text = "${slot.name}\t\t\tweight: ${slot.level}"
            (holder.filterText.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
            holder.filterImage.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return if (items.title == "Relic Set") {
            items.RelicSet.size
        } else if (items.title == "Rarity") {
            items.rarityList.size
        } else if (items.title == "Slot") {
            items.Slot.size
        } else if (items.title == "Level") {
            1
        } else if (items.title == "Status") {
            items.statusList.size
        } else {
            1
        }
    }
}