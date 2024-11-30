package com.example.hsrrelicmanager.ui

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.FragmentActionGroupBodyBinding
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import com.example.hsrrelicmanager.model.rules.group.FilterMap

interface AddFilterListener {
    fun onAddFilter(filter: Filter)
    fun onCancel()
}
class AddActionGroupBodyFragment(
    private val group: ActionGroup
): Fragment(), AddFilterListener {
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
        adapterFilter.notifyDataSetChanged()
        if (ret) return
        group.filters[filter.filterType] = filter
    }

    override fun onCancel() {
        val dialog = AddFilterDialog(group.filters, this)
        dialog.show(parentFragmentManager, "AddFilterDialog")

        val activity = context as MainActivity
        val bgView = activity.findViewById<View>(R.id.activity_main_layout)
        bgView.setRenderEffect(
            RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
        )
    }

    private var _binding: FragmentActionGroupBodyBinding? = null
    private val binding get() = _binding!!

    val filters: FilterMap = mutableMapOf()
    private lateinit var adapterFilter: FilterAdapter

    private val actionItems = mutableListOf("")
    private lateinit var adapterAction: ActionItemAdapter

    private var RelicTracker = 0
    private var SlotTracker = 0
    private var MainstatTracker = 0
    private var SubstatTracker = 0
    private var RarityTracker = 0
    private var LevelTracker = 0
    private var StatusTracker = 0
    private var index = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionGroupBodyBinding.inflate(inflater, container, false)

        binding.apply {
            // Initialize Action Adapter
            adapterAction = ActionItemAdapter(actionItems)
            recyclerViewActionGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewActionGroup.adapter = adapterAction

            // Initialize Filter Adapter
            adapterFilter = FilterAdapter(filters, this@AddActionGroupBodyFragment)
            recyclerViewFilterGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewFilterGroup.adapter = adapterFilter

            // Add Filter Button Click Listener
            filterSectonAdd.setOnClickListener {
                val dialog = AddFilterDialog(filters, this@AddActionGroupBodyFragment)
                dialog.show(parentFragmentManager, "AddFilterDialog")
                val activity = context as MainActivity
                val bgView = activity.findViewById<View>(R.id.activity_main_layout)
                bgView.setRenderEffect(
                    RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
                )
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
