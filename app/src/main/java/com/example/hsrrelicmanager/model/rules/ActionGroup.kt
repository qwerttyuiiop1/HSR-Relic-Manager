package com.example.hsrrelicmanager.model.rules

import android.os.Parcelable
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

typealias FilterMap = MutableMap<Filter.Type, Filter>
@Parcelize
data class ActionGroup(
    var id: Long = -1,
    var filters: @RawValue FilterMap = mutableMapOf(),
    var position: Int = -1,
    var parentGroup: ActionGroup? = null,
    var groupList: MutableList<ActionGroup> = mutableListOf(),
    var action: @RawValue Action? = null
) : Parcelable {
    fun cloneDeep(): ActionGroup {
        return this.copy(
            groupList = groupList.map { it.cloneDeep() }.toMutableList(),
            filters = filters.mapValues { it.value.cloneDeep() }.toMutableMap()
        )
    }

    fun addGroup(child: ActionGroup) {
        child.parentGroup = this
        groupList.add(child)
    }

    fun getViewName() : String {
        if (action != null) {
            return action.toString()
        } else if (filters.isNotEmpty()) {
            return "Filter Group"
        }

        return "None"
    }

    fun getImageResource() : Int {
        if (action != null) {
            if (action is EnhanceAction) {
                return R.drawable.sticker_ppg_09_topaz_and_numby_03
            } else if (action is StatusAction) {
                return when ((action as StatusAction).targetStatus) {
                    Relic.Status.LOCK -> R.drawable.sticker_ppg_07_pom_pom_04
                    Relic.Status.TRASH -> R.drawable.sticker_ppg_12_other_01
                    Relic.Status.DEFAULT -> R.drawable.sticker_ppg_13_acheron_03
                    else -> 0
                }
            }
        } else if (filters.isNotEmpty()) {
            return R.drawable.sticker_ppg_11_other_01
        }
        return 0
    }

    override fun toString(): String {
        return "ActionGroup(position=$position, " +
            "action=$action)"
    }


}