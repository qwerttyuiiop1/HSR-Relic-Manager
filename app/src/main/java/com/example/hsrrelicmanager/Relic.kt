package com.example.hsrrelicmanager

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Relic (
    val set: String,
    val slot: String,
    val rarity: Int,
    val level: Int,
    val mainstat: String,
    val mainstatVal: String,
    val substats: Map<String, String>,
    val status: List<Status>,
    val image: Int,
    val prev: Relic? = null
): Parcelable {
    enum class Status {
        LOCK, TRASH, UPGRADE, EQUIPPED
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
        get() = substatResource(mainstat)

    fun substatResource(s: String): Int {
        return when (s) {
            "ATK", "ATK%" -> R.drawable.icon_atk
            "DEF" -> R.drawable.icon_def
            "SPD" -> R.drawable.icon_spd
            "CRIT Rate" -> R.drawable.icon_crit_rate
            else -> throw IllegalArgumentException("Invalid stat")
        }
    }
    fun statusResource(s: Status): Int {
        return when (s) {
            Status.LOCK -> R.drawable.ic_lock
            Status.TRASH -> R.drawable.ic_trash
            Status.EQUIPPED -> R.drawable.ic_relic_equipped
            Status.UPGRADE -> R.drawable.enhance
        }
    }
}

class RelicBuilder constructor(
    var set: String = "",
    var slot: String = "",
    var rarity: Int = 0,
    var level: Int = 0,
    var mainstat: String = "",
    var mainstatVal: String = "",
    var msubstats: MutableMap<String, String> = mutableMapOf(),
    var mstatus: MutableList<Relic.Status> = mutableListOf(),
    var image: Int = 0,
    prev: RelicBuilder? = null,
    private val isPrev: Boolean = false,
) {
    constructor(r: Relic, isPrev: Boolean = false): this(
        r.set,
        r.slot,
        r.rarity,
        r.level,
        r.mainstat,
        r.mainstatVal,
        r.substats.toMutableMap(),
        r.status.toMutableList(),
        r.image,
        if (isPrev) {
            null
        } else {
            if (r.prev == null) RelicBuilder(r, true)
            else RelicBuilder(r.prev, true)
        },
        isPrev
    )
    constructor(r: RelicBuilder, isPrev: Boolean): this(
        r.set,
        r.slot,
        r.rarity,
        r.level,
        r.mainstat,
        r.mainstatVal,
        r.msubstats.toMutableMap(),
        r.mstatus.toMutableList(),
        r.image,
        if (isPrev) {
            null
        } else {
            if (r._prev == null) RelicBuilder(r, true)
            else RelicBuilder(r._prev!!, true)
        },
        isPrev,
    )

    private var _prev = prev
    init {
        if (isPrev && prev != null)
            throw IllegalAccessException("Cannot set prev on prev")
    }
    var prev: RelicBuilder
        get() = _prev!!
        set(value) {
            if (isPrev) throw IllegalAccessException("Cannot set prev on prev")
            _prev = value
        }

    var substats: Map<String, String>
        get() = msubstats
        set(value) {
            msubstats = value.toMutableMap()
        }
    var status: List<Relic.Status>
        get() = mstatus
        set(value) {
            mstatus = value.toMutableList()
        }

    fun build(): Relic {
        val prev = if (isPrev) null else this.prev?.build()
        return Relic(set, slot, rarity, level, mainstat, mainstatVal, msubstats, mstatus, image, prev)
    }
}