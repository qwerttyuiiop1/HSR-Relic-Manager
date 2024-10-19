package com.example.hsrrelicmanager.model.relics

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.example.hsrrelicmanager.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class RelicSet (
    val name: String,
    @DrawableRes
    val icon: Int,
    val description: String,
    val type: Type = Type.CAVERN_RELICS
) : Parcelable {
    enum class Type {
        CAVERN_RELICS,
        PLANAR_ORNAMENTS,
    }
}

val relicSets = listOf(
    RelicSet(
        "Musketeer of Wild Wheat",
        R.drawable.musketeer_of_wild_wheat,
        "2-Pc: ATK increases by 12%.\n" +
                "4-Pc: The wearer's SPD increases by 6% and Basic ATK DMG increases by 10%."
    ),
    RelicSet(
        "Thief of Shooting Meteor",
        R.drawable.thief_of_shooting_meteor,
        "2-Piece Effect:\tIncreases Break Effect by 16%.\n" +
                "4-Piece Effect:\tIncreases the wearer's Break Effect by 16%. When the wearer inflicts Weakness Break on an enemy, regenerates 3 Energy."
    ),
    RelicSet(
        "Guard of Wuthering Snow",
        R.drawable.set_guard_of_wuthering_snow,
        "2 Piece: Reduces DMG taken by 8%.\n" +
                "4 Piece: At the beginning of the turn, if the wearer's HP is equal to or less than 50%, restores HP equal to 8% of their Max HP and regenerates 5 Energy."
    ),
    RelicSet(
        "Iron Cavalry Against the Scourge",
        R.drawable.set_iron_cavalry_against_the_scourge,
        "2 Piece: Increases Break Effect by 16%.\n" +
                "4 Piece: If the wearer's Break Effect is 150% or higher, the Break DMG dealt to the enemy target ignores 10% of their DEF. If the wearer's Break Effect is 250% or higher, the Super Break DMG dealt to the enemy target additionally ignores 15% of their DEF."
    ),
    RelicSet(
        "Pioneer Diver of Dead Waters",
        R.drawable.set_pioneer_diver_of_dead_waters,
        "2 Piece: Increases DMG dealt to enemies with debuffs by 12%.\n" +
                "4 Piece: Increases CRIT Rate by 4%. The wearer deals 8%/12% increased CRIT DMG to enemies with at least 2/3 debuffs. After the wearer inflicts a debuff on enemy targets, the aforementioned effects increase by 100%, lasting for 1 turn(s)."
    )
)