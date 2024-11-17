package com.example.hsrrelicmanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
        val relicSet = items.RelicSet[position]

        if (relicSet != null) {
            holder.filterText.text = relicSet.name
            holder.filterImage.setImageResource(relicSet.icon)
        }
    }

    override fun getItemCount(): Int {
        return items.RelicSet.size
    }
}