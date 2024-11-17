package com.example.hsrrelicmanager.ui

import android.graphics.RenderEffect
import android.graphics.Shader
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
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.rules.group.FilterGroup

class AddActionGroupBodyFragment : Fragment() {

    private var _binding: FragmentActionGroupBodyBinding? = null
    private val binding get() = _binding!!

    public val filterItems: MutableList<FilterItem> = mutableListOf()
    private lateinit var adapterFilter: FilterAdapter

    private val actionItems = mutableListOf("")
    private lateinit var adapterAction: ActionItemAdapter

    private var RelicTracker = 0
    private var RarityTracker = 0

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

                val activity = context as MainActivity
                val bgView = activity.findViewById<View>(R.id.activity_main_layout)
                bgView.setRenderEffect(
                    RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
                )

                // Listen for Selected Sets from Dialog
                parentFragmentManager.setFragmentResultListener("selectedSets", viewLifecycleOwner) { _, bundle ->
                    val selectedSets = bundle.getParcelableArrayList<RelicSet>("selectedSets")

                    if (RelicTracker == 1){
                        filterItems.removeAll { it.title == "Relic Set" }
                        RelicTracker = 0
                    }
                    if (selectedSets != null && selectedSets.isNotEmpty() && RelicTracker == 0) {
                        val mutableSelectedSets: MutableList<RelicSet> = selectedSets.filterNotNull().toMutableList()

                        filterItems.add(FilterItem("Relic Set", mutableSelectedSets, 0, mutableListOf()))
                        adapterFilter.notifyDataSetChanged()
                        RelicTracker = 1
                    }
                }

                parentFragmentManager.setFragmentResultListener("rarity", viewLifecycleOwner) { _, bundle ->
                    val rarity3 = bundle.getString("Star3")
                    val rarity4 = bundle.getString("Star4")
                    val rarity5 = bundle.getString("Star5")

                    if (RarityTracker ==1){
                        filterItems.removeAll { it.title == "Rarity" }
                        RarityTracker = 0
                    }
                    if (rarity3 != null || rarity4 != null || rarity5 != null && RarityTracker == 0) {
                        val rarityList = mutableListOf<String>()
                        rarity3?.let { rarityList.add(it) }
                        rarity4?.let { rarityList.add(it) }
                        rarity5?.let { rarityList.add(it) }

                        filterItems.add(FilterItem("Rarity",mutableListOf<RelicSet>(), 0, rarityList))
                        adapterFilter.notifyDataSetChanged()
                        RarityTracker = 1
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
