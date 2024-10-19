package com.example.hsrrelicmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.GroupAdapter.ViewHolder
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import com.example.hsrrelicmanager.model.rules.group.FilterGroup
import com.example.hsrrelicmanager.model.rules.group.Group
import java.util.Collections

class RuleBodyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rule_body_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy group data
        val groupData = mutableListOf<Group>()
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

            groupData.add(filterGroup)
            groupData.add(lockActionGroup)
            groupData.add(trashActionGroup)
            groupData.add(resetActionGroup)
            groupData.add(enhanceActionGroup)
        }
        val groupAdapter = GroupAdapter(groupData)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = groupAdapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Collections.swap(groupAdapter.groupData, viewHolder.adapterPosition, target.adapterPosition);
                groupAdapter.groupData.get(viewHolder.adapterPosition).position = target.adapterPosition;
                groupAdapter.groupData.get(target.adapterPosition).position = viewHolder.adapterPosition;
                (viewHolder as GroupAdapter.ViewHolder).updatePosition(target.adapterPosition);
                (target as GroupAdapter.ViewHolder).updatePosition(viewHolder.adapterPosition);
                groupAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition);
                return true;
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val index = viewHolder.adapterPosition;
                groupAdapter.groupData.removeAt(index);
                groupAdapter.notifyItemRemoved(index);
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
