package com.example.hsrrelicmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.FragmentFilterGroupBodyBinding
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import com.example.hsrrelicmanager.model.rules.group.FilterGroup
import com.example.hsrrelicmanager.model.rules.group.Group

class AddFilterGroupBodyFragment : Fragment() {
    lateinit var binding: FragmentFilterGroupBodyBinding

    //    private lateinit var adapter: ActionItemAdapter
    private val actionItems = mutableListOf<Group>()
    private lateinit var adapter: GroupAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentFilterGroupBodyBinding.inflate(inflater, container, false).apply {
            binding = this
            filterGroupSectonAdd.setOnClickListener {
                requireActivity().findViewById<View>(R.id.activity_main_layout).blur()
                val dialog = AddFilterDialog()
                dialog.show(parentFragmentManager, "AddFilterDialog")
                requireActivity().supportFragmentManager.setFragmentResultListener("level", viewLifecycleOwner) { _, bundle ->
                    val minLevel = bundle.getInt("minLevel")
                    val maxLevel = bundle.getInt("maxLevel")
                    val isAtMost = bundle.getBoolean("isAtMost")
                    adapter.notifyItemInserted(actionItems.size - 1)
                }
                requireActivity().supportFragmentManager.setFragmentResultListener("selectedSets", viewLifecycleOwner) { _, bundle ->
                    val relicSet = bundle.getParcelableArrayList<RelicSet>("selectedSets")
                    adapter.notifyItemInserted(actionItems.size - 1)
                }
            }
            adapter = GroupAdapter(actionItems)
            binding.recyclerViewActionGroup.adapter = adapter
            binding.recyclerViewActionGroup.layoutManager = LinearLayoutManager(context)
            adapter.notifyDataSetChanged()
        }.root
    }
}