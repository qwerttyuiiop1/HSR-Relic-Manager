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

class AddRarityDialog: DialogFragment() {

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
            cancelActionGroupDialogButton.setOnClickListener{
                val addFilterDialog = AddFilterDialog()
                addFilterDialog.show(requireActivity().supportFragmentManager, "AddFilterDialog")
                dismiss()
            }
            confirmActionGroupDialogButton.setOnClickListener{
                requireActivity().supportFragmentManager.setFragmentResult("rarity", Bundle().apply {
                    if (check1.isChecked) {
                        putString("Star3", "3 Star")
                    }
                    if (check2.isChecked) {
                        putString("Star4", "4 Star")
                    }
                    if (check3.isChecked) {
                        putString("Star5", "5 Star")
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