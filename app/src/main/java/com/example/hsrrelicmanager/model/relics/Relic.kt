package com.example.hsrrelicmanager.model.relics

import android.os.Parcelable
import com.example.hsrrelicmanager.R
import kotlinx.parcelize.Parcelize


@Parcelize
data class Relic (
    val id: Long,
    val set: RelicSet,
    val slot: Slot,
    val rarity: Int,
    val level: Int,
    val mainstat: Mainstat,
    val mainstatVal: String,
    val substats: Set<Substat>,
    val status: List<Status>,
//    val prev: Relic? = null
): Parcelable {
    /**
     * tus("Enhance", R.drawable.enhance),
     *     Status("Lock", R.drawable.lock),
     *     Status("Reset", R.drawable.reset),
     *     Status("Trash", R.drawable.trash),
     * //    Status("Filter Group", R.drawable.filter_group)
     */
    enum class Status(
        val str: String,
        val image: Int
    ) {
        LOCK(
            "Lock", R.drawable.lock
        ),
        TRASH(
            "Trash", R.drawable.trash
        ),
        DEFAULT(
            "Reset", R.drawable.reset
        ), // mutually exclusive
        UPGRADE(
            "Enhance", R.drawable.enhance
        ),
        EQUIPPED(
            "Equip", R.drawable.ic_relic_equipped
        )
    }

    val rarityResource: Int
        get() = when (rarity) {
            1 -> R.drawable.bg_1_star
            2 -> R.drawable.bg_2_star
            3 -> R.drawable.bg_3_star
            4 -> R.drawable.bg_4_star
            5 -> R.drawable.bg_5_star
            else -> throw IllegalArgumentException("Invalid rarity")
        }

    val mainstatResource: Int
        get() = substatResource(mainstat.name)

    fun substatResource(s: String): Int {
        return substatSets.find { it.name == s }?.image!!
    }
    fun statusResource(s: Status): Int {
        return when (s) {
            Status.LOCK -> R.drawable.ic_lock
            Status.TRASH -> R.drawable.ic_trash
            Status.EQUIPPED -> R.drawable.ic_relic_equipped
            Status.UPGRADE -> R.drawable.enhance
            Status.DEFAULT -> R.drawable.reset
        }
    }

    val builder get() = RelicBuilder(this)
}

class RelicBuilder(
    var id: Long = 0,
    var set: RelicSet = RelicSet("", 0, ""),
    var slot: Slot? = null,
    var rarity: Int = 0,
    var level: Int = 0,
    var mainstat: Mainstat? = null,
    var mainstatVal: String = "",
    var msubstats: MutableSet<Substat> = mutableSetOf(),
    var mstatus: MutableList<Relic.Status> = mutableListOf(),
) {
    constructor(r: Relic): this(
        r.id,
        r.set,
        r.slot,
        r.rarity,
        r.level,
        r.mainstat,
        r.mainstatVal,
        r.substats.toMutableSet(),
        r.status.toMutableList(),
    )
    constructor(r: RelicBuilder): this(
        r.id,
        r.set,
        r.slot,
        r.rarity,
        r.level,
        r.mainstat,
        r.mainstatVal,
        r.msubstats.toMutableSet(),
        r.mstatus.toMutableList()
    )

    var substats: Set<Substat>
        get() = msubstats
        set(value) {
            msubstats = value.toMutableSet()
        }
    var status: List<Relic.Status>
        get() = mstatus
        set(value) {
            mstatus = value.toMutableList()
        }

    fun build(): Relic {
        return Relic(id, set, slot!!, rarity, level, mainstat!!, mainstatVal, msubstats, mstatus)
    }
}