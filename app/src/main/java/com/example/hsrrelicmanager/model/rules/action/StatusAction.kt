package com.example.hsrrelicmanager.model.rules.action

import com.example.hsrrelicmanager.model.relics.Relic

class StatusAction(@JvmField var targetStatus: Relic.Status) : Action() {
    override val image: Int
        get() = targetStatus.image
    override val name: String
        get() = targetStatus.str
    override fun apply(relic: Relic?): Relic? {
        throw UnsupportedOperationException("Not implemented yet")
    }

    override fun toString() = targetStatus.str
}
