package com.example.hsrrelicmanager.ui.rules

import android.graphics.RenderEffect
import android.graphics.Shader
import android.view.View
import androidx.fragment.app.Fragment
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.rules.ActionGroup
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.ui.MainActivity
import com.example.hsrrelicmanager.ui.db.DBManager
import com.example.hsrrelicmanager.ui.rules.modals.AddFilterDialog
import kotlin.reflect.KProperty

interface GroupChangeListener {
    fun onUpdateFilter(filter: Filter)
    fun onCancel()
    fun onChildChange(i: Int, group: ActionGroup)
    fun onChildDelete(i: Int, group: ActionGroup)
    fun onChildCreate(group: ActionGroup)
    fun onUpdateAction(action: Action?)
}

class GroupChangeHandler(
    var group: ActionGroup,
): GroupChangeListener {
    lateinit var fragment: Fragment
    val dbManager: DBManager by lazy {
        val activity = fragment.activity as MainActivity
        activity.dbManager
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): ActionGroup {
        return group
    }
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: ActionGroup) {
        this.group = value
    }

    override fun onUpdateFilter(filter: Filter) {
        var ret = false
        when (filter) {
            is Filter.SetFilter -> {
                if (filter.sets.isEmpty()) {
                    group.filters.remove(filter.filterType)
                    ret = true
                }
            }
            is Filter.SlotFilter -> {

                if (filter.types.isEmpty()) {
                    group.filters.remove(filter.filterType)
                    ret = true
                }
            }
            is Filter.MainStatFilter -> {
                if (filter.stats.isEmpty()) {
                    group.filters.remove(filter.filterType)
                    ret = true
                }
            }
            is Filter.SubStatFilter -> {
                if (filter.stats.isEmpty()) {
                    group.filters.remove(filter.filterType)
                    ret = true
                }
            }
            is Filter.RarityFilter -> {
                if (filter.rarities.isEmpty()) {
                    group.filters.remove(filter.filterType)
                    ret = true
                }
            }
            is Filter.StatusFilter -> {
                if (filter.statuses.isEmpty()) {
                    group.filters.remove(filter.filterType)
                    ret = true
                }

            }
            is Filter.LevelFilter -> {
            }
        }
        if (ret) return
        group.filters[filter.filterType] = filter
        dbManager.open()
        dbManager.updateGroup(group)
        dbManager.close()
    }

    override fun onCancel() {
        val dialog = AddFilterDialog(group.filters, this)
        dialog.show(fragment.parentFragmentManager, "AddFilterDialog")

        val activity = fragment.context as MainActivity
        val bgView = activity.findViewById<View>(R.id.activity_main_layout)
        bgView.setRenderEffect(
            RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
        )
    }

    override fun onUpdateAction(action: Action?) {
        group.action = action
        dbManager.open()
        dbManager.updateGroup(group)
        dbManager.close()
    }

    override fun onChildChange(i: Int, group: ActionGroup) {
        this.group.groupList[i] = group
        group.position = i
        dbManager.open()
        dbManager.updateGroup(group)
        dbManager.close()
    }

    override fun onChildDelete(i: Int, group: ActionGroup) {
        this.group.groupList.removeAt(i)
        val mgr = this.dbManager
        mgr.open()
        mgr.deleteGroup(group.id)
        for (i in this.group.groupList.indices) {
            this.group.groupList[i].position = i
            mgr.updateGroup(this.group.groupList[i])
        }
        mgr.close()
    }

    override fun onChildCreate(group: ActionGroup) {
        this.group.groupList.add(group)
        val mgr = this.dbManager
        mgr.open()
        mgr.insertGroup(group)
        for (i in this.group.groupList.indices) {
            this.group.groupList[i].position = i
            mgr.updateGroup(this.group.groupList[i])
        }
        mgr.close()
    }
}