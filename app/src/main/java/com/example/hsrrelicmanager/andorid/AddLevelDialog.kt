package com.example.hsrrelicmanager.andorid

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.DialogLevelFilterBinding

class AddLevelDialog: DialogFragment() {

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

        binding.apply {
            var minLevel = 0
            var maxLevel = 0
            var isAtMost = true

            lblNumberAtMost.text = maxLevel.toString()
            lblNumberAtLeast.text = minLevel.toString()

            val setRadio = { atMost: Boolean ->
                isAtMost = atMost
                radioLevelAtMost.isChecked = isAtMost
                radioLevelAtLeast.isChecked = !isAtMost
            }
            setRadio(isAtMost)

            containerLevelAtMost.setOnClickListener{
                setRadio(true)
            }
            containerLevelAtLeast.setOnClickListener{
                setRadio(false)
            }
            radioLevelAtMost.setOnClickListener{
                setRadio(true)
            }
            radioLevelAtLeast.setOnClickListener{
                setRadio(false)
            }

            btnPlusAtMost.setOnClickListener{
                if (maxLevel < 15) {
                    maxLevel += 3
                    lblNumberAtMost.text = maxLevel.toString()
                }
            }
            btnMinusAtMost.setOnClickListener{
                if(maxLevel > 0){
                    maxLevel -= 3
                    lblNumberAtMost.text = maxLevel.toString()
                }
            }
            btnPlusAtLeast.setOnClickListener{
                if (minLevel < 15) {
                    minLevel += 3
                    lblNumberAtLeast.text = minLevel.toString()
                }
            }
            btnMinusAtLeast.setOnClickListener{
                if(minLevel > 0){
                    minLevel-=3
                    lblNumberAtLeast.text = minLevel.toString()
                }
            }



            cancelActionGroupDialogButton.setOnClickListener{
                dismiss()
            }
            confirmActionGroupDialogButton.setOnClickListener{
                requireActivity().supportFragmentManager.setFragmentResult("level", Bundle().apply {
                    putInt("minLevel", minLevel)
                    putInt("maxLevel", maxLevel)
                    putBoolean("isAtMost", isAtMost)
                })
                dismiss()
            }
        }

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