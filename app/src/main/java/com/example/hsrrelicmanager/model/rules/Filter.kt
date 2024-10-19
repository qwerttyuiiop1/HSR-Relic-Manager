package com.example.hsrrelicmanager.model.rules

import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicSet

abstract class Filter {
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
    abstract val type: Type?
    abstract val name: String
    abstract val description: String

    abstract fun accepts(relic: Relic): Boolean

    class SetFilter(
        var sets: MutableSet<RelicSet> = mutableSetOf()
    ): Filter() {
        override val type: Type = Type.SET
        override val name: String = "Relic Set"
        override val description: String = "Filter by relic set"

        override fun accepts(relic: Relic): Boolean {
            return sets.contains(relic.set)
        }
    }

    class SlotFilter(
        var types: MutableSet<String> = mutableSetOf()
    ): Filter() {
        override val type: Type = Type.SLOT
        override val name: String = "Relic Type"
        override val description: String = "Filter by relic type"

        override fun accepts(relic: Relic): Boolean {
            return types.contains(relic.slot)
        }
    }

    class MainStatFilter(
        var stats: MutableSet<String> = mutableSetOf()
    ): Filter() {
        override val type: Type = Type.MAIN_STAT
        override val name: String = "Main Stat"
        override val description: String = "Filter by main stat"

        override fun accepts(relic: Relic): Boolean {
            return stats.any { it == relic.mainstat }
        }
    }

    class SubStatFilter(
        var stats: MutableSet<String> = mutableSetOf()
    ): Filter() {
        override val type: Type = Type.SUB_STATS
        override val name: String = "Sub Stats"
        override val description: String = "Filter by sub stats"

        override fun accepts(relic: Relic): Boolean {
            return stats.any { relic.substats.containsKey(it) }
        }
    }

    class RarityFilter(
        var atLeast: Int? = null,
        var atMost: Int? = null
    ): Filter() {
        override val type: Type = Type.RARITY
        override val name: String = "Rarity"
        override val description: String = "Filter by rarity"

        override fun accepts(relic: Relic): Boolean {
            return (atLeast == null || relic.rarity >= atLeast!!) &&
                    (atMost == null || relic.rarity <= atMost!!)
        }
    }

    class LevelFilter(
        var atLeast: Int? = null,
        var atMost: Int? = null
    ): Filter() {
        override val type: Type = Type.LEVEL
        override val name: String = "Level"
        override val description: String = "Filter by level"

        override fun accepts(relic: Relic): Boolean {
            return (atLeast == null || relic.level >= atLeast!!) &&
                    (atMost == null || relic.level <= atMost!!)
        }
    }

    class StatusFilter(
        var statuses: MutableSet<Relic.Status> = mutableSetOf()
    ): Filter() {
        override val type: Type = Type.STATUS
        override val name: String = "Status"
        override val description: String = "Filter by status"

        override fun accepts(relic: Relic): Boolean {
            return statuses.any { relic.status.contains(it) }
        }
    }
}
