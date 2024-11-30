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
import com.example.hsrrelicmanager.databinding.DialogLevelFilterBinding
import com.example.hsrrelicmanager.model.rules.Filter

class AddLevelDialog(
    private val items: Filter.LevelFilter,
    private val callback: GroupChangeListener
): DialogFragment() {

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

        var atLeast = 0
        var atMost = 0
        if (items.atLeast != null) {
            binding.radioLevelAtLeast.isChecked = true
            binding.radioLevelAtMost.isChecked = false
            binding.lblNumberAtLeast.text = items.atLeast.toString()
        }
        else {
            binding.radioLevelAtLeast.isChecked = false
            binding.radioLevelAtMost.isChecked = true
            binding.lblNumberAtMost.text = items.atMost.toString()
        }

        binding.apply {

            lblNumberAtMost.text = atMost.toString()
            lblNumberAtLeast.text = atLeast.toString()

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
                if (atMost < 15) {
                    atMost += 3
                    lblNumberAtMost.text = atMost.toString()
                }
            }
            btnMinusAtMost.setOnClickListener{
                if(atLeast > 0){
                    atLeast -= 3
                    lblNumberAtMost.text = atLeast.toString()
                }
            }
            btnPlusAtLeast.setOnClickListener{
                if (atLeast < 15) {
                    atLeast += 3
                    lblNumberAtLeast.text = atLeast.toString()
                }
            }
            btnMinusAtLeast.setOnClickListener{
                if(atLeast > 0){
                    atLeast-=3
                    lblNumberAtLeast.text = atLeast.toString()
                }
            }

            cancelActionGroupDialogButton.setOnClickListener{
                callback.onCancel()
                dismiss()
            }
            confirmActionGroupDialogButton.setOnClickListener{
                val isAtLeast = radioLevelAtLeast.isChecked
                val least = if (isAtLeast) atLeast else null
                val most = if (!isAtLeast) atMost else null
                val items = Filter.LevelFilter(least, most)
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