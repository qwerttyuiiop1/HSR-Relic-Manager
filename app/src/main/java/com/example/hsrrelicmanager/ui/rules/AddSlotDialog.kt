package com.example.hsrrelicmanager.ui.rules

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
import com.example.hsrrelicmanager.databinding.DialogSlotFilterBinding
import com.example.hsrrelicmanager.databinding.ItemSlotRowBinding
import com.example.hsrrelicmanager.model.Slot
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.slotSets
import com.example.hsrrelicmanager.ui.MainActivity

class SlotboxAdapter(
    val sets: List<Slot>,
    val selectedSlots: MutableList<Slot>,
): RecyclerView.Adapter<SlotboxAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemSlotRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(set: Slot) {
            binding.apply {

                icon.setImageResource(set.image)

                checkbox.setOnCheckedChangeListener{_, isChecked ->
                    if (isChecked) {
                        if (!selectedSlots.contains(set)){
                            selectedSlots.add(set)
                            sortSelectedSets()
                        }
                    } else {
                        selectedSlots.remove(set)
                    }
                }

                container.setOnClickListener {
                    checkbox.isChecked = !checkbox.isChecked
                }
                checkbox.isChecked = selectedSlots.contains(set)

                name.text = set.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSlotRowBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = sets.size

    private fun sortSelectedSets() {
        selectedSlots.sortWith(compareBy { sets.indexOf(it) })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sets[position])
    }
}

class AddSlotDialog(
    private val items: Filter.SlotFilter,
    private val callback: GroupChangeListener,
): DialogFragment() {

    val binding: DialogSlotFilterBinding by lazy {
        DialogSlotFilterBinding.inflate(
            layoutInflater, null, false
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val selectedSlots = items.types.toMutableList()

        binding.apply {
            val adapter = SlotboxAdapter(slotSets, selectedSlots)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            val content = SpannableString("Deselect All")
            content.setSpan(
                UnderlineSpan(), 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            btnDeselectAll.text = content
            btnDeselectAll.setOnClickListener {
                adapter.selectedSlots.clear()
                adapter.notifyDataSetChanged()
            }

            cancelActionGroupDialogButton.setOnClickListener {
                adapter.selectedSlots.clear()
                callback.onCancel()
                dismiss()
            }

            confirmActionGroupDialogButton.setOnClickListener {
                val items = Filter.SlotFilter(adapter.selectedSlots.toMutableSet())
                callback.onAddFilter(items)
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