package com.example.hsrrelicmanager.model.relics

import android.os.Parcelable
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.substatSets
import kotlinx.parcelize.Parcelize


@Parcelize
data class Relic (
    val id: Long,
    val set: RelicSet,
    val slot: String,
    val rarity: Int,
    val level: Int,
    val mainstat: String,
    val mainstatVal: String,
    val substats: Map<String, String>,
    val status: List<Status>,
    val prev: Relic? = null
): Parcelable {
    enum class Status {
        LOCK, TRASH, DEFAULT, // mutually exclusive
        UPGRADE, EQUIPPED, // can be combined with others
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
        return substatSets.find { it.name == s }?.image ?: R.drawable.stat_hp
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
    var slot: String = "",
    var rarity: Int = 0,
    var level: Int = 0,
    var mainstat: String = "",
    var mainstatVal: String = "",
    var msubstats: MutableMap<String, String> = mutableMapOf(),
    var mstatus: MutableList<Relic.Status> = mutableListOf(),
    prev: RelicBuilder? = null,
    val isPrev: Boolean = false,
) {
    constructor(r: Relic, isPrev: Boolean = false): this(
        r.id,
        r.set,
        r.slot,
        r.rarity,
        r.level,
        r.mainstat,
        r.mainstatVal,
        r.substats.toMutableMap(),
        r.status.toMutableList(),
        if (isPrev) {
            null
        } else {
            if (r.prev == null) RelicBuilder(r, true)
            else RelicBuilder(r.prev, true)
        },
        isPrev
    )
    constructor(r: RelicBuilder, isPrev: Boolean): this(
        r.id,
        r.set,
        r.slot,
        r.rarity,
        r.level,
        r.mainstat,
        r.mainstatVal,
        r.msubstats.toMutableMap(),
        r.mstatus.toMutableList(),
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
        val prev = if (isPrev) null else this.prev.build()
        return Relic(id, set, slot, rarity, level, mainstat, mainstatVal, msubstats, mstatus, prev)
    }
}