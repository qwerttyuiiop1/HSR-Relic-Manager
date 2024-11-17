package com.example.hsrrelicmanager.ui

import android.app.AlertDialog
import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.components.FilterItem

class FilterAdapter(private val items: MutableList<FilterItem>) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val choiceText: TextView = itemView.findViewById(R.id.item_filter_text)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.chosenFilterGroup)
        val entireView: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filterItem = items[position]
        val view = R.layout.item_filter

        holder.choiceText.text = filterItem.title

        val subAdapter = SubFilterAdapter(filterItem.RelicSet)
        holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recyclerView.adapter = subAdapter

        holder.entireView.setOnClickListener {
            showDialog(it.context, filterItem)
            Toast.makeText(it.context, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog(context: Context, filterItem: FilterItem) {

        val addSetDialog = AddSetDialog()
        val fragmentManager = (context as? androidx.fragment.app.FragmentActivity)?.supportFragmentManager
        fragmentManager?.let {
            addSetDialog.show(it, "AddSetDialog")
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
