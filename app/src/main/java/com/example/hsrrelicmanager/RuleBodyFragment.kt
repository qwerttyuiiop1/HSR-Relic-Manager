package com.example.hsrrelicmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.group.FilterGroup
import com.example.hsrrelicmanager.model.rules.group.Group
import com.example.hsrrelicmanager.model.rules.action.StatusAction

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
                FilterGroup()
            val lockActionGroup =
                ActionGroup(
                    StatusAction(
                        Relic.Status.LOCK
                    )
                )
            val trashActionGroup =
                ActionGroup(
                    StatusAction(
                        Relic.Status.TRASH
                    )
                )
            val resetActionGroup =
                ActionGroup(
                    StatusAction(
                        Relic.Status.DEFAULT
                    )
                )
            val enhanceActionGroup =
                ActionGroup(
                    EnhanceAction(
                        9
                    )
                )

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
    }
}
