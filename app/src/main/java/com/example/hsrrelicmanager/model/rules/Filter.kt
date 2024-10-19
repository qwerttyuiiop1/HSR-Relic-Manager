package com.example.hsrrelicmanager.model.rules

abstract class Filter {
    enum class Type {
        SET,
        TYPE,
        MAIN_STAT,
        SUB_STATS,
        RARITY,
        LEVEL,
        STATUS
    }

    // SET: All possible sets
    // TYPE: Head, Hands, Body, Feet, Orb, Rope
    // MAIN_STAT: may vary depending on TYPE
    // SUB_STATS: All possible substats
    // RARITY: 1-5
    // LEVEL: 0-15
    // STATUS: based on Relic.Status
    abstract val type: Type?
    abstract val name: String
    abstract val description: String

    class SetFilter: Filter() {
        override val type: Type = Type.SET
        override val name: String = "Relic Set"
        override val description: String = "Filter by relic set"
    }
}
