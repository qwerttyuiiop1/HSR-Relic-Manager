package com.example.hsrrelicmanager.model

import android.os.Parcelable
import com.example.hsrrelicmanager.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mainstat(
    val name: String,
    val image: Int
): Parcelable{}

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
        "Crit Rate",
        R.drawable.stat_critrate
    ),
    Mainstat(
        "Crit DMG",
        R.drawable.stat_critdmg
    ),
    Mainstat(
        "Outgoing Healing",
        R.drawable.stat_outgoing_healing
    ),
    Mainstat(
        "Effect HIT Rate",
        R.drawable.stat_effecthit
    ),
    Mainstat(
        "Speed",
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
        "Energy Regen Rate",
        R.drawable.stat_energy
    ),
)