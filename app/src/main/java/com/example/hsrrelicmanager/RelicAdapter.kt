package com.example.hsrrelicmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.databinding.InventoryRelicItemBinding
import com.example.hsrrelicmanager.model.relics.Relic

class RelicAdapter(
    private val relicData: MutableList<Relic>,
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
        fun bind(relic: Relic, position: Int) {
            binding.apply {
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

                val relicBottomSheetFragment = RelicBottomSheetFragment(relic)
                relicBottomSheetFragment.show(inventoryBodyFragment.childFragmentManager, relicBottomSheetFragment.tag)
                inventoryBodyFragment.requireActivity().supportFragmentManager.setFragmentResultListener("relic", inventoryBodyFragment.viewLifecycleOwner) { _, bundle ->
                    val r = bundle.getParcelable<Relic>("relic")!!
                    relicData[position] = r
                    notifyItemChanged(position)
                }
            }
        }
    }
}