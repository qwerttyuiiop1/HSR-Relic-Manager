package com.example.hsrrelicmanager

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.hsrrelicmanager.databinding.RelicBottomSheetBinding
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicBuilder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RelicBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        operator fun invoke(r: Relic) =
            RelicBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("relic", r)
                }
            }
    }

    private fun updateView(binding: RelicBottomSheetBinding, r: Relic) {
        var relic = r
        binding.apply {
            lblRelicDescription.text = relic.set.description

            lblRelicName.text = relic.set.name
            lblRelicMainStatType.text = relic.mainstat
            lblRelicMainStatValue.text = relic.mainstatVal
            imgRelicMainStat.setImageResource(relic.mainstatResource)

            imgRelic.setImageResource(relic.set.icon)
            if (relic.prev != null && relic.level != relic.prev!!.level) {
                lblRelicLevel.text = "+${relic.prev!!.level}  >  +${relic.level}"
            } else {
                lblRelicLevel.text = "+${relic.level}"
            }

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

            val entries = relic.substats.entries
            for (i in entries.indices) {
                val substat = entries.elementAt(i)
                icons[i].setImageResource(relic.substatResource(substat.key))
                types[i].text = substat.key
                values[i].text = substat.value
            }

            if (relic.status.contains(Relic.Status.EQUIPPED)) {
                containerRelicEquipped.visibility = View.VISIBLE
            } else {
                containerRelicEquipped.visibility = View.INVISIBLE
            }

            updateStatusIcons(relic, binding)
            val toggleStatus = { old: Relic.Status ->
                relic = RelicBuilder(relic).apply {
                    mstatus = mstatus.toMutableList().apply {
                        if (old in mstatus) {
                            remove(old)
                            if (old == Relic.Status.UPGRADE) {
                                level = prev.level
                            }
                        } else {
                            add(old)
                            if (old == Relic.Status.LOCK) {
                                remove(Relic.Status.TRASH)
                            } else if (old == Relic.Status.TRASH) {
                                remove(Relic.Status.LOCK)
                            } else if (old == Relic.Status.UPGRADE) {
                                val lprev = level
                                level = ((lprev/3 + 1)*3).coerceAtMost(relic.rarity * 3)
                                if (lprev == level) {
                                    remove(Relic.Status.UPGRADE)
                                } else {
                                    prev.level = lprev
                                }
                            }
                        }
                    }
                }.build()
                updateStatusIcons(relic, binding)
                requireActivity().supportFragmentManager.setFragmentResult("relic", Bundle().apply {
                    putParcelable("relic", relic)
                    updateView(binding, relic)
                })
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
        var relic = arguments?.getParcelable<Relic>("relic")!!
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
                val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.setBackgroundResource(android.R.color.transparent)
//                val blurRadius = 30f
//                val renderEffect = RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.CLAMP)
//                bottomSheet.setRenderEffect(renderEffect)

            }
        }
    }
}