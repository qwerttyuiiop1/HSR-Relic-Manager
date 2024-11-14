package com.example.hsrrelicmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.FragmentActionGroupBodyBinding
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import com.example.hsrrelicmanager.model.rules.group.FilterGroup
import com.example.hsrrelicmanager.model.rules.group.Group

// TODO: delete
val groupData = mutableListOf<Group>().apply {
    for (i in 1..3) {
        val filterGroup =
            FilterGroup().apply {
                actionGroupList.add(
                    ActionGroup(
                        StatusAction(
                            if (i % 2 == 0) Relic.Status.LOCK
                            else Relic.Status.TRASH
                        )
                    )
                )
                filters[Filter.Type.RARITY] = Filter.RarityFilter(
                    atMost = 2 + i
                )
            }
        val lockActionGroup =
            ActionGroup(
                StatusAction(
                    Relic.Status.LOCK
                )
            ).apply {
                filters[Filter.Type.RARITY] = Filter.RarityFilter(
                    atLeast = 3
                )
            }
        val trashActionGroup =
            ActionGroup(
                StatusAction(
                    Relic.Status.TRASH
                )
            ).apply {
                filters[Filter.Type.SLOT] = Filter.SlotFilter(
                    mutableSetOf("Boots")
                )
            }
        val resetActionGroup =
            ActionGroup(
                StatusAction(
                    Relic.Status.DEFAULT
                )
            ).apply {
                filters[Filter.Type.LEVEL] = Filter.LevelFilter(
                    atLeast = 10
                )
            }
        val enhanceActionGroup =
            ActionGroup(
                EnhanceAction(
                    15
                )
            ).apply {
                filters[Filter.Type.MAIN_STAT] = Filter.MainStatFilter(
                    mutableSetOf("SPD")
                )
            }

        add(filterGroup)
        add(lockActionGroup)
        add(trashActionGroup)
        add(resetActionGroup)
        add(enhanceActionGroup)
    }
}

class AddActionGroupBodyFragment : Fragment() {
    lateinit var binding: FragmentActionGroupBodyBinding

    private val filterItems = mutableListOf<Group>()
    private lateinit var adapterFilter: GroupAdapter

    private lateinit var adapterAction: ActionItemAdapter
    private val actionItems = mutableListOf("")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentActionGroupBodyBinding.inflate(inflater, container, false).apply {
            binding = this
            filterSectonAdd.setOnClickListener{
                requireActivity().findViewById<View>(R.id.activity_main_layout).blur()
                val dialog = AddFilterDialog()
                dialog.show(parentFragmentManager, "AddFilterDialog")
                requireActivity().supportFragmentManager.setFragmentResultListener("level", viewLifecycleOwner) { _, bundle ->
                    val minLevel = bundle.getInt("minLevel")
                    val maxLevel = bundle.getInt("maxLevel")
                    val isAtMost = bundle.getBoolean("isAtMost")
                    filterItems.add(groupData.random())
                    adapterFilter.notifyItemInserted(actionItems.size - 1)
                    Toast.makeText(context, "minLevel: $minLevel, maxLevel: $maxLevel, isAtMost: $isAtMost", Toast.LENGTH_SHORT).show()
                }
                requireActivity().supportFragmentManager.setFragmentResultListener("selectedSets", viewLifecycleOwner) { _, bundle ->
                    val relicSet = bundle.getParcelableArrayList<RelicSet>("selectedSets")
                    Toast.makeText(context, "relicSet: $relicSet", Toast.LENGTH_SHORT).show()
                }
            }
            adapterAction = ActionItemAdapter(actionItems)
            recyclerViewActionGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewActionGroup.adapter = adapterAction
        }.root
    }

}

