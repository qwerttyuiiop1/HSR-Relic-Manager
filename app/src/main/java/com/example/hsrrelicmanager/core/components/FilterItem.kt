package com.example.hsrrelicmanager.core.components

import com.example.hsrrelicmanager.model.Mainstat
import com.example.hsrrelicmanager.model.Slot
import com.example.hsrrelicmanager.model.Substat
import com.example.hsrrelicmanager.model.Status
import com.example.hsrrelicmanager.model.relics.RelicSet

data class FilterItem(
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
)
