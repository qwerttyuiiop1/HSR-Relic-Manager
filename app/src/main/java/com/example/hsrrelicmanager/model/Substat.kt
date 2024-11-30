package com.example.hsrrelicmanager.model

import android.os.Parcelable
import com.example.hsrrelicmanager.R
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize@Serializable
data class Substat(
    val name: String,
    val image: Int,
    var value: String = "0"
): Parcelable {
    companion object {
        fun fromName(name: String): Substat? {
            return substatSets.find { it.name == name }
        }
    }
}


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
        "CRIT Rate",
        R.drawable.stat_critrate
    ),
    Substat(
        "CRIT DMG",
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
        "SPD",
        R.drawable.stat_speed
    )
)
