package com.example.hsrrelicmanager.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.rules.Filter

class SubFilterAdapter(private val items: Filter) : RecyclerView.Adapter<SubFilterAdapter.SubFilterViewHolder>() {

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
//        when (items) {
//            is Filter.SetFilter -> {
//                val relicSet = items.sets[position]
//                holder.filterText.text = relicSet.name
//                holder.filterImage.setImageResource(relicSet.icon)
//            }
//            is Filter.SlotFilter -> {
//                val slot = items.types[position]
//                holder.filterText.text = slot.name
//                holder.filterImage.setImageResource(slot.image)
//            }
//            is Filter.MainStatFilter -> {
//                val mainstat = items.stats[position]
//                holder.filterText.text = mainstat.name
//                holder.filterImage.setImageResource(mainstat.image)
//            }
//            is Filter.SubStatFilter -> {
//                val substat = items.stats[position]
//                holder.filterText.text = substat.name
//                holder.weightText.text = substat.level.toString()
//                holder.weightText.visibility = View.VISIBLE
//                holder.filterImage.setImageResource(substat.image)
//            }
//            is Filter.RarityFilter -> {
//                val rarity = items.rarities[position]
//                holder.filterText.text = "${rarity} Star"
//                (holder.filterText.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
//                holder.filterImage.visibility = View.GONE
//            }
//            is Filter.LevelFilter -> {
//                val atLeast = items.atLeast
//                val atMost = items.atMost
//                if (atLeast != null) {
//                    holder.filterText.text = "At Least $atLeast"
//                } else {
//                    holder.filterText.text = "At Most $atMost"
//                }
//                (holder.filterText.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
//                holder.filterImage.visibility = View.GONE
//            }
//            is Filter.StatusFilter -> {
//                val status = items.statuses[position]
//                holder.filterText.text = status.name
//                holder.filterImage.setImageResource(status.image)
//            }
//        }
    }

    override fun getItemCount(): Int {
        return when (items) {
            is Filter.SetFilter -> items.sets.size
            is Filter.SlotFilter -> items.types.size
            is Filter.MainStatFilter -> items.stats.size
            is Filter.SubStatFilter -> items.stats.size
            is Filter.RarityFilter -> items.rarities.size
            is Filter.LevelFilter -> 1
            is Filter.StatusFilter -> items.statuses.size
            else -> 0
        }
    }
}