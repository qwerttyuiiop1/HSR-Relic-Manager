package com.example.hsrrelicmanager.model

import android.os.Parcelable
import com.example.hsrrelicmanager.R
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize@Serializable
data class Substat(
    val name: String,
    val image: Int
): Parcelable{}


val substatSets = listOf(
    Substat(
        "HP",
        R.drawable.stat_hp
    ),
    Substat(
        "ATK",
        R.drawable.stat_atk
    ),
    Substat(
        "DEF",
        R.drawable.stat_def
    ),
    Substat(
        "HP%",
        R.drawable.stat_hp
    ),
    Substat(
        "ATK%",
        R.drawable.stat_atk
    ),
    Substat(
        "DEF%",
        R.drawable.stat_def
    ),
    Substat(
        "Crit Rate",
        R.drawable.stat_critrate
    ),
    Substat(
        "Crit DMG",
        R.drawable.stat_critdmg
    ),
    Substat(
        "Effect HIT Rate",
        R.drawable.stat_effecthit
    ),
    Substat(
        "Effect RES",
        R.drawable.stat_effectres
    ),
    Substat(
        "Break Effect",
        R.drawable.stat_break
    ),
    Substat(
        "Speed",
        R.drawable.stat_speed
    )
)
