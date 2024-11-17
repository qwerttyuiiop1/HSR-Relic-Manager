package com.example.hsrrelicmanager.core.components

import com.example.hsrrelicmanager.model.relics.RelicSet

data class FilterItem(
    val title: String,
    val RelicSet: MutableList<RelicSet>,
    var levelNumber: Int,
    val rarityList: MutableList<String>
)
