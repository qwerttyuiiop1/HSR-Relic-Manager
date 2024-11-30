package com.example.hsrrelicmanager.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.DialogRarityFilterBinding
import com.example.hsrrelicmanager.model.rules.Filter

class AddRarityDialog(
    private val items: Filter.RarityFilter,
    private val callback: GroupChangeListener
): DialogFragment() {

    val binding: DialogRarityFilterBinding by lazy {
        DialogRarityFilterBinding.inflate(
            layoutInflater, null, false
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val rarity = items.rarities.toMutableSet()
        val checks = arrayOf(
            binding.check1,
            binding.check2,
            binding.check3,
            binding.check4,
            binding.check5
        )
        for (i in 0..4) {
            checks[i].isChecked = i + 1 in rarity
        }

        binding.apply {
            container1.setOnClickListener {
                check1.isChecked = !check1.isChecked
            }
            container2.setOnClickListener {
                check2.isChecked = !check2.isChecked
            }
            container3.setOnClickListener {
                check3.isChecked = !check3.isChecked
            }
            container4.setOnClickListener {
                check4.isChecked = !check4.isChecked
            }
            container5.setOnClickListener {
                check5.isChecked = !check5.isChecked
            }
            cancelActionGroupDialogButton.setOnClickListener{
                callback.onCancel()
                dismiss()
            }
            confirmActionGroupDialogButton.setOnClickListener{
                val rarities = mutableSetOf<Int>()
                for (i in 0..4) {
                    if (checks[i].isChecked)
                        rarities.add(i + 1)
                }
                val items = Filter.RarityFilter(rarities)
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