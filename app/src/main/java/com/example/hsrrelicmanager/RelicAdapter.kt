package com.example.hsrrelicmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.databinding.InventoryRelicItemBinding

class RelicAdapter(private val relicData: List<Relic>) :
    RecyclerView.Adapter<RelicAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InventoryRelicItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val relic = relicData[position]
        holder.bind(relic)
    }

    override fun getItemCount(): Int {
        return relicData.size
    }

    private fun getRarityResource(relic: Relic): Int {
        val rarity = relic.rarity

        return when (rarity) {
            1 -> R.drawable.bg_1_star
            2 -> R.drawable.bg_2_star
            3 -> R.drawable.bg_3_star
            4 -> R.drawable.bg_4_star
            5 -> R.drawable.bg_5_star
            else -> R.drawable.playfrag_test
        }
    }

    private fun getStatResource(stat: String): Int {
        return when (stat) {
            "ATK", "ATK%" -> R.drawable.icon_atk
            "DEF" -> R.drawable.icon_def
            "SPD" -> R.drawable.icon_spd
            "CRIT Rate" -> R.drawable.icon_crit_rate
            else -> R.drawable.playfrag_test
        }
    }

    inner class ViewHolder(val binding: InventoryRelicItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(relic: Relic) {
            binding.apply {
                lblRelicName.text = relic.set
                lblRelicMainStatType.text = relic.mainstat
                lblRelicMainStatValue.text = relic.mainstatVal
                imgRelicMainStat.setImageResource(getStatResource(relic.mainstat))

                imgRelic.setImageResource(relic.image)
                lblRelicLevel.text = "+${relic.level}"

                val backgroundResource = getRarityResource(relic)
                imgRelic.setBackgroundResource(backgroundResource)

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
                        containers[i].visibility = View.GONE
                }

                val entries = relic.substats.entries
                for (i in entries.indices) {
                    val substat = entries.elementAt(i)
                    icons[i].setImageResource(getStatResource(substat.key))
                    types[i].text = substat.key
                    values[i].text = substat.value.toString()
                }
            }
        }
    }
}