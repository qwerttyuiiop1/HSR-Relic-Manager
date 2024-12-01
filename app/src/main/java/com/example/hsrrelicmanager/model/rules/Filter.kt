package com.example.hsrrelicmanager.model.rules

import com.example.hsrrelicmanager.model.Mainstat
import com.example.hsrrelicmanager.model.Slot
import com.example.hsrrelicmanager.model.Substat
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicSet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Filter {
    enum class Type {
        SET,
        SLOT,
        MAIN_STAT,
        SUB_STATS,
        RARITY,
        LEVEL,
        STATUS
    }

    // SET: All possible sets
    // SLOT: Head, Hands, Body, Feet, Orb, Rope
    // MAIN_STAT: may vary depending on TYPE
    // SUB_STATS: All possible substats
    // RARITY: 1-5
    // LEVEL: 0-15
    // STATUS: based on Relic.Status
    abstract val filterType: Type
    abstract val name: String
    abstract val description: String

    abstract fun accepts(relic: Relic): Boolean
    abstract fun cloneDeep(): Filter

    @Serializable
    @SerialName("SET")
    data class SetFilter(
        var sets: Set<RelicSet> = mutableSetOf()
    ): Filter() {
        override val filterType: Type = Type.SET
        override val name: String = "Relic Set"
        override val description: String
            get() = sets.joinToString(", ") { it.name.split(" ").first() }

        override fun accepts(relic: Relic): Boolean {
            return sets.contains(relic.set)
        }
        override fun cloneDeep() = SetFilter(sets.toMutableSet())
    }

    @Serializable
    @SerialName("SLOT")
    data class SlotFilter(
        var types: Set<Slot> = mutableSetOf()
    ): Filter() {
        override val filterType: Type = Type.SLOT
        override val name: String = "Relic Type"
        override val description
            get() = types.joinToString(", ")

        override fun accepts(relic: Relic): Boolean {
            return types.contains(relic.slot)
        }
        override fun cloneDeep() = SlotFilter(types.toMutableSet())
    }

    @Serializable
    @SerialName("MAINSTAT")
    data class MainStatFilter(
        var stats: Set<Mainstat> = mutableSetOf()
    ): Filter() {
        override val filterType: Type = Type.MAIN_STAT
        override val name: String = "Main Stat"
        override val description: String
            get() = stats.joinToString(", ")

        override fun accepts(relic: Relic): Boolean {
            return stats.any { it == relic.mainstat }
        }
        override fun cloneDeep() = MainStatFilter(stats.toMutableSet())
    }

    @Serializable
    @SerialName("SUBSTAT")
    data class SubStatFilter(
        var stats: Map<Substat, Int> = mutableMapOf(),
        var minWeight: Int = -1
    ): Filter() {
        override val filterType: Type = Type.SUB_STATS
        override val name: String = "Sub Stats"
        override val description: String
            get() = stats.keys.joinToString(", ")

        override fun accepts(relic: Relic): Boolean {
            //return stats.any { relic.substats.containsKey(it) }

            if (minWeight != -1) {
                // Get weight of each relic substat, and compare to minimum weight required
                var totalWeight = 0

                for (relicSubstat in relic.substats) {
                    if (relicSubstat in stats) {
                        totalWeight += stats[relicSubstat]!!
                    }
                }

                return totalWeight >= minWeight
            } else {
                // Check if each stat in filter is in relic also
                return stats.keys.all { it in relic.substats }

//                for (filterSubstat in stats.keys) {
//                    if (filterSubstat !in relic.substats) {
//                        return false
//                    }
//                }
//
//                return true
            }
        }
        override fun cloneDeep() = SubStatFilter(stats.toMutableMap(), minWeight)
    }

    @Serializable
    @SerialName("RARITY")
    data class RarityFilter(
        var rarities: Set<Int> = mutableSetOf()
    ): Filter() {
        override val filterType: Type = Type.RARITY
        override val name: String = "Rarity"
        override val description: String
            get() = rarities.joinToString(", ")

        override fun accepts(relic: Relic): Boolean {
            return rarities.contains(relic.rarity)
        }
        override fun cloneDeep() = RarityFilter(rarities.toMutableSet())
    }

    @Serializable
    @SerialName("LEVEL")
    data class LevelFilter(
        var atLeast: Int? = null,
        var atMost: Int? = null
    ): Filter() {
        override val filterType: Type = Type.LEVEL
        override val name: String = "Level"
        override val description: String
            get() = if (atLeast != null && atMost != null) {
                "$atLeast - $atMost"
            } else if (atLeast != null) {
                "At least $atLeast"
            } else if (atMost != null) {
                "At most $atMost"
            } else {
                "Any"
            }

        override fun accepts(relic: Relic): Boolean {
            return (atLeast == null || relic.level >= atLeast!!) &&
                    (atMost == null || relic.level <= atMost!!)
        }
        override fun cloneDeep() = LevelFilter(atLeast, atMost)
    }

    @Serializable
    @SerialName("STATUS")
    data class StatusFilter(
        var statuses: Set<Relic.Status> = mutableSetOf()
    ): Filter() {
        override val filterType: Type = Type.STATUS
        override val name: String = "Status"
        override val description: String
            get() = statuses.joinToString(", ")

        override fun accepts(relic: Relic): Boolean {
            return statuses.any { relic.status.contains(it) }
        }
        override fun cloneDeep() = StatusFilter(statuses.toMutableSet())
    }
}
