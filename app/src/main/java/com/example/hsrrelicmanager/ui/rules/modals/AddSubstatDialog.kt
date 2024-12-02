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
import com.example.hsrrelicmanager.databinding.DialogSubstatFilterBinding
import com.example.hsrrelicmanager.databinding.ItemSubstatRowBinding
import com.example.hsrrelicmanager.model.relics.Substat
import com.example.hsrrelicmanager.model.relics.substatSets
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.ui.MainActivity
import com.example.hsrrelicmanager.ui.rules.GroupChangeListener

class SubstatboxAdapter(
    val sets: List<Substat>,
    selectedSubstats: Map<Substat, Int>
): RecyclerView.Adapter<SubstatboxAdapter.ViewHolder>() {
    val selectedSubstats = selectedSubstats.keys.toMutableSet()
    val levels = sets.associateWith {
        if (it in selectedSubstats) selectedSubstats[it]!! else 1
    }.toMutableMap()
    inner class ViewHolder(val binding: ItemSubstatRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(set: Substat) {
            binding.apply {
                icon.setColorFilter(android.graphics.Color.parseColor("#4A4A4A"))
                icon.setImageResource(set.image)

                checkbox.setOnCheckedChangeListener{_, isChecked ->
                    if (isChecked) {
                        selectedSubstats.add(set)
                    } else {
                        selectedSubstats.remove(set)
                    }
                }

                subtractLevel.setOnClickListener {
                    val level = levels[set]!!
                    if (level > 1) {
                        levels[set] = level - 1
                        levelNumber.text = levels[set].toString()
                    }
                }

                addLevel.setOnClickListener {
                    levels[set] = levels[set]!! + 1
                    levelNumber.text = levels[set].toString()
                }

                container.setOnClickListener {
                    checkbox.isChecked = !checkbox.isChecked
                }
                checkbox.isChecked = selectedSubstats.contains(set)
                levelNumber.text = levels[set].toString()

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

//    private fun sortSelectedSets() {
//        selectedSubstats.sortWith(compareBy { sets.indexOf(it) })
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sets[position])
    }
}

class AddSubstatDialog(
    private val items: Filter.SubStatFilter,
    private val callback: GroupChangeListener,
): DialogFragment() {

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

        var isExact = true
        val origWeight = items.minWeight
        val selectedSubstats = items.stats.toMutableMap()

        if (origWeight != -1) {
            binding.radioButtonExact.setImageResource(R.drawable.ic_radio_button_unchecked)
            binding.radioButtonWeight.setImageResource(R.drawable.ic_radio_button_checked)
            binding.weightNumber.text = origWeight.toString()
            isExact = false
        }

        binding.apply {
            val adapter = SubstatboxAdapter(substatSets, selectedSubstats)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            var level = weightNumber.text.toString().toInt()

            radioButtonExact.setOnClickListener {
                radioButtonExact.setImageResource(R.drawable.ic_radio_button_checked)
                radioButtonWeight.setImageResource(R.drawable.ic_radio_button_unchecked)
                isExact = true
            }

            radioButtonWeight.setOnClickListener {
                radioButtonExact.setImageResource(R.drawable.ic_radio_button_unchecked)
                radioButtonWeight.setImageResource(R.drawable.ic_radio_button_checked)
                isExact = false
            }

            weightSubtractLevel.setOnClickListener {
                if (level > 3) {
                    level--
                    weightNumber.text = level.toString()
                }
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
                adapter.notifyDataSetChanged()
                dismiss()
            }

            confirmActionGroupDialogButton.setOnClickListener {
                val substats = adapter.selectedSubstats.map {
                    it to adapter.levels[it]!!
                }.toMap()
                val items = Filter.SubStatFilter(substats, if (isExact) -1 else level)
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