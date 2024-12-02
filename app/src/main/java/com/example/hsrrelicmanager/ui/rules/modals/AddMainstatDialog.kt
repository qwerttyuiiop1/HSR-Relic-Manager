package com.example.hsrrelicmanager.ui.rules.modals

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
import com.example.hsrrelicmanager.databinding.DialogMainstatFilterBinding
import com.example.hsrrelicmanager.databinding.ItemMainstatRowBinding
import com.example.hsrrelicmanager.model.Mainstat
import com.example.hsrrelicmanager.model.mainstatSets
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.ui.MainActivity
import com.example.hsrrelicmanager.ui.rules.GroupChangeListener

class MainstatboxAdapter(
    val sets: List<Mainstat>,
    selectedMainstats: List<Mainstat>,
): RecyclerView.Adapter<MainstatboxAdapter.ViewHolder>() {
    val selectedMainstats = selectedMainstats.toMutableList()
    inner class ViewHolder(val binding: ItemMainstatRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(set: Mainstat) {
            binding.apply {

                icon.setImageResource(set.image)

                checkbox.setOnCheckedChangeListener{_, isChecked ->
                    if (isChecked) {
                        if (!selectedMainstats.contains(set)){
                            selectedMainstats.add(set)
                            sortSelectedSets()
                        }
                    } else {
                        selectedMainstats.remove(set)
                    }
                }

                container.setOnClickListener {
                    checkbox.isChecked = !checkbox.isChecked
                }
                checkbox.isChecked = selectedMainstats.contains(set)

                name.text = set.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainstatRowBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = sets.size

    private fun sortSelectedSets() {
        selectedMainstats.sortWith(compareBy { sets.indexOf(it) })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sets[position])
    }
}

class AddMainstatDialog(
    private val items: Filter.MainStatFilter,
    private val callback: GroupChangeListener,
): DialogFragment() {

    val binding: DialogMainstatFilterBinding by lazy {
        DialogMainstatFilterBinding.inflate(
            layoutInflater, null, false
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val selectedMainstats = items.stats.toMutableList()
//        var index = items.indexOfFirst { it.title == "Mainstat" }
//        val selectedMainstats = if (index != -1) items[index].Mainstat else mutableListOf<Mainstat>()
//        val selectedMainstatsCopy = selectedMainstats.toMutableList()

        binding.apply {
            val adapter = MainstatboxAdapter(mainstatSets, selectedMainstats)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            val content = SpannableString("Deselect All")
            content.setSpan(
                UnderlineSpan(), 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            btnDeselectAll.text = content
            btnDeselectAll.setOnClickListener {
                adapter.selectedMainstats.clear()
                adapter.notifyDataSetChanged()
            }

            cancelActionGroupDialogButton.setOnClickListener {
                adapter.selectedMainstats.clear()
                callback.onCancel()
                dismiss()
            }

            confirmActionGroupDialogButton.setOnClickListener {
                val items = Filter.MainStatFilter(adapter.selectedMainstats.toMutableSet())
                callback.onUpdateFilter(items)
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