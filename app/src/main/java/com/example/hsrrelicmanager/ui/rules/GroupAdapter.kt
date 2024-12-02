package com.example.hsrrelicmanager.ui.rules

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.GroupCardDescriptionBinding
import com.example.hsrrelicmanager.model.rules.group.ActionGroup

class GroupAdapter(val groupData: MutableList<ActionGroup>, private val ruleBodyFragment: Fragment) :
    RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.action_group_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groupData[position]
        holder.updatePosition(position)
        holder.bind(group)

        holder.card.setBackgroundResource(R.drawable.bg_dark)
        holder.ivTrash.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return groupData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupIcon: ImageView = itemView.findViewById(R.id.groupIcon)
        private val tvGroupName: TextView = itemView.findViewById(R.id.tvGroupName)
        private val tvPosition: TextView = itemView.findViewById(R.id.tvPosition)
        private val filterContainer: ViewGroup = itemView.findViewById(R.id.filter_container)
        internal val card: LinearLayout = itemView.findViewById(R.id.group_card)
        val ivTrash: ImageView = itemView.findViewById(R.id.trash_icon)
        private var pos = 0
        lateinit var group: ActionGroup

        init {
            itemView.rootView.setOnClickListener { v: View? ->
                ruleBodyFragment.parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.header_fragment_container, GroupHeaderFragment())
                    .replace(R.id.body_fragment_container, GroupBodyFragment(
                        GroupChangeHandler(group),
                        ruleBodyFragment as GroupChangeListener
                    ))
                    .addToBackStack(null)
                    .commit()
            }
        }

        fun bind(group: ActionGroup) {
            this.group = group
            tvGroupName.text = group.getViewName()
            filterContainer.removeAllViews()

            // Load group icon based on default action (if it has one)
            groupIcon.setImageResource(group.getImageResource())

            // check class of group is filter / action group
            for (filter in group.filters.values) {
                val binding = GroupCardDescriptionBinding.inflate(
                    LayoutInflater.from(filterContainer.context),
                    filterContainer,
                    false
                )
                binding.rowName.text = filter.name + ':'
                binding.rowValue.text = filter.description
                filterContainer.addView(binding.root)
            }
        }

        fun updatePosition(position: Int) {
            this.pos = position
            tvPosition.text = (position + 1).toString()
        }
    }
}
