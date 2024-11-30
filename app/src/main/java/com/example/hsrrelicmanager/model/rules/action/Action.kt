package com.example.hsrrelicmanager.model.rules.action

import com.example.hsrrelicmanager.model.relics.Relic

sealed class Action {
    abstract val name: String
    abstract fun apply(relic: Relic?): Relic?
}
