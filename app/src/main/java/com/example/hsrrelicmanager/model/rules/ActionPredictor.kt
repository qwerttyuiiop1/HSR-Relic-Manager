package com.example.hsrrelicmanager.model.rules

import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction

class ActionPredictor(
    val actionGroups: List<ActionGroup>,
    val manualActions: Map<Relic, List<Relic.Status>>
) {
    fun predict(relic: Relic): List<Action>? {
        val manualAction = manualActions[relic]
        if (manualAction != null) {
            return manualAction.map {
                if (it == Relic.Status.UPGRADE) {
                    EnhanceAction(-1)
                } else {
                    StatusAction(it)
                }
            }
        }
        return predict(actionGroups, relic)?.let {
            listOf(it)
        }
    }
    fun predict(actionGroups: List<ActionGroup>, relic: Relic): Action? {
        for (group in actionGroups) {
            val action = predict(group, relic)
            if (action != null) {
                return action
            }
        }
        return null
    }
    fun predict(group: ActionGroup, relic: Relic): Action? {
        for (filter in group.filters.values) {
            if (!filter.accepts(relic))
                return null
        }
        var res = predict(group.groupList, relic)
        if (res != null) {
            return res
        }
        res = group.action
        if (res != null && res is EnhanceAction) {
            var targetLevel = res.targetLevel
            if (targetLevel <= 0)
                targetLevel = (relic.level/3 + 1)*3
            targetLevel = targetLevel.coerceAtMost(relic.rarity * 3)
            targetLevel = targetLevel.coerceAtLeast(relic.level)
            res = EnhanceAction(targetLevel)
        }
        return res
    }
}