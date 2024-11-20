package com.example.hsrrelicmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Slot(
    val name: String,
    var level: Int
): Parcelable{}

val slotSets = listOf(
    Slot(
        "HP",
        1
    ),
    Slot(
        "ATK",
        1
    ),
    Slot(
        "HP%",
        1
    ),
    Slot(
        "ATK%",
        1
    ),
    Slot(
        "DEF%",
        1
    ),
    Slot(
        "Effect Hit Rate",
        1
    ),
    Slot(
        "Outgoing Healing Boost",
        1
    ),
    Slot(
        "Crit Rate",
        1
    ),
    Slot(
        "Crit DMG",
        1
    ),
    Slot(
        "SPD",
        1
    ),
    Slot(
        "Physical DMG Boost",
        1
    ),
    Slot(
        "Fire DMG Boost",
        1
    ),
    Slot(
        "Ice DMG Boost",
        1
    ),
    Slot(
        "Wind DMG Boost",
        1
    ),
    Slot(
        "Lightning DMG Boost",
        1
    ),
    Slot(
        "Quantum DMG Boost",
        1
    ),
    Slot(
        "Imaginary DMG Boost",
        1
    ),
    Slot(
        "Break Effect",
        1
    ),
    Slot(
        "Energy Regeneration Rate",
        1
    ),
)
