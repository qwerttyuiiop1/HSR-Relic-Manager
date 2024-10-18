package com.example.hsrrelicmanager

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AddRuleDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_rule_dialog, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle("Add Rule")
            .setView(requireActivity().layoutInflater.inflate(R.layout.add_rule_dialog, null))
            .setPositiveButton("OK") { dialog, which ->
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .create()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val bgView = requireActivity().findViewById<View>(R.id.activity_main_layout)
        bgView.setRenderEffect(null)
    }
}
