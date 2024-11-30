package com.example.hsrrelicmanager.ui

import android.graphics.RenderEffect
import android.graphics.Shader
import android.view.View
import androidx.fragment.app.Fragment
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.group.ActionGroup

interface GroupChangeListener {
    fun onAddFilter(filter: Filter)
    fun onCancel()
    fun onChildChange(i: Int, group: ActionGroup)
    fun onChildDelete(i: Int, group: ActionGroup)
    fun onChildCreate(group: ActionGroup)
}

class GroupChangeHandler(
    val group: ActionGroup,
    var onFilterChanged: ((Filter) -> Unit)? = null
): GroupChangeListener {
    lateinit var fragment: Fragment

    override fun onAddFilter(filter: Filter) {
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
        this.onFilterChanged?.invoke(filter)
        if (ret) return
        group.filters[filter.filterType] = filter
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

    override fun onChildChange(i: Int, group: ActionGroup) {
        TODO("Not yet implemented")
    }

    override fun onChildDelete(i: Int, group: ActionGroup) {
        TODO("Not yet implemented")
    }

    override fun onChildCreate(group: ActionGroup) {
        TODO("Not yet implemented")
    }
}