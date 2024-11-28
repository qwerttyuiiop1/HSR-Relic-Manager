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
import com.example.hsrrelicmanager.databinding.DialogLevelFilterBinding

class AddLevelDialog(private val items: MutableList<FilterBuilder>): DialogFragment() {

    val binding: DialogLevelFilterBinding by lazy {
        DialogLevelFilterBinding.inflate(
            layoutInflater, null, false
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        var leastLevel = 0
        var mostLevel = 0
        var isAtLeast = true
        var isAtMost = false

        var index = -1
        index = items.indexOfFirst { it.title == "Level" }

        if (index != -1){
            val lvl = items[index].levelNum.toString()
            val type = items[index].levelType
            if (type) {
                binding.radioLevelAtLeast.isChecked = true
                binding.radioLevelAtMost.isChecked = false
                leastLevel = lvl.toInt()
                binding.lblNumberAtLeast.text = lvl
            }
            else {
                binding.radioLevelAtLeast.isChecked = false
                binding.radioLevelAtMost.isChecked = true
                mostLevel = lvl.toInt()
                binding.lblNumberAtMost.text = lvl
            }
        }
        else{
            binding.radioLevelAtLeast.isChecked = true
            binding.radioLevelAtMost.isChecked = false
        }

        binding.apply {

            lblNumberAtMost.text = mostLevel.toString()
            lblNumberAtLeast.text = leastLevel.toString()

            val setRadioAtLeast = {
                radioLevelAtLeast.isChecked = true
                radioLevelAtMost.isChecked = false
            }

            val setRadioAtMost = {
                radioLevelAtLeast.isChecked = false
                radioLevelAtMost.isChecked = true
            }

            containerLevelAtMost.setOnClickListener{
                setRadioAtMost()
            }
            containerLevelAtLeast.setOnClickListener{
                setRadioAtLeast()
            }
            radioLevelAtMost.setOnClickListener{
                setRadioAtMost()
            }
            radioLevelAtLeast.setOnClickListener{
                setRadioAtLeast()
            }

            btnPlusAtMost.setOnClickListener{
                if (mostLevel < 15) {
                    mostLevel += 3
                    lblNumberAtMost.text = mostLevel.toString()
                }
            }
            btnMinusAtMost.setOnClickListener{
                if(mostLevel > 0){
                    mostLevel -= 3
                    lblNumberAtMost.text = mostLevel.toString()
                }
            }
            btnPlusAtLeast.setOnClickListener{
                if (leastLevel < 15) {
                    leastLevel += 3
                    lblNumberAtLeast.text = leastLevel.toString()
                }
            }
            btnMinusAtLeast.setOnClickListener{
                if(leastLevel > 0){
                    leastLevel-=3
                    lblNumberAtLeast.text = leastLevel.toString()
                }
            }

            cancelActionGroupDialogButton.setOnClickListener{
                if (index == -1){
                    val addFilterDialog = AddFilterDialog(items)
                    addFilterDialog.show(requireActivity().supportFragmentManager, "AddFilterDialog")
                }
                dismiss()
            }
            confirmActionGroupDialogButton.setOnClickListener{
                requireActivity().supportFragmentManager.setFragmentResult("level", Bundle().apply {
                    isAtLeast = radioLevelAtLeast.isChecked

                    if (isAtLeast) {
                        putInt("level", leastLevel)
                    }
                    else{
                        putInt("level", mostLevel)
                    }
                    putBoolean("isAtLeast", isAtLeast)
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