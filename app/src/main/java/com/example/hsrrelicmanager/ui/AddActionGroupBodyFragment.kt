package com.example.hsrrelicmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.components.FilterItem
import com.example.hsrrelicmanager.databinding.FragmentActionGroupBodyBinding
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.rules.group.FilterGroup

class AddActionGroupBodyFragment : Fragment() {

    private var _binding: FragmentActionGroupBodyBinding? = null
    private val binding get() = _binding!!

    private val filterItems: MutableList<FilterItem> = mutableListOf()
    private lateinit var adapterFilter: FilterAdapter

    private val actionItems = mutableListOf("")
    private lateinit var adapterAction: ActionItemAdapter

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
            adapterFilter = FilterAdapter(filterItems)
            recyclerViewFilterGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewFilterGroup.adapter = adapterFilter

            // Add Filter Button Click Listener
            filterSectonAdd.setOnClickListener {
                val dialog = AddFilterDialog()
                dialog.show(parentFragmentManager, "AddFilterDialog")

                // Listen for Selected Sets from Dialog
                parentFragmentManager.setFragmentResultListener("selectedSets", viewLifecycleOwner) { _, bundle ->
                    val selectedSets = bundle.getParcelableArrayList<RelicSet>("selectedSets")
                    if (selectedSets != null) {
                        val mutableSelectedSets: MutableList<RelicSet> = selectedSets.filterNotNull().toMutableList()

                        filterItems.add(FilterItem("Main Stat", mutableSelectedSets, 0))
                        adapterFilter.notifyDataSetChanged()

                        Toast.makeText(context, "Added ${mutableSelectedSets.size} relic sets", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "No relic sets selected", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
