package com.example.hsrrelicmanager.ui

import android.content.DialogInterface
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.components.FilterItem
import com.example.hsrrelicmanager.databinding.DialogSetFilterBinding
import com.example.hsrrelicmanager.databinding.ItemRelicSetRowBinding
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.relics.relicSets

class RelicCheckboxAdapter(
    val sets: List<RelicSet>,
    val selectedSets: MutableList<RelicSet>,
): RecyclerView.Adapter<RelicCheckboxAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRelicSetRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(set: RelicSet) {
            binding.apply {
                checkbox.setOnCheckedChangeListener{_, isChecked ->
                    if (isChecked) {
                        if (!selectedSets.contains(set)){
                            selectedSets.add(set)
                            sortSelectedSets()
                        }
                    } else {
                        selectedSets.remove(set)
                    }
                }
                container.setOnClickListener {
                    checkbox.isChecked = !checkbox.isChecked
                }
                checkbox.isChecked = selectedSets.contains(set)

                icon.setImageResource(set.icon)
                name.text = set.name
                description.text = set.description

                description.visibility = View.GONE
                btnMinimize.visibility = View.GONE
                btnMaximize.visibility = View.VISIBLE

                btnMaximize.setOnClickListener {
                    description.visibility = View.VISIBLE
                    btnMinimize.visibility = View.VISIBLE
                    btnMaximize.visibility = View.GONE
                }
                btnMinimize.setOnClickListener {
                    description.visibility = View.GONE
                    btnMinimize.visibility = View.GONE
                    btnMaximize.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRelicSetRowBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = sets.size

    private fun sortSelectedSets() {
        selectedSets.sortWith(compareBy { sets.indexOf(it) })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sets[position])
    }
}

class AddSetDialog(private val items: MutableList<FilterItem>): DialogFragment() {

    val binding: DialogSetFilterBinding by lazy {
        DialogSetFilterBinding.inflate(
            layoutInflater, null, false
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        var index = -1
        index = items.indexOfFirst { it.title == "Relic Set" }
        val relicSetList = if (index != -1) items[index].RelicSet else mutableListOf()

        binding.apply {
            val adapter = RelicCheckboxAdapter(relicSets, relicSetList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            val content = SpannableString("Deselect All")
            content.setSpan(
                UnderlineSpan(), 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            btnDeselectAll.text = content
            btnDeselectAll.setOnClickListener {
                adapter.selectedSets.clear()
                adapter.notifyDataSetChanged()
            }

            cancelActionGroupDialogButton.setOnClickListener {
                val addFilterDialog = AddFilterDialog(items)
                addFilterDialog.show(requireActivity().supportFragmentManager, "AddSetDialog")
                dismiss()
            }

            confirmActionGroupDialogButton.setOnClickListener {
                requireActivity().supportFragmentManager.setFragmentResult(
                    "selectedSets",
                    Bundle().apply {
                        putParcelableArrayList("selectedSets", ArrayList(adapter.selectedSets))
                    }
                )
                dismiss()
            }
        }
        val activity = context as MainActivity
        val bgView = activity.findViewById<View>(R.id.activity_main_layout)
        bgView.setRenderEffect(
            RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
        )

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val layoutParams = window.attributes

            val displayMetrics = requireContext().resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels

            layoutParams.width = screenWidth - 200
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            window.attributes = layoutParams
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Remove the blur effect on dismissal
        val bgView = requireActivity().findViewById<View>(R.id.activity_main_layout)
        bgView.setRenderEffect(null)
    }
}