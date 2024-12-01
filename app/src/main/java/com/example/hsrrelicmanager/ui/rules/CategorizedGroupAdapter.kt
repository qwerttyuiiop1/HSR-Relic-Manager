package com.example.hsrrelicmanager.ui.rules

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.GroupCardDescriptionBinding
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.model.rules.group.ActionGroup

class CategorizedGroupAdapter(val groupData: MutableList<ActionGroup>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val listItems: MutableList<ListItem>

    init {
        val enhanceGroups: MutableList<ListItem> = ArrayList()
        val lockGroups: MutableList<ListItem> = ArrayList()
        val trashGroups: MutableList<ListItem> = ArrayList()
        val resetGroups: MutableList<ListItem> = ArrayList()
        val filterGroups: MutableList<ListItem> = ArrayList()

        for (group in groupData) {
            if (!group.groupList.isEmpty()) {
                filterGroups.add(GroupItem(group))
            } else if (group.action != null) {
                val action = group.action

                if (action is EnhanceAction) {
                    enhanceGroups.add(GroupItem(group))
                } else if (action is StatusAction) {
                    when (action.targetStatus) {
                        Relic.Status.LOCK -> lockGroups.add(GroupItem(group))
                        Relic.Status.TRASH -> trashGroups.add(GroupItem(group))
                        Relic.Status.DEFAULT -> resetGroups.add(GroupItem(group))
                        else -> throw IllegalArgumentException("Illegal status: ${action.targetStatus}")
                    }
                }
            }
        }

        listItems = ArrayList()

        listItems.add(HeaderItem("Enhance"))
        listItems.addAll(enhanceGroups)
        listItems.add(DividerItem())

        listItems.add(HeaderItem("Lock"))
        listItems.addAll(lockGroups)
        listItems.add(DividerItem())

        listItems.add(HeaderItem("Trash"))
        listItems.addAll(trashGroups)
        listItems.add(DividerItem())

        listItems.add(HeaderItem("Reset"))
        listItems.addAll(resetGroups)
        listItems.add(DividerItem())

        listItems.add(HeaderItem("Filter Groups"))
        listItems.addAll(filterGroups)
    }

    fun getItems(): List<ListItem> {
        return listItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val li = LayoutInflater.from(parent.context)
        val view: View

        when (viewType) {
            ListItem.Companion.TYPE_GROUP -> {
                view = li.inflate(R.layout.action_group_layout, parent, false)
                return GroupViewHolder(view)
            }

            ListItem.Companion.TYPE_HEADER -> {
                view = li.inflate(R.layout.categorized_rules_header, parent, false)
                return HeaderViewHolder(view)
            }

            ListItem.Companion.TYPE_DIVIDER -> {
                view = li.inflate(R.layout.categorized_rules_divider, parent, false)
                return DividerViewHolder(view)
            }
        }
        // Dummy view
        view = li.inflate(R.layout.categorized_rules_divider, parent, false)
        return DividerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listItems[position]

        when (getItemViewType(position)) {
            ListItem.Companion.TYPE_GROUP -> {
                val groupItem = item as GroupItem
                val groupVh = holder as GroupViewHolder
                groupVh.updatePosition(groupItem.group.position)
                groupVh.bind(groupItem.group)
            }

            ListItem.Companion.TYPE_HEADER -> {
                val header = item as HeaderItem
                val headerVh = holder as HeaderViewHolder
                headerVh.bind(header.text)
            }

            ListItem.Companion.TYPE_DIVIDER -> {}
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return listItems[position].type
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupIcon: ImageView = itemView.findViewById(R.id.groupIcon)
        private val tvGroupName: TextView = itemView.findViewById(R.id.tvGroupName)
        private val tvPosition: TextView = itemView.findViewById(R.id.tvPosition)
        private val filterContainer: ViewGroup = itemView.findViewById(R.id.filter_container)

        fun bind(group: ActionGroup) {
            tvGroupName.text = group.getViewName()
            filterContainer.removeAllViews()
            // check class of group is filter / action group
            for (filter in group.filters.values) {
                if (filter != null) {
                    val binding = GroupCardDescriptionBinding.inflate(
                        LayoutInflater.from(filterContainer.context),
                        filterContainer,
                        false
                    )
                    binding.rowName.text = filter.name + ':'
                    binding.rowValue.text = filter.description
                    filterContainer.addView(binding.root)
                    groupIcon.setImageResource(group.getImageResource())
                }
            }
        }

        fun updatePosition(position: Int) {
            tvPosition.text = (position + 1).toString()
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvHeader: TextView = itemView.findViewById(R.id.tvHeader)

        fun bind(text: String?) {
            tvHeader.text = text
        }
    }

    inner class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // Based on link below
    // https://stackoverflow.com/questions/34848401/divide-elements-on-groups-in-recyclerview
    abstract class ListItem {
        abstract val type: Int

        companion object {
            const val TYPE_GROUP: Int = 0
            const val TYPE_HEADER: Int = 1
            const val TYPE_DIVIDER: Int = 2
        }
    }

    internal inner class GroupItem(var group: ActionGroup) : ListItem() {
        override val type: Int
            get() = Companion.TYPE_GROUP
    }

    internal inner class HeaderItem(var text: String) : ListItem() {
        override val type: Int
            get() = Companion.TYPE_HEADER
    }

    internal inner class DividerItem : ListItem() {
        override val type: Int
            get() = Companion.TYPE_DIVIDER
    }
}
