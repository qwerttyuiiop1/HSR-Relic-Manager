package com.example.hsrrelicmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.ui.CategorizedGroupAdapter.GroupViewHolder

class CategorizedRuleBodyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rule_body_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val groupData = (activity as MainActivity).groupData
        val categorizedGroupAdapter = CategorizedGroupAdapter(groupData)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = categorizedGroupAdapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder !is GroupViewHolder)
                    return makeMovementFlags(0, 0)

                return makeMovementFlags(
                    0,
                    ItemTouchHelper.LEFT
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false;
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemIndex = viewHolder.adapterPosition
                val groupIndex = (categorizedGroupAdapter.items.get(itemIndex) as CategorizedGroupAdapter.GroupItem).group.position;

                categorizedGroupAdapter.items.removeAt(itemIndex)
                categorizedGroupAdapter.groupData.removeAt(groupIndex)

                for (i in groupIndex..<categorizedGroupAdapter.groupData.size) {
                    groupData.get(i).position = i
                }

                categorizedGroupAdapter.notifyDataSetChanged()
            }

        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}