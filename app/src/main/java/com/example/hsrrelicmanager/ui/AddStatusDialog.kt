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
import com.example.hsrrelicmanager.databinding.DialogStatusFilterBinding

class AddStatusDialog(private val items: MutableList<FilterBuilder>): DialogFragment() {

    val binding: DialogStatusFilterBinding by lazy {
        DialogStatusFilterBinding.inflate(
            layoutInflater, null, false
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        var index = -1
        index = items.indexOfFirst { it.title == "Status" }

        if (index != -1){
            for (item in items[index].statusList) {
                if (item.name == "Lock") {
                    binding.check1.isChecked = true
                }
                if (item.name == "Trash") {
                    binding.check2.isChecked = true
                }
                if (item.name == "None") {
                    binding.check3.isChecked = true
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

            cancelActionGroupDialogButton.setOnClickListener{
                if (index == -1){
                    val addFilterDialog = AddFilterDialog(items)
                    addFilterDialog.show(requireActivity().supportFragmentManager, "AddFilterDialog")
                }
                dismiss()
            }
            confirmActionGroupDialogButton.setOnClickListener{
                val check = check1.isChecked || check2.isChecked || check3.isChecked

                if (!check) {
                    items.removeAt(index)
                }

                requireActivity().supportFragmentManager.setFragmentResult("status", Bundle().apply {
                    if (check1.isChecked) {
                        putString("Lock", "Lock")
                        putInt("lockImage", R.drawable.lock)
                    }
                    if (check2.isChecked) {
                        putString("Trash", "Trash")
                        putInt("trashImage", R.drawable.trash)
                    }
                    if (check3.isChecked) {
                        putString("None", "None")
                        putInt("noneImage", R.drawable.transparent)
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