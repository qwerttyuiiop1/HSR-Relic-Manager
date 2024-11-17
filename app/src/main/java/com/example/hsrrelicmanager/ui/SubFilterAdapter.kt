package com.example.hsrrelicmanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.relics.RelicSet

class SubFilterAdapter(private val item: MutableList<RelicSet>) : RecyclerView.Adapter<SubFilterAdapter.SubFilterViewHolder>() {

    class SubFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val FilterImage: ImageView = itemView.findViewById(R.id.chosenFilterImage)
        val FilterText: TextView = itemView.findViewById(R.id.chosenFilterText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubFilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.relicset_chosen_filter, parent, false) // Inflate the new layout
        return SubFilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubFilterViewHolder, position: Int) {

        val relic = item[position]

        holder.FilterText.text = relic.name
        holder.FilterImage.setImageResource(relic.icon)
    }
    override fun getItemCount(): Int {
        return 1
    }
}