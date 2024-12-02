package com.example.hsrrelicmanager.ui.rules

import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.group.FilterMap
import com.example.hsrrelicmanager.ui.MainActivity
import com.example.hsrrelicmanager.ui.rules.modals.AddLevelDialog
import com.example.hsrrelicmanager.ui.rules.modals.AddMainstatDialog
import com.example.hsrrelicmanager.ui.rules.modals.AddRarityDialog
import com.example.hsrrelicmanager.ui.rules.modals.AddSetDialog
import com.example.hsrrelicmanager.ui.rules.modals.AddSlotDialog
import com.example.hsrrelicmanager.ui.rules.modals.AddStatusDialog
import com.example.hsrrelicmanager.ui.rules.modals.AddSubstatDialog

class FilterAdapter(
    private val items: FilterMap,
    private val _callback: GroupChangeListener
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>(),
    GroupChangeListener by _callback {
    var isEditMode = false
    override fun onCancel() {
        if (isEditMode) {
            isEditMode = false
        } else {
            _callback.onCancel()
        }
    }
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

        val subAdapter = SubFilterAdapter(filterItem) {
            isEditMode = true
            showDialog(holder.entireView.context, filterItem)
        }
        holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recyclerView.adapter = subAdapter

        holder.entireView.setOnClickListener {
            showDialog(it.context, filterItem)
        }
    }
    private fun showDialog(context: Context, filter: Filter) {
        val mgr = (context as? androidx.fragment.app.FragmentActivity)
            ?.supportFragmentManager ?: return
        when (filter) {
            is Filter.SetFilter -> {
                AddSetDialog(filter.copy(), this).show(mgr, "AddSetDialog")
            }
            is Filter.SlotFilter -> {
                AddSlotDialog(filter.copy(), this).show(mgr, "AddSlotDialog")
            }
            is Filter.MainStatFilter -> {
                AddMainstatDialog(filter.copy(), this).show(mgr, "AddMainstatDialog")
            }
            is Filter.SubStatFilter -> {
                AddSubstatDialog(filter.copy(), this).show(mgr, "AddSubstatDialog")
            }
            is Filter.RarityFilter -> {
                AddRarityDialog(filter.copy(), this).show(mgr, "AddRarityDialog")
            }
            is Filter.LevelFilter -> {
                AddLevelDialog(filter.copy(), this).show(mgr, "AddLevelDialog")
            }
            is Filter.StatusFilter -> {
                AddStatusDialog(filter.copy(), this).show(mgr, "AddStatusDialog")
            }
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
