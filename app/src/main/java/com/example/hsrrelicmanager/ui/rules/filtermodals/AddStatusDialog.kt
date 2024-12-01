package com.example.hsrrelicmanager.ui.rules.filtermodals

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.DialogStatusFilterBinding
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.ui.MainActivity
import com.example.hsrrelicmanager.ui.rules.GroupChangeListener

class AddStatusDialog(
    private val items: Filter.StatusFilter,
    private val callback: GroupChangeListener
): DialogFragment() {

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

        if (Relic.Status.LOCK in items.statuses) {
            binding.check1.isChecked = true
        }
        if (Relic.Status.TRASH in items.statuses) {
            binding.check2.isChecked = true
        }
        if (Relic.Status.DEFAULT in items.statuses) {
            binding.check3.isChecked = true
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
                callback.onCancel()
                dismiss()
            }
            confirmActionGroupDialogButton.setOnClickListener{
                val statuses = mutableSetOf<Relic.Status>()
                if (check1.isChecked) {
                    statuses.add(Relic.Status.LOCK)
                }
                if (check2.isChecked) {
                    statuses.add(Relic.Status.TRASH)
                }
                if (check3.isChecked) {
                    statuses.add(Relic.Status.DEFAULT)
                }
                callback.onUpdateFilter(Filter.StatusFilter(statuses))
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