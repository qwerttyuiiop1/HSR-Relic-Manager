package com.example.hsrrelicmanager.andorid

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hsrrelicmanager.R

class AddRuleDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_rule, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireActivity())
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_add_rule, null)

        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val closeButton: ImageView = dialogView.findViewById(R.id.close_dialog_button)
        closeButton.setOnClickListener {
            dismiss()
        }

        val addActionGroupFragment: View = dialogView.findViewById(R.id.add_action_group)
        addActionGroupFragment.setOnClickListener {
            val addActionGroupHeaderFragment  = AddActionGroupHeaderFragment()
            val addActionGroupBodyFragment = AddActionGroupBodyFragment()

            (requireActivity() as MainActivity).loadFragment(addActionGroupHeaderFragment, addActionGroupBodyFragment)
            dismiss()
        }

        val addFilterGroupFragment: View = dialogView.findViewById(R.id.filter_action_group)
        addFilterGroupFragment.setOnClickListener {
            val addFilterGroupHeaderFragment  = AddFilterGroupHeaderFragment()
            val addFilterGroupBodyFragment = AddFilterGroupBodyFragment()

            (requireActivity() as MainActivity).loadFragment(addFilterGroupHeaderFragment, addFilterGroupBodyFragment)
            dismiss()
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


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Remove the blur effect on dismissal
        val bgView = requireActivity().findViewById<View>(R.id.activity_main_layout)
        bgView.setRenderEffect(null)
    }
}