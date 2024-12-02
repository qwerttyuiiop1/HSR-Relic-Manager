package com.example.hsrrelicmanager.ui.inventory

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.InventoryRelicItemBinding
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.ActionPredictor
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction

class RelicAdapter(
    private val relicData: MutableList<Relic>,
    private val predictor: ActionPredictor,
    private val inventoryBodyFragment: InventoryBodyFragment,
    private var selectedPos: Int = RecyclerView.NO_POSITION
) :
    RecyclerView.Adapter<RelicAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InventoryRelicItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val relic = relicData[position]
        holder.bind(relic, position)
    }

    override fun getItemCount(): Int {
        return relicData.size
    }

    inner class ViewHolder(val binding: InventoryRelicItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(_relic: Relic, position: Int) {
            binding.apply {
                var relic = _relic
                lblRelicName.text = relic.set.name
                lblRelicMainStatType.text = relic.mainstat.name
                lblRelicMainStatValue.text = relic.mainstatVal
                imgRelicMainStat.setImageResource(relic.mainstat.image)
                imgRelic.setImageResource(relic.set.icon)
                var levelText = ""
                val goldSpan = ForegroundColorSpan(Color.parseColor("#FFC65C"))
                levelText = "+${relic.level}"
                val spanString = SpannableString(levelText)
                if (relic.level == relic.rarity * 3) {
                    spanString.setSpan(goldSpan, 0, levelText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                lblRelicLevel.text = spanString



                val action = predictor.predict(relic)
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

                val relics = relic.status.filter {
                    it != Relic.Status.EQUIPPED
                }.map {
                    relic.statusResource(it)
                }
                when (relics.size) {
                    0 -> {
                        containerSingleRelicStatus.visibility = View.INVISIBLE
                        containerDoubleRelicStatus.visibility = View.INVISIBLE
                    }
                    1 -> {
                        containerSingleRelicStatus.visibility = View.VISIBLE
                        containerDoubleRelicStatus.visibility = View.INVISIBLE
                        imgRelicStatus.setImageResource(relics[0])
                    }
                    2 -> {
                        containerSingleRelicStatus.visibility = View.INVISIBLE
                        containerDoubleRelicStatus.visibility = View.VISIBLE
                        imgRelicStatus1.setImageResource(relics[0])
                        imgRelicStatus2.setImageResource(relics[1])

                    }
                }
            }

            val relicCard = binding.root.findViewById<LinearLayout>(R.id.relic_card)

            if (position == selectedPos) {
                relicCard.setBackgroundResource(R.drawable.bg_inventory_relic_item_selected)
            } else {
                relicCard.setBackgroundResource(R.drawable.bg_inventory_relic_item)
            }

            binding.root.setOnClickListener {
                notifyItemChanged(selectedPos)

                selectedPos = if (selectedPos == position) {
                    RecyclerView.NO_POSITION
                } else {
                    position
                }

                notifyItemChanged(selectedPos)

                val relicBottomSheetFragment = RelicBottomSheetFragment(_relic, predictor, onUpdate = {
                    relicData[position] = it
                    notifyItemChanged(position)
                })
                relicBottomSheetFragment.show(inventoryBodyFragment.childFragmentManager, relicBottomSheetFragment.tag)
            }
        }
    }
}