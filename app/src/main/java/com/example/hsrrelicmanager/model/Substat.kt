package com.example.hsrrelicmanager.model

import android.os.Parcelable
import com.example.hsrrelicmanager.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Substat(
    val name: String,
    var level: Int,
    val image: Int
): Parcelable{}


val substatSets = listOf(
    Substat(
        "HP",
        1,
        R.drawable.stat_hp
    ),
    Substat(
        "ATK",
        1,
        R.drawable.stat_atk
    ),
    Substat(
        "DEF",
        1,
        R.drawable.stat_def
    ),
    Substat(
        "HP%",
        1,
        R.drawable.stat_hp
    ),
    Substat(
        "ATK%",
        1,
        R.drawable.stat_atk
    ),
    Substat(
        "DEF%",
        1,
        R.drawable.stat_def
    ),
    Substat(
        "Crit Rate",
        1,
        R.drawable.stat_critrate
    ),
    Substat(
        "Crit DMG",
        1,
        R.drawable.stat_critdmg
    ),
    Substat(
        "Effect HIT Rate",
        1,
        R.drawable.stat_effecthit
    ),
    Substat(
        "Effect RES",
        1,
        R.drawable.stat_effectres
    ),
    Substat(
        "Break Effect",
        1,
        R.drawable.stat_break
    ),
    Substat(
        "Speed",
        1,
        R.drawable.stat_speed
    )
)
