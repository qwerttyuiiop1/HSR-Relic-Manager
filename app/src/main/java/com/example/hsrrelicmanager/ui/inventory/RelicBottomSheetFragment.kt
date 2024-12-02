package com.example.hsrrelicmanager.ui.inventory

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.RelicBottomSheetBinding
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.ActionPredictor
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.ui.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RelicBottomSheetFragment(
    private val relic: Relic,
    private val predictor: ActionPredictor,
    private val onUpdate: () -> Unit
) : BottomSheetDialogFragment() {
//    companion object {
//        operator fun invoke(r: Relic) =
//            RelicBottomSheetFragment().apply {
//                arguments = Bundle().apply {
//                    putParcelable("relic", r)
//                }
//            }
//    }

    private fun updateView(binding: RelicBottomSheetBinding, r: Relic) {
        var relic = r
        binding.apply {
            lblRelicDescription.text = relic.set.description

            lblRelicName.text = relic.set.name
            lblRelicMainStatType.text = relic.mainstat.name
            lblRelicMainStatValue.text = relic.mainstatVal
            imgRelicMainStat.setImageResource(relic.mainstat.image)

            imgRelic.setImageResource(relic.set.icon)
            var levelText = ""
            val goldSpan = ForegroundColorSpan(Color.parseColor("#FFC65C"))

            val action = predictor.predict(relic)
            levelText = "+${relic.level}"
            val spanString = SpannableString(levelText)
            if (relic.level == relic.rarity * 3) {
                spanString.setSpan(goldSpan, 0, levelText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            lblRelicLevel.text = spanString

            val newStatus = relic.status.toMutableSet()
            action?.forEach {
                if (it is EnhanceAction) {
                    var targetLevel = it.targetLevel
                    if (targetLevel <= 0) {
                        targetLevel = (relic.level / 3 + 1) * 3
                        targetLevel = targetLevel.coerceAtMost(relic.rarity * 3)
                        newStatus.add(Relic.Status.UPGRADE)
                    }

                    levelText = "+${relic.level}  >  +${targetLevel}"
                    val spanString = SpannableString(levelText)

                    if (it.targetLevel == relic.rarity * 3) {
                        spanString.setSpan(goldSpan, 7, levelText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    lblRelicLevel.text = spanString

                } else if (it is StatusAction) {
                    val exclusiveStatus = listOf(Relic.Status.LOCK, Relic.Status.TRASH, Relic.Status.DEFAULT)
                    if (it.targetStatus in exclusiveStatus)
                        newStatus.removeAll(exclusiveStatus)
                    newStatus.add(it.targetStatus)
                }
            }
            relic = relic.copy(status = newStatus.toList())

            imgRelic.setBackgroundResource(relic.rarityResource)

            val icons = listOf(
                imgRelicSubStat1,
                imgRelicSubStat2,
                imgRelicSubStat3,
                imgRelicSubStat4
            )
            val types = listOf(
                lblRelicSubStatType1,
                lblRelicSubStatType2,
                lblRelicSubStatType3,
                lblRelicSubStatType4
            )
            val values = listOf(
                lblRelicSubStatValue1,
                lblRelicSubStatValue2,
                lblRelicSubStatValue3,
                lblRelicSubStatValue4
            )
            val containers = listOf(
                relicSubstatContainer1,
                relicSubstatContainer2,
                relicSubstatContainer3,
                relicSubstatContainer4
            )

            for (i in containers.indices) {
                if (i < relic.substats.size)
                    containers[i].visibility = View.VISIBLE
                else
                    containers[i].visibility = View.INVISIBLE
            }

            val entries = relic.substats.toList()
            for (i in entries.indices) {
                val substat = entries.elementAt(i)
                icons[i].setImageResource(substat.image)
                types[i].text = substat.name
                values[i].text = substat.value
            }

            if (relic.status.contains(Relic.Status.EQUIPPED)) {
                containerRelicEquipped.visibility = View.VISIBLE
            } else {
                containerRelicEquipped.visibility = View.INVISIBLE
            }

            updateStatusIcons(relic, binding)

            val toggleStatus = { old: Relic.Status ->
                relic = relic.copy(
                    status = relic.status.toMutableList().apply {
                        if (old in this) {
                            remove(old)
                            if (Relic.Status.LOCK !in this && Relic.Status.TRASH !in this) {
                                add(Relic.Status.DEFAULT)
                            }
                        } else {
                            val exclusiveStatus = listOf(Relic.Status.LOCK, Relic.Status.TRASH, Relic.Status.DEFAULT)
                            if (old in exclusiveStatus)
                                removeAll(exclusiveStatus)
                            add(old)
                        }
                    }
                )
                updateStatusIcons(relic, binding)
                updateManualStatus(relic)
                onUpdate()
            }

            btnRelicUpgrade.setOnClickListener {
                toggleStatus(Relic.Status.UPGRADE)
            }
            btnRelicLock.setOnClickListener {
                toggleStatus(Relic.Status.LOCK)
            }
            btnRelicTrash.setOnClickListener {
                toggleStatus(Relic.Status.TRASH)
            }
        }
    }

    lateinit var binding: RelicBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RelicBottomSheetBinding.inflate(inflater, container, false)
//        var relic = arguments?.getParcelable<Relic>("relic")!!
        updateView(binding, relic)
        return binding.root
    }

    private fun updateStatusIcons(relic: Relic, binding: RelicBottomSheetBinding) = binding.apply {
        if (Relic.Status.LOCK in relic.status) {
            btnRelicLock.setBackgroundResource(R.drawable.bg_status_selected)
            btnRelicLock.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
        } else {
            btnRelicLock.setBackgroundResource(R.drawable.bg_status)
            btnRelicLock.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
        }
        if (Relic.Status.TRASH in relic.status) {
            btnRelicTrash.setBackgroundResource(R.drawable.bg_status_trash_selected)
            btnRelicTrash.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
        } else {
            btnRelicTrash.setBackgroundResource(R.drawable.bg_status)
            btnRelicTrash.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.red))
        }
        if (Relic.Status.UPGRADE in relic.status) {
            btnRelicUpgrade.setBackgroundResource(R.drawable.bg_status_selected)
        } else {
            btnRelicUpgrade.setBackgroundResource(R.drawable.bg_status)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.apply {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setOnShowListener {
                @Suppress("UNRESOLVED_REFERENCE")
                val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.setBackgroundResource(android.R.color.transparent)
//                val blurRadius = 30f
//                val renderEffect = RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.CLAMP)
//                bottomSheet.setRenderEffect(renderEffect)

            }
        }
    }

    private fun updateManualStatus(relic: Relic) {
        val mainActivity = requireContext() as MainActivity
        val dbManager = mainActivity.dbManager
        dbManager.open()
        dbManager.setManualStatus(relic)
        dbManager.close()
        mainActivity.cachedManualStatus[relic] = relic.status
    }
}