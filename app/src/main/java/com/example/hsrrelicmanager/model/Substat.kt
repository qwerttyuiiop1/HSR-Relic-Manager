package com.example.hsrrelicmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Substat(
    val name: String,
    var level: Int
): Parcelable{}


val substatSets = listOf(
    Substat(
        "HP",
        1
    ),
    Substat(
        "ATK",
        1
    ),
    Substat(
        "DEF",
        1
    ),
    Substat(
        "HP%",
        1
    ),
    Substat(
        "ATK%",
        1
    ),
    Substat(
        "DEF%",
        1
    ),
    Substat(
        "Crit Rate",
        1
    ),
    Substat(
        "Crit DMG",
        1
    ),
    Substat(
        "Effect HIT Rate",
        1
    ),
    Substat(
        "Effect RES",
        1
    ),
    Substat(
        "Break Effect",
        1
    ),
    Substat(
        "Speed",
        1
    )
)
