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
import com.example.hsrrelicmanager.core.components.FilterItem
import com.example.hsrrelicmanager.databinding.FragmentActionGroupBodyBinding
import com.example.hsrrelicmanager.model.Substat
import com.example.hsrrelicmanager.model.Status
import com.example.hsrrelicmanager.model.relics.RelicSet

class AddActionGroupBodyFragment : Fragment() {

    private var _binding: FragmentActionGroupBodyBinding? = null
    private val binding get() = _binding!!

    public val filterItems: MutableList<FilterItem> = mutableListOf()
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
            adapterFilter = FilterAdapter(filterItems)
            recyclerViewFilterGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewFilterGroup.adapter = adapterFilter

            // Add Filter Button Click Listener
            filterSectonAdd.setOnClickListener {
                val dialog = AddFilterDialog(filterItems)
                dialog.show(parentFragmentManager, "AddFilterDialog")

                val activity = context as MainActivity
                val bgView = activity.findViewById<View>(R.id.activity_main_layout)
                bgView.setRenderEffect(
                    RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
                )

                // Listen for Selected Sets from Dialog
                parentFragmentManager.setFragmentResultListener("selectedSets", viewLifecycleOwner) { _, bundle ->
                    val selectedSets = bundle.getParcelableArrayList<RelicSet>("selectedSets")

                    if (selectedSets == null || selectedSets.isEmpty()) {
                        adapterFilter.notifyDataSetChanged()
                        return@setFragmentResultListener
                    }

                    if (RelicTracker == 1){
                        index = filterItems.indexOfFirst { it.title == "Relic Set" }

                        if (index != -1) {
                            filterItems[index] = FilterItem("Relic Set", selectedSets!!, mutableListOf<Substat>(), -1,0, false, mutableListOf(), mutableListOf<Status>())
                        }
                        else{
                            filterItems.add(FilterItem("Relic Set", selectedSets!!, mutableListOf<Substat>(), -1,0, false, mutableListOf(), mutableListOf<Status>()))
                        }
                        index = -1
                    }
                    else if (selectedSets != null && selectedSets.isNotEmpty() && RelicTracker == 0) {
                        val mutableSelectedSets: MutableList<RelicSet> = selectedSets.filterNotNull().toMutableList()

                        filterItems.add(FilterItem("Relic Set", mutableSelectedSets, mutableListOf<Substat>(), -1, 0, false, mutableListOf(), mutableListOf<Status>()))
                        RelicTracker = 1
                    }
                    adapterFilter.notifyDataSetChanged()
                }

//                parentFragmentManager.setFragmentResultListener("slot", viewLifecycleOwner) { _, bundle ->
//                    val substat = bundle.getParcelableArrayList<Substat>("substat")
//                    val weightLevel = bundle.getInt("weightLevel")
//
//                    if (substat == null || substat.isEmpty()) {
//                        adapterFilter.notifyDataSetChanged()
//                        return@setFragmentResultListener
//                    }
//
//                    if (SubstatTracker == 1){
//                        index = filterItems.indexOfFirst { it.title == "Substat" }
//
//                        if (index != -1) {
//                            filterItems[index] = FilterItem("Substat", mutableListOf<RelicSet>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>())
//                        }
//                        else{
//                            filterItems.add(FilterItem("Substat", mutableListOf<RelicSet>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>()))
//                        }
//                        index = -1
//                    }
//                    else if (substat != null && substat.isNotEmpty() && SubstatTracker == 0) {
//
//                        filterItems.add(FilterItem("Substat", mutableListOf<RelicSet>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>()))
//                        SubstatTracker = 1
//                    }
//                    adapterFilter.notifyDataSetChanged()
//                }
//
//                parentFragmentManager.setFragmentResultListener("mainstat", viewLifecycleOwner) { _, bundle ->
//                    val substat = bundle.getParcelableArrayList<Substat>("substat")
//                    val weightLevel = bundle.getInt("weightLevel")
//
//                    if (substat == null || substat.isEmpty()) {
//                        adapterFilter.notifyDataSetChanged()
//                        return@setFragmentResultListener
//                    }
//
//                    if (SubstatTracker == 1){
//                        index = filterItems.indexOfFirst { it.title == "Substat" }
//
//                        if (index != -1) {
//                            filterItems[index] = FilterItem("Substat", mutableListOf<RelicSet>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>())
//                        }
//                        else{
//                            filterItems.add(FilterItem("Substat", mutableListOf<RelicSet>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>()))
//                        }
//                        index = -1
//                    }
//                    else if (substat != null && substat.isNotEmpty() && SubstatTracker == 0) {
//
//                        filterItems.add(FilterItem("Substat", mutableListOf<RelicSet>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>()))
//                        SubstatTracker = 1
//                    }
//                    adapterFilter.notifyDataSetChanged()
//                }
//

                parentFragmentManager.setFragmentResultListener("substat", viewLifecycleOwner) { _, bundle ->
                    val substat = bundle.getParcelableArrayList<Substat>("substat")
                    val weightLevel = bundle.getInt("weightLevel")

                    if (substat == null || substat.isEmpty()) {
                        adapterFilter.notifyDataSetChanged()
                        return@setFragmentResultListener
                    }

                    if (SubstatTracker == 1){
                        index = filterItems.indexOfFirst { it.title == "Substat" }

                        if (index != -1) {
                            filterItems[index] = FilterItem("Substat", mutableListOf<RelicSet>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>())
                        }
                        else{
                            filterItems.add(FilterItem("Substat", mutableListOf<RelicSet>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>()))
                        }
                        index = -1
                    }
                    else if (substat != null && substat.isNotEmpty() && SubstatTracker == 0) {

                        filterItems.add(FilterItem("Substat", mutableListOf<RelicSet>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>()))
                        SubstatTracker = 1
                    }
                    adapterFilter.notifyDataSetChanged()
                }

                parentFragmentManager.setFragmentResultListener("rarity", viewLifecycleOwner) { _, bundle ->
                    val rarity1 = bundle.getInt("1 Star")
                    val rarity2 = bundle.getInt("2 Star")
                    val rarity3 = bundle.getInt("3 Star")
                    val rarity4 = bundle.getInt("4 Star")
                    val rarity5 = bundle.getInt("5 Star")

                    val rarityList = mutableListOf<Int>()
                    if (rarity1 != 0) rarityList.add(rarity1)
                    if (rarity2 != 0) rarityList.add(rarity2)
                    if (rarity3 != 0) rarityList.add(rarity3)
                    if (rarity4 != 0) rarityList.add(rarity4)
                    if (rarity5 != 0) rarityList.add(rarity5)


                    if (rarity1 == 0 && rarity2 == 0 && rarity3 == 0 && rarity4 == 0 && rarity5 == 0){
                        adapterFilter.notifyDataSetChanged()
                        return@setFragmentResultListener
                    }

                    if (RarityTracker ==1){
                        index = filterItems.indexOfFirst { it.title == "Rarity" }
                        if (index!=-1){
                            filterItems[index] = FilterItem("Rarity", mutableListOf<RelicSet>(), mutableListOf<Substat>(), -1,0, false, rarityList, mutableListOf<Status>())
                        }
                        else{
                            filterItems.add(FilterItem("Rarity", mutableListOf<RelicSet>(), mutableListOf<Substat>(), -1,0, false, rarityList, mutableListOf<Status>()))
                        }
                        index = -1
                    }
                    else if (rarity1 != 0 || rarity2 != 0 || rarity3 != 0 || rarity4 != 0 || rarity5 != 0 && RarityTracker == 0) {
                        filterItems.add(FilterItem("Rarity",mutableListOf<RelicSet>(), mutableListOf<Substat>(), -1,0, false, rarityList, mutableListOf<Status>()))
                        RarityTracker = 1
                    }
                    adapterFilter.notifyDataSetChanged()
                }

                parentFragmentManager.setFragmentResultListener("level", viewLifecycleOwner) { _, bundle ->
                    val level = bundle.getInt("level")
                    val isAtLeast = bundle.getBoolean("isAtLeast")

                    val levelList = mutableListOf<Any>()
                    levelList.add(level)
                    levelList.add(isAtLeast)

                    if (LevelTracker == 1){
                        index = filterItems.indexOfFirst { it.title == "Level" }
                        if (index != -1){
                            filterItems[index] = FilterItem("Level", mutableListOf<RelicSet>(), mutableListOf<Substat>(), -1, level, isAtLeast, mutableListOf(), mutableListOf<Status>())
                        }
                        else{
                            filterItems.add(FilterItem("Level", mutableListOf<RelicSet>(), mutableListOf<Substat>(), -1, level, isAtLeast, mutableListOf(), mutableListOf<Status>()))
                        }
                        index = -1
                    }
                    else if (level != null && LevelTracker == 0) {
                        filterItems.add(FilterItem("Level", mutableListOf<RelicSet>(), mutableListOf<Substat>(), -1, level, isAtLeast, mutableListOf(), mutableListOf<Status>()))
                        LevelTracker = 1
                    }
                    adapterFilter.notifyDataSetChanged()
                }

                parentFragmentManager.setFragmentResultListener("status", viewLifecycleOwner) { _, bundle ->
                    val lock = bundle.getString("Lock")
                    val lockImg = bundle.getInt("lockImage")
                    val trash = bundle.getString("Trash")
                    val trashImg = bundle.getInt("trashImage")
                    val none = bundle.getString("None")
                    val noneImg = bundle.getInt("noneImage")

                    val statusList = mutableListOf<Status>()

                    lock?.let { statusList.add(Status(it, lockImg)) }
                    trash?.let { statusList.add(Status(it, trashImg)) }
                    none?.let { statusList.add(Status(it, noneImg)) }

                    if (lock == null && trash == null && none == null){
                        adapterFilter.notifyDataSetChanged()
                        return@setFragmentResultListener
                    }

                    if (StatusTracker == 1){
                        index = filterItems.indexOfFirst { it.title == "Status" }
                        if (index != -1){
                            filterItems[index] = FilterItem("Status", mutableListOf<RelicSet>(), mutableListOf<Substat>(), -1,0, false, mutableListOf(), statusList)
                        }
                        else{
                            filterItems.add(FilterItem("Status", mutableListOf<RelicSet>(), mutableListOf<Substat>(), -1,0, false, mutableListOf(), statusList))
                        }
                        index = -1
                    }
                    else if (lock != null || trash != null || none != null && StatusTracker == 0) {
                        filterItems.add(FilterItem("Status", mutableListOf<RelicSet>(), mutableListOf<Substat>(), -1,0, false, mutableListOf(), statusList))
                        StatusTracker = 1
                    }
                    adapterFilter.notifyDataSetChanged()
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
