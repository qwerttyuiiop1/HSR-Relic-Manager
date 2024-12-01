package com.example.hsrrelicmanager.ui.rules

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.Mainstat
import com.example.hsrrelicmanager.model.Slot
import com.example.hsrrelicmanager.model.Status
import com.example.hsrrelicmanager.model.Substat
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.rules.Filter

class SubFilterAdapter(
    private val items: Filter,
    private val onClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<SubFilterAdapter.SubFilterViewHolder>() {

    var data: List<*> = onUpdateData()

    fun onUpdateData(): List<*> {
        data = when (items) {
            is Filter.SetFilter -> items.sets.sortedBy { it.name }
            is Filter.SlotFilter -> items.types.sortedBy { it.name }
            is Filter.MainStatFilter -> items.stats.sortedBy { it.name }
            is Filter.SubStatFilter -> items.stats.toList().sortedBy { it.first.name }
            is Filter.RarityFilter -> items.rarities.sortedBy { it }
            is Filter.StatusFilter -> items.statuses.sortedBy { it.name }
            is Filter.LevelFilter -> listOf(Unit)
        }
        return data
    }
    inner class SubFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        when (items) {
            is Filter.SetFilter -> {
                val relicSet = data[position] as RelicSet
                holder.filterText.text = relicSet.name
                holder.filterImage.setImageResource(relicSet.icon)
            }
            is Filter.SlotFilter -> {
                val slot = data[position] as Slot
                holder.filterText.text = slot.name
                holder.filterImage.setImageResource(slot.image)
            }
            is Filter.MainStatFilter -> {
                val mainstat = data[position] as Mainstat
                holder.filterText.text = mainstat.name
                holder.filterImage.setImageResource(mainstat.image)
            }
            is Filter.SubStatFilter -> {
                val substat = data[position] as Pair<Substat, Int>
                holder.filterText.text = substat.first.name
                holder.weightText.text = substat.second.toString()
                holder.weightText.visibility = View.VISIBLE
                holder.filterImage.setImageResource(substat.first.image)
            }
            is Filter.RarityFilter -> {
                val rarity = data[position] as Int
                holder.filterText.text = "${rarity} Star"
                (holder.filterText.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
                holder.filterImage.visibility = View.GONE
            }
            is Filter.LevelFilter -> {
                val atLeast = items.atLeast
                val atMost = items.atMost
                if (atLeast != null) {
                    holder.filterText.text = "At Least $atLeast"
                } else {
                    holder.filterText.text = "At Most $atMost"
                }
                (holder.filterText.layoutParams as ViewGroup.MarginLayoutParams).marginStart = 0
                holder.filterImage.visibility = View.GONE
            }
            is Filter.StatusFilter -> {
                val status = data[position] as Status
                holder.filterText.text = status.name
                holder.filterImage.setImageResource(status.image)
            }
        }
        holder.itemView.setOnClickListener {
            onClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}