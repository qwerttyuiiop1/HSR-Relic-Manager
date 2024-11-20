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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.components.FilterItem
import com.example.hsrrelicmanager.databinding.DialogSubstatFilterBinding
import com.example.hsrrelicmanager.databinding.ItemSubstatRowBinding
import com.example.hsrrelicmanager.model.Substat
import com.example.hsrrelicmanager.model.substatSets

class SubstatboxAdapter(
    val sets: List<Substat>,
    val selectedSubstats: MutableList<Substat>,
): RecyclerView.Adapter<SubstatboxAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemSubstatRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(set: Substat) {
            binding.apply {
                var level = set.level

                if (!selectedSubstats.contains(set)) {
                    set.level = 1
                }

                checkbox.setOnCheckedChangeListener{_, isChecked ->
                    if (isChecked) {
                        if (!selectedSubstats.contains(set)){
                            selectedSubstats.add(set)
                            sortSelectedSets()
                        }
                    } else {
                        selectedSubstats.remove(set)
                    }
                }

                subtractLevel.setOnClickListener {
                    if (level > 1) {
                        level--
                        levelNumber.text = level.toString()
                        set.level = level
                    }
                }

                addLevel.setOnClickListener {
                    level++
                    levelNumber.text = level.toString()
                    set.level = level
                }



                container.setOnClickListener {
                    checkbox.isChecked = !checkbox.isChecked
                }
                checkbox.isChecked = selectedSubstats.contains(set)
                levelNumber.text = sets[sets.indexOf(set)].level.toString()

                name.text = set.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubstatRowBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = sets.size

    private fun sortSelectedSets() {
        selectedSubstats.sortWith(compareBy { sets.indexOf(it) })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sets[position])
    }
}

class AddSubstatDialog(private val items: MutableList<FilterItem>): DialogFragment() {

    val binding: DialogSubstatFilterBinding by lazy {
        DialogSubstatFilterBinding.inflate(
            layoutInflater, null, false
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        var index = -1
        index = items.indexOfFirst { it.title == "Substat" }
        val selectedSubstats = if (index != -1) items[index].Substat else mutableListOf<Substat>()
        val selectedSubstatsCopy = selectedSubstats.toMutableList()

        binding.apply {
            val adapter = SubstatboxAdapter(substatSets, selectedSubstats)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            var level = weightNumber.text.toString().toInt()
            Toast.makeText(context, level.toString(), Toast.LENGTH_SHORT).show()

            radioButtonExact.setOnClickListener {
                radioButtonExact.setImageResource(R.drawable.ic_radio_button_checked)
                radioButtonWeight.setImageResource(R.drawable.ic_radio_button_unchecked)
            }

            radioButtonWeight.setOnClickListener {
                radioButtonExact.setImageResource(R.drawable.ic_radio_button_unchecked)
                radioButtonWeight.setImageResource(R.drawable.ic_radio_button_checked)
            }

            weightSubtractLevel.setOnClickListener {
                level--
                weightNumber.text = level.toString()
            }

            weightAddLevel.setOnClickListener {
                level++
                weightNumber.text = level.toString()
            }


            val content = SpannableString("Deselect All")
            content.setSpan(
                UnderlineSpan(), 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            btnDeselectAll.text = content
            btnDeselectAll.setOnClickListener {
                adapter.selectedSubstats.clear()
                adapter.notifyDataSetChanged()
            }

            cancelActionGroupDialogButton.setOnClickListener {
                adapter.selectedSubstats.clear()
                if (index == -1){
                    val addFilterDialog = AddFilterDialog(items)
                    addFilterDialog.show(requireActivity().supportFragmentManager, "AddSetDialog")
                }

                requireActivity().supportFragmentManager.setFragmentResult(
                    "substat",
                    Bundle().apply {
                        putParcelableArrayList("substat", ArrayList(selectedSubstatsCopy))
                    }
                )
                dismiss()
            }

            confirmActionGroupDialogButton.setOnClickListener {

                if (adapter.selectedSubstats.isEmpty() && index != -1) {
                    items.removeAt(index)
                }

                requireActivity().supportFragmentManager.setFragmentResult(
                    "substat",
                    Bundle().apply {
                        putParcelableArrayList("substat", ArrayList(adapter.selectedSubstats))
                    }
                )
                dismiss()
                index = items.indexOfFirst { it.title == "Substat" }
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