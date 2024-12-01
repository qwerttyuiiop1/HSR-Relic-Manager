package com.example.hsrrelicmanager.ui.rules

import android.content.DialogInterface
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.DialogSelectFilterBinding
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.group.FilterMap
import com.example.hsrrelicmanager.ui.MainActivity

class AddFilterDialog(
    private val items: FilterMap,
    private val listener: GroupChangeListener
): DialogFragment() {

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

            if (Filter.Type.SET in items) {
                addRelicSet.isEnabled = false
                addRelicSet.alpha = 0.5f
            } else {
                addRelicSet.setOnClickListener {
                    val filter = items[Filter.Type.SET] as Filter.SetFilter?
                        ?: Filter.SetFilter()
                    val addSetDialog = AddSetDialog(filter, listener)
                    addSetDialog.show(requireActivity().supportFragmentManager, "AddSetDialog")
                    dismiss()
                }
            }

            if (Filter.Type.SLOT in items) {
                addSlot.isEnabled = false
                addSlot.alpha = 0.5f
            } else {
                addSlot.setOnClickListener {
                    val filter = items[Filter.Type.SLOT] as Filter.SlotFilter?
                        ?: Filter.SlotFilter()
                    val addSlotDialog = AddSlotDialog(filter, listener)
                    addSlotDialog.show(requireActivity().supportFragmentManager, "AddSlotDialog")
                    dismiss()
                }
            }

            if (Filter.Type.MAIN_STAT in items) {
                addMainStat.isEnabled = false
                addMainStat.alpha = 0.5f
            } else {
                addMainStat.setOnClickListener {
                    val filter = items[Filter.Type.MAIN_STAT] as Filter.MainStatFilter?
                        ?: Filter.MainStatFilter()
                    val addMainStatDialog = AddMainstatDialog(filter, listener)
                    addMainStatDialog.show(
                        requireActivity().supportFragmentManager,
                        "AddMainstatDialog"
                    )
                    dismiss()
                }
            }

            if (Filter.Type.SUB_STATS in items) {
                addSubStat.isEnabled = false
                addSubStat.alpha = 0.5f
            } else {
                addSubStat.setOnClickListener {
                    val filter = items[Filter.Type.SUB_STATS] as Filter.SubStatFilter?
                        ?: Filter.SubStatFilter()
                    val addSubStatDialog = AddSubstatDialog(filter, listener)
                    addSubStatDialog.show(
                        requireActivity().supportFragmentManager,
                        "AddSubstatDialog"
                    )
                    dismiss()
                }
            }

            if (Filter.Type.RARITY in items) {
                addRarity.isEnabled = false
                addRarity.alpha = 0.5f
            } else {
                addRarity.setOnClickListener {
                    val filter = items[Filter.Type.RARITY] as Filter.RarityFilter?
                        ?: Filter.RarityFilter()
                    val addRarityDialog = AddRarityDialog(filter, listener)
                    addRarityDialog.show(requireActivity().supportFragmentManager, "AddRarityDialog")
                    dismiss()
                }
            }

            if (Filter.Type.LEVEL in items) {
                addLevel.isEnabled = false
                addLevel.alpha = 0.5f
            } else {
                addLevel.setOnClickListener {
                    val filter = items[Filter.Type.LEVEL] as Filter.LevelFilter?
                        ?: Filter.LevelFilter()
                    val addLevelDialog = AddLevelDialog(filter, listener)
                    addLevelDialog.show(requireActivity().supportFragmentManager,"AddLevelDialog")
                    dismiss()
                }
            }

            if (Filter.Type.STATUS in items) {
                addStatus.isEnabled = false
                addStatus.alpha = 0.5f
            } else {
                addStatus.setOnClickListener {
                    val filter = items[Filter.Type.STATUS] as Filter.StatusFilter?
                        ?: Filter.StatusFilter()
                    val addStatusDialog = AddStatusDialog(filter, listener)
                    addStatusDialog.show(requireActivity().supportFragmentManager, "AddStatusDialog")
                    dismiss()
                }
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