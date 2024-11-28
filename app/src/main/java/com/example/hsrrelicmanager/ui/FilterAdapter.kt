package com.example.hsrrelicmanager.ui

import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.group.FilterMap

class FilterAdapter(
    private val items: FilterMap,
    private val callback: AddFilterListener
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {
    private val types = Filter.Type.entries.toTypedArray()

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val choiceText: TextView = itemView.findViewById(R.id.item_filter_text)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.chosenFilterGroup)
        val substatText: TextView = itemView.findViewById(R.id.substats_subtext)
        val entireView: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        var _filterItem: Filter? = null
        var i = 0
        for (item in types) {
            if (item in items) {
                if (i == position) {
                    _filterItem = items[item]!!
                    break
                }
                i++
            }
        }
        val filterItem = _filterItem!!


        holder.choiceText.text = filterItem.name

        if (filterItem is Filter.SubStatFilter) {
            holder.substatText.visibility = View.VISIBLE
            val weightLevel = filterItem.minWeight
            if (weightLevel != -1) {
                holder.substatText.text = "(>= ${weightLevel})"
            }
            else{
                holder.substatText.text = "(exact)"
            }
        }

        val subAdapter = SubFilterAdapter(filterItem)
        holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recyclerView.adapter = subAdapter

        holder.entireView.setOnClickListener {
            showDialog(it.context, filterItem)
        }
    }

    private fun showDialog(context: Context, filter: Filter) {

        if (filterBuilder.title == "Relic Set") {
            val addSetDialog = AddSetDialog(items)
            val fragmentManager = (context as? androidx.fragment.app.FragmentActivity)?.supportFragmentManager
            fragmentManager?.let {
                addSetDialog.show(it, "AddSetDialog")
            }
        }
        else if (filterBuilder.title == "Slot") {
            val addSlotDialog = AddSlotDialog(items)
            val fragmentManager = (context as? androidx.fragment.app.FragmentActivity)?.supportFragmentManager
            fragmentManager?.let {
                addSlotDialog.show(it, "AddSlotDialog")
            }
        }
        else if (filterBuilder.title == "Mainstat") {
            val addMainstatDialog = AddMainstatDialog(items)
            val fragmentManager = (context as? androidx.fragment.app.FragmentActivity)?.supportFragmentManager
            fragmentManager?.let {
                addMainstatDialog.show(it, "AddMainstatDialog")
            }
        }
        else if (filterBuilder.title == "Substat") {
            val addSubstatDialog = AddSubstatDialog(items)
            val fragmentManager = (context as? androidx.fragment.app.FragmentActivity)?.supportFragmentManager
            fragmentManager?.let {
                addSubstatDialog.show(it, "AddSubstatDialog")
            }
        }
        else if (filterBuilder.title == "Rarity") {
            val addRarityDialog = AddRarityDialog(items)
            val fragmentManager = (context as? androidx.fragment.app.FragmentActivity)?.supportFragmentManager
            fragmentManager?.let {
                addRarityDialog.show(it, "AddRarityDialog")
            }
        }
        else if (filterBuilder.title == "Level") {
            val addLevelDialog = AddLevelDialog(items)
            val fragmentManager = (context as? androidx.fragment.app.FragmentActivity)?.supportFragmentManager
            fragmentManager?.let {
                addLevelDialog.show(it, "AddLevelDialog")
            }
        }
        else if (filterBuilder.title == "Status") {
            val addStatusDialog = AddStatusDialog(items)
            val fragmentManager = (context as? androidx.fragment.app.FragmentActivity)?.supportFragmentManager
            fragmentManager?.let {
                addStatusDialog.show(it, "AddStatusDialog")
            }
        }
        else {
            Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }


        val activity = context as MainActivity
        val bgView = activity.findViewById<View>(R.id.activity_main_layout)
        bgView.setRenderEffect(
            RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
