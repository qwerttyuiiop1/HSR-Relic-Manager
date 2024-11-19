package com.example.hsrrelicmanager.core.components

import com.example.hsrrelicmanager.model.Status
import com.example.hsrrelicmanager.model.relics.RelicSet

data class FilterItem(
    val title: String,
    val RelicSet: MutableList<RelicSet>,
    var levelNum: Int,
    var levelType: Boolean,
    val rarityList: MutableList<String>,
    val statusList: MutableList<Status>
)
