package com.example.hsrrelicmanager

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hsrrelicmanager.databinding.DialogSelectFilterBinding

class AddFilterDialog: DialogFragment() {

    val binding: DialogSelectFilterBinding by lazy {
        DialogSelectFilterBinding.inflate(
            layoutInflater, null, false
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.apply {
            closeDialogButton.setOnClickListener {
                dismiss()
            }
            addRelicSet.setOnClickListener {
                val addSetDialog = AddSetDialog()
                addSetDialog.show(requireActivity().supportFragmentManager, "AddSetDialog")
                dismiss()
            }
            addSlot.setOnClickListener {
                // TODO: start AddSlotFragment
            }
            addMainStat.setOnClickListener {
                // TODO: start AddMainStatFragment
            }
            addSubStat.setOnClickListener {
                // TODO: start AddSubStatFragment
            }
            addRarity.setOnClickListener {
                // TODO: start AddRarityFragment
            }
            addLevel.setOnClickListener {
                val addLevelDialog = AddLevelDialog();
                addLevelDialog.show(requireActivity().supportFragmentManager, "AddLevelDialog")
                dismiss()
            }
            addStatus.setOnClickListener {
                // TODO: start AddStatusFragment
            }
        }
//        val addActionGroupFragment: View = dialogView.findViewById(R.id.add_action_group)
//        addActionGroupFragment.setOnClickListener {
//            val addActionGroupHeaderFragment  = AddActionGroupHeaderFragment()
//            val addActionGroupBodyFragment = AddActionGroupBodyFragment()
//
//            (requireActivity() as MainActivity).loadFragment(addActionGroupHeaderFragment, addActionGroupBodyFragment)
//            dismiss()
//        }

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