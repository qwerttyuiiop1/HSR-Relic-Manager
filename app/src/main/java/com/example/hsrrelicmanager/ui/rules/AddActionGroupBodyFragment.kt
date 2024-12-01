package com.example.hsrrelicmanager.ui.rules

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
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import com.example.hsrrelicmanager.model.rules.group.FilterMap
import com.example.hsrrelicmanager.ui.MainActivity

class AddActionGroupBodyFragment(
    private val group: ActionGroup,
    private val _groupChangeHandler: GroupChangeHandler = GroupChangeHandler(group)
): Fragment(), GroupChangeListener by _groupChangeHandler {
    init {
        _groupChangeHandler.fragment = this
        _groupChangeHandler.onFilterChanged = {
            adapterFilter.notifyDataSetChanged()
        }
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