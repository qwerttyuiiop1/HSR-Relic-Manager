package com.example.hsrrelicmanager.ui

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.GroupCardDescriptionBinding
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import com.example.hsrrelicmanager.model.rules.group.FilterGroup
import com.example.hsrrelicmanager.model.rules.group.Group

class DeleteRuleDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_INDEX = "index"
        private const val ARG_DATA = "data"

        fun newInstance(index: Int, data: Group): DeleteRuleDialogFragment {
            val fragment = DeleteRuleDialogFragment()
            val args = Bundle().apply {
                putInt(ARG_INDEX, index)
                putParcelable(ARG_DATA, data)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_delete_rule, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): androidx.appcompat.app.AlertDialog {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_delete_rule, null)

        val index = arguments?.getInt(ARG_INDEX)
        val group = arguments?.getParcelable<Group>(ARG_DATA)

        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Set group data
        val name = group!!.viewName
        val groupName = dialogView.findViewById<TextView>(R.id.tvGroupName)
        groupName.text = name

        val groupIcon = dialogView.findViewById<ImageView>(R.id.groupIcon)
        groupIcon.setBackgroundResource(getGroupImageResource(group))

        val filters = group!!.filters
        val filterList = ArrayList(filters.values)

        // Set filters
        val filterContainer = dialogView.findViewById<ViewGroup>(R.id.filter_container)
        filterContainer.removeAllViews()

        for (filter in filterList) {
            val binding = GroupCardDescriptionBinding.inflate(
                LayoutInflater.from(filterContainer.context),
                filterContainer,
                false
            )
            binding.rowName.setTextColor(Color.parseColor("#4A4A4A"))
            binding.rowValue.setTextColor(Color.parseColor("#4A4A4A"))

            binding.rowName.text = filter.name + ':'
            binding.rowValue.text = filter.description
            filterContainer.addView(binding.root)
        }

        // Cancel button
        val cancelButton: FrameLayout = dialogView.findViewById(R.id.cancel_delete_rule_dialog_button)
        cancelButton.setOnClickListener {
            val resultBundle = Bundle().apply {
                putInt("index", index ?: -1)
                putString("action", "cancel")
            }
            parentFragmentManager.setFragmentResult("delete_rule_request", resultBundle)
            dismiss()
        }

        // Confirm button
        val confirmButton: FrameLayout = dialogView.findViewById(R.id.confirm_delete_rule_dialog_button)
        confirmButton.setOnClickListener {
            val resultBundle = Bundle().apply {
                putInt("index", index ?: -1)
                putString("action", "confirm")
            }
            parentFragmentManager.setFragmentResult("delete_rule_request", resultBundle)
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

            layoutParams.width = screenWidth - 100
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

    private fun getGroupImageResource(group: Group): Int {
        if (group is FilterGroup) {
            return R.drawable.sticker_ppg_11_other_01
        } else if (group is ActionGroup) {
            val action = group.action

            if (action is EnhanceAction) {
                return R.drawable.sticker_ppg_09_topaz_and_numby_03
            } else if (action is StatusAction) {
                return when ((action as StatusAction).targetStatus) {
                    Relic.Status.LOCK -> R.drawable.sticker_ppg_07_pom_pom_04
                    Relic.Status.TRASH -> R.drawable.sticker_ppg_12_other_01
                    Relic.Status.DEFAULT -> R.drawable.sticker_ppg_13_acheron_03
                    else -> -1
                }
            }
        }

        return -1
    }
}