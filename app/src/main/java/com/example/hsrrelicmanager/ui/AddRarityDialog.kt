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
import com.example.hsrrelicmanager.model.FilterBuilder
import com.example.hsrrelicmanager.databinding.DialogRarityFilterBinding

class AddRarityDialog(private val items: MutableList<FilterBuilder>): DialogFragment() {

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

        var index = -1
        index = items.indexOfFirst { it.title == "Rarity" }

        if (index != -1){
            for (item in items[index].rarityList) {
                if (item == 1) {
                    binding.check1.isChecked = true
                }
                if (item == 2) {
                    binding.check2.isChecked = true
                }
                if (item == 3) {
                    binding.check3.isChecked = true
                }
                if (item == 4) {
                    binding.check4.isChecked = true
                }
                if (item == 5) {
                    binding.check5.isChecked = true
                }
            }
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
                if (index == -1){
                    val addFilterDialog = AddFilterDialog(items)
                    addFilterDialog.show(requireActivity().supportFragmentManager, "AddFilterDialog")
                }
                dismiss()
            }
            confirmActionGroupDialogButton.setOnClickListener{
                val check = check1.isChecked || check2.isChecked || check3.isChecked || check4.isChecked || check5.isChecked

                if (!check){
                    items.removeAt(index)
                }

                requireActivity().supportFragmentManager.setFragmentResult("rarity", Bundle().apply {
                    if (check1.isChecked) {
                        putInt("1 Star", 1)
                    }
                    else {
                        putInt("1 Star", 0)
                    }
                    if (check2.isChecked) {
                        putInt("2 Star", 2)
                    }
                    else {
                        putInt("2 Star", 0)
                    }
                    if (check3.isChecked) {
                        putInt("3 Star", 3)
                    }
                    else {
                        putInt("3 Star", 0)
                    }
                    if (check4.isChecked) {
                        putInt("4 Star", 4)
                    }
                    else {
                        putInt("4 Star", 0)
                    }
                    if (check5.isChecked) {
                        putInt("5 Star", 5)
                    }
                    else {
                        putInt("5 Star", 0)
                    }
                })
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