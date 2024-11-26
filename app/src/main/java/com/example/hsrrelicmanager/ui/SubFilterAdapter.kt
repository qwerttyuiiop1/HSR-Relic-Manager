package com.example.hsrrelicmanager.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.FilterItem

class SubFilterAdapter(private val items: FilterItem) : RecyclerView.Adapter<SubFilterAdapter.SubFilterViewHolder>() {

    class SubFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filterImage: ImageView = itemView.findViewById(R.id.chosenFilterImage)
        val filterText: TextView = itemView.findViewById(R.id.chosenFilterText)
        val weightText: TextView = itemView.findViewById(R.id.weightFilterText)
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
        }
        else if (title == "Slot"){
            val slot = items.Slot[position]
            holder.filterText.text = slot.name
            holder.filterImage.setImageResource(slot.image)
        }
        else if (title == "Mainstat") {
            val mainstat = items.Mainstat[position]
            holder.filterText.text = mainstat.name
            holder.filterImage.setImageResource(mainstat.image)
        }
        else if (title == "Substat") {
            val substat = items.Substat[position]
            holder.filterText.text = substat.name
            holder.weightText.text = substat.level.toString()
            holder.weightText.visibility = View.VISIBLE
            holder.filterImage.setImageResource(substat.image)
        }
        else if (title == "Rarity") {
            val rarity = items.rarityList[position]
            holder.filterText.text = "${rarity} Star"
            (holder.filterText.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
            holder.filterImage.visibility = View.GONE
        }
        else if (title == "Level") {
            val level = items.levelNum
            val type = items.levelType
            if (type == true) {
                holder.filterText.text = "At Least $level"
            } else {
                holder.filterText.text = "At Most $level"
            }
            (holder.filterText.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
            holder.filterImage.visibility = View.GONE
        }
        else if (title == "Status") {
            val status = items.statusList[position]
            holder.filterText.text = status.name
            holder.filterImage.setImageResource(status.image)
        }
    }

    override fun getItemCount(): Int {
        return if (items.title == "Relic Set") {
            items.RelicSet.size
        }
        else if (items.title == "Slot") {
            items.Slot.size
        }
        else if (items.title == "Mainstat") {
            items.Mainstat.size
        }
        else if (items.title == "Status") {
            items.statusList.size
        }
        else if (items.title == "Rarity") {
            items.rarityList.size
        }
        else if (items.title == "Substat") {
            items.Substat.size
        }
        else if (items.title == "Level") {
            1
        }
        else {
            1
        }
    }
}