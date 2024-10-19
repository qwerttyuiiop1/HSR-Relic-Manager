package com.example.hsrrelicmanager.model.rules;

public class Filter {
    public enum Type {
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
}
