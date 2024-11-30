package com.example.hsrrelicmanager.model.rules.action

import com.example.hsrrelicmanager.model.relics.Relic

class StatusAction(@JvmField var targetStatus: Relic.Status) : Action() {
    override val name: String
        get() = toString()
    override fun apply(relic: Relic?): Relic? {
        throw UnsupportedOperationException("Not implemented yet")
    }

    override fun toString(): String {
        return when (targetStatus) {
            Relic.Status.LOCK -> "Lock"
            Relic.Status.TRASH -> "Trash"
            Relic.Status.DEFAULT -> "Reset"
            Relic.Status.EQUIPPED -> "Equip"
            Relic.Status.UPGRADE -> "Upgrade"
        }
    }
}
