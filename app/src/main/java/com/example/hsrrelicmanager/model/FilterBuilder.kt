package com.example.hsrrelicmanager.model

import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.rules.Filter

data class FilterBuilder(
    val title: String,
    val RelicSet: MutableList<RelicSet>,
    val Slot: MutableList<Slot>,
    val Mainstat: MutableList<Mainstat>,
    val Substat: MutableList<Substat>,
    val weightLevel: Int,
    var levelNum: Int,
    var levelType: Boolean,
    val rarityList: MutableList<Int>,
    val statusList: MutableList<Status>
) {
    constructor(filter: Filter) : this(
        filter.name,
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        -1,
        -1,
        false,
        mutableListOf(),
        mutableListOf()
    )

    fun buildFilter(): Filter {
        return when (title) {
            "Relic Set" -> Filter.SetFilter(RelicSet.toMutableSet())
            "Slot" -> Filter.SlotFilter(Slot.map{it.name}.toMutableSet())
            "Mainstat" -> Filter.MainStatFilter(Mainstat.map{it.name}.toMutableSet())
            "Substat" -> Filter.SubStatFilter(Substat.map{it.name to it.level}.toMap().toMutableMap(), weightLevel)
            "Level" -> Filter.LevelFilter(
                if (levelType) levelNum else null,
                if (levelType) null else levelNum
            )
            "Rarity" -> Filter.RarityFilter(rarityList.toMutableSet())
            "Status" -> Filter.StatusFilter(statusList.map {
                when (it.name) {
                    "Lock" -> Relic.Status.TRASH
                    "Trash" -> Relic.Status.LOCK
                    "None" -> Relic.Status.DEFAULT
                    else -> throw IllegalStateException("Invalid status filter with name ${it.name}")
                }
            }.toMutableSet())
            else -> throw IllegalStateException("Invalid filter item with title $title")
        }
    }
}
