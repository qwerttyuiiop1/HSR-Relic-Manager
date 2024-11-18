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
        "Band of Sizzling Thunder",
        R.drawable.set_band_of_sizzling_thunder,
        "2-Pc: Increases Lightning DMG by 10%.\n" +
                "4-Pc: When the wearer uses their Skill, increases the wearer's ATK by 20% for 1 turn(s)."
    ),
    RelicSet(
        "Champion of Streetwise Boxing",
        R.drawable.set_champion_of_streetwise_boxing,
        "2-Pc: Increases Physical DMG by 10%.\n" +
                "4-Pc: After the wearer attacks or is hit, their ATK increases by 5% for the rest of the battle. This effect can stack up to 5 time(s)."
    ),
    RelicSet(
        "Eagle of Twilight Line",
        R.drawable.set_eagle_of_twilight_line,
        "2-Pc: Increases Wind DMG by 10%.\n" +
                "4-Pc: After the wearer uses their Ultimate, their action is Advanced Forward by 25%."
    ),
    RelicSet(
        "Firesmith of Lava-Forging",
        R.drawable.set_firesmith_of_lava_forging,
        "2-Pc: Increases Fire DMG by 10%.\n" +
                "4-Pc: Increases the wearer's Skill DMG by 12%. After unleashing Ultimate, increases the wearer's Fire DMG by 12% for the next attack."
    ),
    RelicSet(
        "Genius of Brilliant Stars",
        R.drawable.set_genius_of_brilliant_stars,
        "2-Pc: Increases Quantum DMG by 10%.\n" +
                "4-Pc: When the wearer deals DMG to the target enemy, ignores 10% DEF. If the target enemy has Quantum Weakness, the wearer additionally ignores 10% DEF."
    ),
    RelicSet(
        "Guard of Wuthering Snow",
        R.drawable.set_guard_of_wuthering_snow,
        "2 Piece: Reduces DMG taken by 8%.\n" +
                "4 Piece: At the beginning of the turn, if the wearer's HP is equal to or less than 50%, restores HP equal to 8% of their Max HP and regenerates 5 Energy."
    ),
    RelicSet(
        "Hunter of Glacial Forest",
        R.drawable.set_hunter_of_glacial_forest,
        "2-Pc: Increases Ice DMG by 10%.\n" +
                "4-Pc: After the wearer uses their Ultimate, their CRIT DMG increases by 25% for 2 turn(s)."
    ),
    RelicSet(
        "Iron Cavalry Against the Scourge",
        R.drawable.set_iron_cavalry_against_the_scourge,
        "2 Piece: Increases Break Effect by 16%.\n" +
                "4 Piece: If the wearer's Break Effect is 150% or higher, the Break DMG dealt to the enemy target ignores 10% of their DEF. If the wearer's Break Effect is 250% or higher, the Super Break DMG dealt to the enemy target additionally ignores 15% of their DEF."
    ),
    RelicSet(
        "Knight of Purity Palace",
        R.drawable.set_knight_of_purity_palace,
        "2-Pc: Increases DEF by 15%.\n" +
                "4-Pc: Increases the max DMG that can be absorbed by the Shield created by the wearer by 20%."
    ),
    RelicSet(
        "Longevous Disciple",
        R.drawable.set_longevous_disciple,
        "2-Pc: Increases Max HP by 12%.\n" +
                "4-Pc: When the wearer is hit or has their HP consumed by an ally or themselves, their CRIT Rate increases by 8% for 2 turn(s) and up to 2 stacks."
    ),
    RelicSet(
        "Messenger Traversing Hackerspace",
        R.drawable.set_messenger_traversing_hackerspace,
        "2-Pc: Increases SPD by 6%.\n" +
                "4-Pc: When the wearer uses their Ultimate on an ally, SPD for all allies increases by 12% for 1 turn(s). This effect cannot be stacked."
    ),
    RelicSet(
        "Musketeer of Wild Wheat",
        R.drawable.musketeer_of_wild_wheat,
        "2-Pc: ATK increases by 12%.\n" +
                "4-Pc: The wearer's SPD increases by 6% and Basic ATK DMG increases by 10%."
    ),
    RelicSet(
        "Passerby of Wandering Cloud",
        R.drawable.set_passerby_of_wandering_cloud,
        "2-Pc: Increases Outgoing Healing by 10%.\n" +
                "4-Pc: At the start of the battle, immediately regenerates 1 Skill Point."
    ),
    RelicSet(
        "Pioneer Diver of Dead Waters",
        R.drawable.set_pioneer_diver_of_dead_waters,
        "2 Piece: Increases DMG dealt to enemies with debuffs by 12%.\n" +
                "4 Piece: Increases CRIT Rate by 4%. The wearer deals 8%/12% increased CRIT DMG to enemies with at least 2/3 debuffs. After the wearer inflicts a debuff on enemy targets, the aforementioned effects increase by 100%, lasting for 1 turn(s)."
    ),
    RelicSet(
        "Prisoner in Deep Confinement",
        R.drawable.set_prisoner_in_deep_confinement,
        "2-Pc: ATK increases by 12%.\n" +
                "4-Pc: For every DoT the target enemy is afflicted with, the wearer will ignore 6% of its DEF when dealing DMG to it. This effect is valid for a max of 3 DoTs."
    ),
    RelicSet(
        "Sacerdos' Relived Ordeal",
        R.drawable.set_sacerdos_relived_ordeal,
        "2-Pc: SPD increases by 6%.\n" +
                "4-Pc: When using Skill or Ultimate on one ally target, increases the ability target's CRIT DMG by 18%, lasting for 2 turns. This effect can stack up to 2 times."
    ),
    RelicSet(
        "Scholar Lost in Erudition",
        R.drawable.set_scholar_lost_in_erudition,
        "2-Pc: CRIT Rate increases by 8%.\n" +
                "4-Pc: Increases DMG dealt by Ultimate and Skill by 20%. After using Ultimate, additionally increases the DMG dealt by the next Skill by 25%."
    ),
    RelicSet(
        "The Ashblazing Grand Duke",
        R.drawable.set_the_ashblazing_grand_duke,
        "2-Pc: Increases the DMG dealt by follow-up attacks by 20%.\n" +
                "4-Pc: When the wearer uses follow-up attacks, increases the wearer's ATK by 6% for every time the follow-up attack deals DMG. This effect can stack up to 8 time(s) and lasts for 3 turn(s). This effect is removed the next time the wearer uses a follow-up attack."
    ),
    RelicSet(
        "The Wind-Soaring Valorous",
        R.drawable.set_the_wind_soaring_valorous,
        "2-Pc: Increases ATK by 12%.\n" +
                "4-Pc: Increases the wearer's CRIT Rate by 6%. When the wearer uses a follow-up attack, increases the DMG dealt by Ultimate by 36%, lasting for 1 turn(s)."
    ),
    RelicSet(
        "Thief of Shooting Meteor",
        R.drawable.thief_of_shooting_meteor,
        "2-Piece Effect:\tIncreases Break Effect by 16%.\n" +
                "4-Piece Effect:\tIncreases the wearer's Break Effect by 16%. When the wearer inflicts Weakness Break on an enemy, regenerates 3 Energy."
    ),
    RelicSet(
        "Wastelander of Banditry Desert",
        R.drawable.set_wastelander_of_banditry_desert,
        "2-Pc: Increases Imaginary DMG by 10%.\n" +
                "4-Pc: When attacking debuffed enemies, the wearer's CRIT Rate increases by 10%, and their CRIT DMG increases by 20% against Imprisoned enemies."
    ),
    RelicSet(
        "Watchmaker, Master of Dream Machinations",
        R.drawable.set_watchmaker_master_of_dream_machinations,
        "2-Pc: Increases Break Effect by 16%.\n" +
                "4-Pc: When the wearer uses their Ultimate on an ally, all allies' Break Effect increases by 30% for 2 turn(s). This effect cannot be stacked."
    ),
)