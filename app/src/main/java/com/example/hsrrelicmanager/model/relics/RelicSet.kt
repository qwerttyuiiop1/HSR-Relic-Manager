package com.example.hsrrelicmanager.model.relics

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.ext.norm
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
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
        "Belobog of the Architects",
        R.drawable.set_belobog_of_the_architects,
        "2-Pc: Increases the wearer's DEF by 15%. When the wearer's Effect Hit Rate is 50% or higher, the wearer gains an extra 15% DEF."
    ),
    RelicSet(
        "Broken Keel",
        R.drawable.set_broken_keel,
        "2-Pc: Increases the wearer's Effect RES by 10%. When the wearer's Effect RES is at 30% or higher, all allies' CRIT DMG increases by 10%."
    ),
    RelicSet(
        "Celestial Differentiator",
        R.drawable.set_celestial_differentiator,
        "2-Pc: Increases the wearer's CRIT DMG by 16%. When the wearer's current CRIT DMG reaches 120% or higher, after entering battle, the wearer's CRIT Rate increases by 60% until the end of their first attack."
    ),
    RelicSet(
        "Champion of Streetwise Boxing",
        R.drawable.set_champion_of_streetwise_boxing,
        "2-Pc: Increases Physical DMG by 10%.\n" +
                "4-Pc: After the wearer attacks or is hit, their ATK increases by 5% for the rest of the battle. This effect can stack up to 5 time(s)."
    ),
    RelicSet(
        "Duran, Dynasty of Running Wolves",
        R.drawable.set_duran_dynasty_of_running_wolves,
        "2-Pc: When an ally uses follow-up attacks, the wearer gains 1 stack of Merit, stacking up to 5 time(s). Each stack of Merit increases the DMG dealt by the wearer's follow-up attacks by 5%. When there are 5 stacks, additionally increases the wearer's CRIT DMG by 25%."
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
        "Firmament Frontline: Glamoth",
        R.drawable.set_firmament_frontline_glamoth,
        "2-Pc: Increases the wearer's ATK by 12%. When the wearer's SPD is equal to or higher than 135/160, the wearer deals 12%/18% more DMG."
    ),
    RelicSet(
        "Fleet of the Ageless",
        R.drawable.set_fleet_of_the_ageless,
        "2-Pc: Increases the wearer's Max HP by 12%. When the wearer's SPD reaches 120 or higher, all allies' ATK increases by 8%."
    ),

    RelicSet(
        "Forge of the Kalpagni Lantern",
        R.drawable.set_forge_of_the_kalpagni_lantern,
        "2-Pc: Increases the wearer's SPD by 6%. When the wearer hits an enemy target that has Fire Weakness, the wearer's Break Effect increases by 40%, lasting for 1 turn(s)."
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
        "Inert Salsotto",
        R.drawable.set_inert_salsotto,
        "2-Pc: Increases the wearer's CRIT Rate by 8%. When the wearer's current CRIT Rate reaches 50% or higher, the wearer's Ultimate and follow-up attack DMG increases by 15%."
    ),
    RelicSet(
        "Iron Cavalry Against the Scourge",
        R.drawable.set_iron_cavalry_against_the_scourge,
        "2 Piece: Increases Break Effect by 16%.\n" +
                "4 Piece: If the wearer's Break Effect is 150% or higher, the Break DMG dealt to the enemy target ignores 10% of their DEF. If the wearer's Break Effect is 250% or higher, the Super Break DMG dealt to the enemy target additionally ignores 15% of their DEF."
    ),
    RelicSet(
        "Izumo Gensei and Takama Divine Realm",
        R.drawable.set_izumo_gensei_and_takama_divine_realm,
        "2-Pc: Increases the wearer's ATK by 12%. When entering battle, if at least one other ally follows the same Path as the wearer, then the wearer's CRIT Rate increases by 12%."
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
        "Lushaka, the Sunken Seas",
        R.drawable.set_lushaka_the_sunken_seas,
        "2-Pc: Increases the wearer's Energy Regeneration Rate by 5%. If the wearer is not the first character in the team lineup, then increases the ATK of the first character in the team lineup by 12%."
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
        "Pan-Cosmic Commercial Enterprise",
        R.drawable.set_pan_cosmic_commercial_enterprise,
        "2-Pc: Increases the wearer's Effect Hit Rate by 10%. Meanwhile, the wearer's ATK increases by an amount that is equal to 25% of the current Effect Hit Rate, up to a maximum of 25%."
    ),
    RelicSet(
        "Passerby of Wandering Cloud",
        R.drawable.set_passerby_of_wandering_cloud,
        "2-Pc: Increases Outgoing Healing by 10%.\n" +
                "4-Pc: At the start of the battle, immediately regenerates 1 Skill Point."
    ),
    RelicSet(
        "Penacony, Land of the Dreams",
        R.drawable.set_penacony_land_of_the_dreams,
        "2-Pc: Increases wearer's Energy Regeneration Rate by 5%. Increases DMG by 10% for all other allies that are of the same Type as the wearer."
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
        "Rutilant Arena",
        R.drawable.set_rutilant_arena,
        "2-Pc: Increases the wearer's CRIT Rate by 8%. When the wearer's current CRIT Rate reaches 70% or higher, the wearer's Basic ATK and Skill DMG increase by 20%."
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
        "Sigonia, the Unclaimed Desolation",
        R.drawable.set_sigonia_the_unclaimed_desolation,
        "2-Pc: Increases the wearer's CRIT Rate by 4%. When an enemy target gets defeated, the wearer's CRIT DMG increases by 4%, stacking up to 10 time(s)."
    ),
    RelicSet(
        "Space Sealing Station",
        R.drawable.set_space_sealing_station,
        "2-Pc: Increases the wearer's ATK by 12%. When the wearer's SPD reaches 120 or higher, the wearer's ATK increases by an extra 12%."
    ),
    RelicSet(
        "Sprightly Vonwacq",
        R.drawable.set_sprightly_vonwacq,
        "2-Pc: Increases the wearer's Energy Regeneration Rate by 5%. When the wearer's SPD reaches 120 or higher, the wearer's action is Advanced Forward by 40% immediately upon entering battle."
    ),
    RelicSet(
        "Talia: Kingdom of Banditry",
        R.drawable.set_talia_kingdom_of_banditry,
        "2-Pc: Increases the wearer's Break Effect by 16%. When the wearer's SPD reaches 145 or higher, the wearer's Break Effect increases by an extra 20%."
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
        "The Wondrous BananAmusement Park",
        R.drawable.set_the_wondrous_bananamusement_park,
        "2-Pc: Increases the wearer's CRIT DMG by 16%. When a target summoned by the wearer is on the field, CRIT DMG additionally increases by 32%."
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

val relicNameToSet = listOf(
    "Band of Sizzling Thunder" to listOf(
        "Band's Polarized Sunglasses",
        "Band's Touring Bracelet",
        "Band's Leather Jacket With Studs",
        "Band's Ankle Boots With Rivets"
    )
    ,
    "Champion of Streetwise Boxing" to listOf(
        "Champion's Headgear",
        "Champion's Heavy Gloves",
        "Champion's Chest Guard",
        "Champion's Fleetfoot Boots"
    )
    ,
    "Eagle of Twilight Line" to listOf(
        "Eagle's Beaked Helmet",
        "Eagle's Soaring Ring",
        "Eagle's Winged Suit Harness",
        "Eagle's Quilted Puttees"
    )
    ,
    "Firesmith of Lava-Forging" to listOf(
        "Firesmith's Obsidian Goggles",
        "Firesmith's Ring of Flame-Mastery",
        "Firesmith's Fireproof Apron",
        "Firesmith's Alloy Leg"
    )
    ,
    "Genius of Brilliant Stars" to listOf(
        "Genius's Ultraremote Sensing Visor",
        "Genius's Frequency Catcher",
        "Genius's Metafield Suit",
        "Genius's Gravity Walker"
    )
    ,
    "Guard of Wuthering Snow" to listOf(
        "Guard's Cast Iron Helmet",
        "Guard's Shining Gauntlets",
        "Guard's Uniform of Old",
        "Guard's Silver Greaves"
    )
    ,
    "Hunter of Glacial Forest" to listOf(
        "Hunter's Artaius Hood",
        "Hunter's Lizard Gloves",
        "Hunter's Ice Dragon Cloak",
        "Hunter's Soft Elkskin Boots"
    )
    ,
    "Iron Cavalry Against the Scourge" to listOf(
        "Iron Cavalry's Homing Helm",
        "Iron Cavalry's Crushing Wristguard",
        "Iron Cavalry's Silvery Armor",
        "Iron Cavalry's Skywalk Greaves"
    )
    ,
    "Knight of Purity Palace" to listOf(
        "Knight's Forgiving Casque",
        "Knight's Silent Oath Ring",
        "Knight's Solemn Breastplate",
        "Knight's Iron Boots of Order"
    )
    ,
    "Longevous Disciple" to listOf(
        "Disciple's Prosthetic Eye",
        "Disciple's Ingenium Hand",
        "Disciple's Dewy Feather Garb",
        "Disciple's Celestial Silk Sandals"
    )
    ,
    "Messenger Traversing Hackerspace" to listOf(
        "Messenger's Holovisor",
        "Messenger's Transformative Arm",
        "Messenger's Secret Satchel",
        "Messenger's Par-kool Sneakers"
    )
    ,
    "Musketeer of Wild Wheat" to listOf(
        "Musketeer's Wild Wheat Felt Hat",
        "Musketeer's Coarse Leather Gloves",
        "Musketeer's Wind-Hunting Shawl",
        "Musketeer's Rivets Riding Boots"
    )
    ,
    "Passerby of Wandering Cloud" to listOf(
        "Passerby's Rejuvenated Wooden Hairstick",
        "Passerby's Roaming Dragon Bracer",
        "Passerby's Ragged Embroided Coat",
        "Passerby's Stygian Hiking Boots"
    )
    ,
    "Pioneer Diver of Dead Waters" to listOf(
        "Pioneer's Heatproof Shell",
        "Pioneer's Lacuna Compass",
        "Pioneer's Sealed Lead Apron",
        "Pioneer's Starfaring Anchor"
    )
    ,
    "Prisoner in Deep Confinement" to listOf(
        "Prisoner's Sealed Muzzle",
        "Prisoner's Leadstone Shackles",
        "Prisoner's Repressive Straitjacket",
        "Prisoner's Restrictive Fetters"
    )
    ,
    "Sacerdos' Relived Ordeal" to listOf(
        "Sacerdos' Melodic Earrings",
        "Sacerdos' Welcoming Gloves",
        "Sacerdos' Ceremonial Garb",
        "Sacerdos' Arduous Boots"
    )
    ,
    "Scholar Lost in Erudition" to listOf(
        "Scholar's Silver-Rimmed Monocle",
        "Scholar's Auxiliary Knuckle",
        "Scholar's Tweed Jacket",
        "Scholar's Felt Snowboots"
    )
    ,
    "The Ashblazing Grand Duke" to listOf(
        "Grand Duke's Crown of Netherflame",
        "Grand Duke's Gloves of Fieryfur",
        "Grand Duke's Robe of Grace",
        "Grand Duke's Ceremonial Boots"
    )
    ,
    "The Wind-Soaring Valorous" to listOf(
        "Valorous Mask of Northern Skies",
        "Valorous Bracelet of Grappling Hooks",
        "Valorous Plate of Soaring Flight",
        "Valorous Greaves of Pursuing Hunt"
    )
    ,
    "Thief of Shooting Meteor" to listOf(
        "Thief's Myriad-Faced Mask",
        "Thief's Gloves With Prints",
        "Thief's Steel Grappling Hook",
        "Thief's Meteor Boots"
    )
    ,
    "Wastelander of Banditry Desert" to listOf(
        "Wastelander's Breathing Mask",
        "Wastelander's Desert Terminal",
        "Wastelander's Friar Robe",
        "Wastelander's Powered Greaves"
    )
    ,
    "Watchmaker, Master of Dream Machinations" to listOf(
        "Watchmaker's Telescoping Lens",
        "Watchmaker's Fortuitous Wristwatch",
        "Watchmaker's Illusory Formal Suit",
        "Watchmaker's Dream-Concealing Dress Shoes"
    )
    ,
    "Belobog of the Architects" to listOf(
        "Belobog's Fortress of Preservation",
        "Belobog's Iron Defense"
    )
    ,
    "Broken Keel" to listOf(
        "Insumousu's Whalefall Ship",
        "Insumousu's Frayed Hawser"
    )
    ,
    "Celestial Differentiator" to listOf(
        "Planet Screwllum's Mechanical Sun",
        "Planet Screwllum's Ring System"
    )
    ,
    "Duran, Dynasty of Running Wolves" to listOf(
        "Duran's Tent of Golden Sky",
        "Duran's Mechabeast Bridle"
    )
    ,
    "Firmament Frontline: Glamoth" to listOf(
        "Glamoth's Iron Cavalry Regiment",
        "Glamoth's Silent Tombstone"
    )
    ,
    "Fleet of the Ageless" to listOf(
        "The Xianzhou Luofu's Celestial Ark",
        "The Xianzhou Luofu's Ambrosial Arbor Vines"
    )
    ,
    "Forge of the Kalpagni Lantern" to listOf(
        "Forge's Lotus Lantern Wick",
        "Forge's Heavenly Flamewheel Silk"
    )
    ,
    "Inert Salsotto" to listOf(
        "Salsotto's Moving City",
        "Salsotto's Terminator Line"
    )
    ,
    "Izumo Gensei and Takama Divine Realm" to listOf(
        "Izumo's Magatsu no Morokami",
        "Izumo's Blades of Origin and End"
    )
    ,
    "Lushaka, the Sunken Seas" to listOf(
        "Lushaka's Twinlanes",
        "Lushaka's Waterscape"
    )
    ,
    "Pan-Cosmic Commercial Enterprise" to listOf(
        "The IPC's Mega HQ",
        "The IPC's Trade Route"
    )
    ,
    "Penacony, Land of the Dreams" to listOf(
        "Penacony's Grand Hotel",
        "Penacony's Dream-Seeking Tracks"
    )
    ,
    "Rutilant Arena" to listOf(
        "Taikiyan Laser Stadium",
        "Taikiyan's Arclight Race Track"
    )
    ,
    "Sigonia, the Unclaimed Desolation" to listOf(
        "Sigonia's Gaiathra Berth",
        "Sigonia's Knot of Cyclicality"
    )
    ,
    "Space Sealing Station" to listOf(
        "Herta's Space Station",
        "Herta's Wandering Trek"
    )
    ,
    "Sprightly Vonwacq" to listOf(
        "Vonwacq's Island of Birth",
        "Vonwacq's Islandic Coast"
    )
    ,
    "Talia: Kingdom of Banditry" to listOf(
        "Talia's Nailscrap Town",
        "Talia's Exposed Electric Wire"
    )
    ,
    "The Wondrous BananAmusement Park" to listOf(
        "BananAmusement Park's Memetic Cables",
        "BananAmusement Park's BananAxis Plaza"
    )
).let { list ->
    val nameToSet = relicSets.associateBy { it.name }
    list.flatMap { (set, items) ->
        items.map { it.norm to nameToSet[set]!! }
    }.toMap()
}