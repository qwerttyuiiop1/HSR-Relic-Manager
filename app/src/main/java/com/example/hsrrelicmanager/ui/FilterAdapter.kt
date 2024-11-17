package com.example.hsrrelicmanager.ui

import android.app.AlertDialog
import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.components.FilterItem

class FilterAdapter(private val items: MutableList<FilterItem>) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    private var selectedOption: FilterItem? = null

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val choiceText: TextView = itemView.findViewById(R.id.item_filter_text)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.chosenFilterGroup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false) // Inflate the new layout
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filterItem = items[position]

        holder.choiceText.text = filterItem.title

        val subAdapter = SubFilterAdapter(filterItem.RelicSet)
        holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recyclerView.adapter = subAdapter

        holder.choiceText.setOnClickListener {
            selectedOption = filterItem
            showDialog(it.context, filterItem)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showDialog(context: Context, filterItem: FilterItem) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_buttons, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)

        val dialog = builder.create()

        val activity = context as MainActivity
        val bgView = activity.findViewById<View>(R.id.activity_main_layout)
        bgView.setRenderEffect(
            RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
        )

        dialog.show()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
