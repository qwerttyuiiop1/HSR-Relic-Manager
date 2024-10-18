package com.example.hsrrelicmanager

data class Relic (
    val set: String,
    val slot: String,
    val rarity: Int,
    val level: Int,
    val mainstat: String,
    val mainstatVal: String,
    val substats: Map<String, Double>,
    val status: List<Status>,
    val image: Int
) {
    enum class Status {
        LOCK, TRASH, UPGRADE
    }
}