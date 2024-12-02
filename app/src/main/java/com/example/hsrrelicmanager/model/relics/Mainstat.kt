package com.example.hsrrelicmanager.model.relics

import android.os.Parcelable
import com.example.hsrrelicmanager.R
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize@Serializable
data class Mainstat(
    val name: String,
    val image: Int
): Parcelable{
    companion object {
        fun fromName(s: String): Mainstat? {
            return mainstatSets.find { it.name == s }
        }
    }
}

val mainstatSets = listOf(
    Mainstat(
        "HP",
        R.drawable.stat_hp
    ),
    Mainstat(
        "ATK",
        R.drawable.stat_atk
    ),
    Mainstat(
        "HP%",
        R.drawable.stat_hp
    ),
    Mainstat(
        "ATK%",
        R.drawable.stat_atk
    ),
    Mainstat(
        "DEF%",
        R.drawable.stat_def
    ),
    Mainstat(
        "CRIT Rate",
        R.drawable.stat_critrate
    ),
    Mainstat(
        "CRIT DMG",
        R.drawable.stat_critdmg
    ),
    Mainstat(
        "Outgoing Healing Boost",
        R.drawable.stat_outgoing_healing
    ),
    Mainstat(
        "Effect HIT Rate",
        R.drawable.stat_effecthit
    ),
    Mainstat(
        "SPD",
        R.drawable.stat_speed
    ),
    Mainstat(
        "Physical DMG",
        R.drawable.stat_phydmg
    ),
    Mainstat(
        "Fire DMG",
        R.drawable.stat_firedmg
    ),
    Mainstat(
        "Ice DMG",
        R.drawable.stat_icedmg
    ),
    Mainstat(
        "Lightning DMG",
        R.drawable.stat_lightningdmg
    ),
    Mainstat(
        "Wind DMG",
        R.drawable.stat_winddmg
    ),
    Mainstat(
        "Quantum DMG",
        R.drawable.stat_quantumdmg
    ),
    Mainstat(
        "Imaginary DMG",
        R.drawable.stat_imaginarydmg
    ),
    Mainstat(
        "Break Effect",
        R.drawable.stat_break
    ),
    Mainstat(
        "Energy Regeneration Rate",
        R.drawable.stat_energy
    ),
)