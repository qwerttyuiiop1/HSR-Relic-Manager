package com.example.hsrrelicmanager.model.rules.action

import com.example.hsrrelicmanager.model.relics.Relic

class EnhanceAction(var targetLevel: Int) : Action() {
    override val name = "Enhance"
    override fun apply(relic: Relic?): Relic {
        throw UnsupportedOperationException("Not implemented yet")
    }

    override fun toString(): String {
        return "Enhance (Lvl $targetLevel)"
    }
}