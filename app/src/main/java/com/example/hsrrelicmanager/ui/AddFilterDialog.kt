package com.example.hsrrelicmanager.ui

import android.content.DialogInterface
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.components.FilterItem
import com.example.hsrrelicmanager.databinding.DialogSelectFilterBinding
import java.util.logging.Filter

class AddFilterDialog(private val items: MutableList<FilterItem>): DialogFragment() {

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

        val relicSetIndex = items.indexOfFirst { it.title == "Relic Set" }
        val rarityIndex = items.indexOfFirst { it.title == "Rarity" }
        val levelIndex = items.indexOfFirst { it.title == "Level" }
        val statusIndex = items.indexOfFirst { it.title == "Status" }

        binding.apply {
            closeDialogButton.setOnClickListener {
                dismiss()
            }

            if (relicSetIndex != -1) {
                addRelicSet.isEnabled = false
                addRelicSet.alpha = 0.5f
            } else {
                addRelicSet.setOnClickListener {
                    val addSetDialog = AddSetDialog(items)
                    addSetDialog.show(requireActivity().supportFragmentManager, "AddSetDialog")
                    dismiss()
                }
            }

//            addSlot.setOnClickListener {
//                // TODO: start AddSlotFragment
//            }

            addMainStat.setOnClickListener {
                val addMainStatDialog = AddMainstatDialog()
                addMainStatDialog.show(requireActivity().supportFragmentManager, "AddMainstatDialog")
                dismiss()
            }


            addSubStat.setOnClickListener {
                val addSubStatDialog = AddSubstatDialog()
                addSubStatDialog.show(requireActivity().supportFragmentManager, "AddSubstatDialog")
                dismiss()
            }

            if (rarityIndex != -1) {
                addRarity.isEnabled = false
                addRarity.alpha = 0.5f
            } else {
                addRarity.setOnClickListener {
                    val addRarityDialog = AddRarityDialog(items)
                    addRarityDialog.show(requireActivity().supportFragmentManager, "AddRarityDialog")
                    dismiss()
                }
            }

            if (levelIndex != -1) {
                addLevel.isEnabled = false
                addLevel.alpha = 0.5f
            } else {
                addLevel.setOnClickListener() {
                    val addLevelDialog = AddLevelDialog(items);
                    addLevelDialog.show(requireActivity().supportFragmentManager,"AddLevelDialog")
                    dismiss()
                }
            }

            if (statusIndex != -1) {
                addStatus.isEnabled = false
                addStatus.alpha = 0.5f
            } else {
                addStatus.setOnClickListener {
                    val addStatusDialog = AddStatusDialog(items)
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