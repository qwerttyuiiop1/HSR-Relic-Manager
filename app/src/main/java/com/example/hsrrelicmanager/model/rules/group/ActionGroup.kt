package com.example.hsrrelicmanager.model.rules.group

import android.os.Parcelable
import android.util.Log
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.EnumMap

@Parcelize
class ActionGroup(
    val id: Long = -1,
    var filters: @RawValue MutableMap<Filter.Type, Filter?> = EnumMap(Filter.Type.entries.associateWith { null }),
    var position: Int = -1,
    var parentGroup: ActionGroup? = null,
    var groupList: MutableList<ActionGroup> = mutableListOf(),
    var action: @RawValue Action? = null
) : Parcelable {
    fun checkActionToPerform(relic: Relic): Action? {
        for (filter in filters.values) {
            if (filter != null && !filter.accepts(relic)) {
                Log.d("ActionGroup", "Relic $relic not accepted by filter ${filter.description}")
                return null
            }
        }

        for (child in groupList) {
            val childAction = child.checkActionToPerform(relic)
            if (childAction != null) {
                return childAction
            }
        }

        return action
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