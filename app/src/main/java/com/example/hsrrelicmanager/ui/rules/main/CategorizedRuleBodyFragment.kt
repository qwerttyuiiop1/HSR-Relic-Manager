package com.example.hsrrelicmanager.ui.rules.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.databinding.GroupCardDescriptionBinding
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.model.rules.ActionGroup
import com.example.hsrrelicmanager.ui.MainActivity
import com.example.hsrrelicmanager.ui.blur
import com.example.hsrrelicmanager.ui.rules.modals.DeleteRuleDialogFragment
import com.example.hsrrelicmanager.ui.rules.GroupChangeHandler
import com.example.hsrrelicmanager.ui.rules.GroupChangeListener

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
            if (group.groupList.isNotEmpty()) {
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
class CategorizedRuleBodyFragment(
    private val _groupChangeHandler: GroupChangeHandler = GroupChangeHandler(ActionGroup())
) : Fragment(), GroupChangeListener by _groupChangeHandler {
    init {
        _groupChangeHandler.fragment = this
    }
    override fun onChildCreate(group: ActionGroup) {
        _groupChangeHandler.onChildCreate(group)
        groupAdapter.notifyDataSetChanged()
    }
    override fun onChildChange(i: Int, group: ActionGroup) {
        val groupIndex = if (isSwiped) {
            (groupAdapter.listItems[i] as CategorizedGroupAdapter.GroupItem).group.position
        } else {
            i
        }
        isSwiped = false
        _groupChangeHandler.onChildChange(groupIndex, group)
        groupAdapter.notifyDataSetChanged()
    }
    override fun onChildDelete(itemIndex: Int, group: ActionGroup) {
        val categorizedGroupAdapter = groupAdapter
        val groupIndex = (categorizedGroupAdapter.listItems[itemIndex] as CategorizedGroupAdapter.GroupItem).group.position;
        _groupChangeHandler.onChildDelete(groupIndex, group)
        isSwiped = false
        categorizedGroupAdapter.listItems.removeAt(itemIndex)
//        categorizedGroupAdapter.groupData.removeAt(groupIndex)
        categorizedGroupAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rule_body_fragment, container, false)
    }

    lateinit var groupAdapter: CategorizedGroupAdapter
    var isSwiped: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbManager = (requireContext() as MainActivity).dbManager
        dbManager.open()
        val groupData = dbManager.listGroups()
        _groupChangeHandler.group.groupList = groupData
        dbManager.close()
        groupAdapter = CategorizedGroupAdapter(groupData)
        val categorizedGroupAdapter = groupAdapter

        //Log.d(this::class.toString(), groupData.joinToString(",\n"))

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = categorizedGroupAdapter

//        var swipedItemIndex: Int = -1
//
//        parentFragmentManager.setFragmentResultListener("delete_rule_request", viewLifecycleOwner) { _, bundle ->
//            val index = bundle.getInt("index")
//            val action = bundle.getString("action")
//
//            // Delete confirmed
//            if (action == "confirm" && index >= 0) {
//                val itemIndex = index
//                val groupIndex = (categorizedGroupAdapter.listItems.get(itemIndex) as CategorizedGroupAdapter.GroupItem).group.position;
//
//                categorizedGroupAdapter.listItems.removeAt(itemIndex)
//                categorizedGroupAdapter.groupData.removeAt(groupIndex)
//
//                for (i in groupIndex..<categorizedGroupAdapter.groupData.size) {
//                    groupData.get(i).position = i
//                }
//
//                categorizedGroupAdapter.notifyDataSetChanged()
//
//                Toast.makeText(requireContext(), "Rule trashed.", Toast.LENGTH_SHORT).show()
//
//                // Delete cancelled
//            } else if (action == "cancel" && swipedItemIndex != -1) {
//                categorizedGroupAdapter.notifyItemChanged(swipedItemIndex)
//            }
//            swipedItemIndex = -1
//        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder !is CategorizedGroupAdapter.GroupViewHolder)
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
                val groupIndex =
                    (categorizedGroupAdapter.listItems.get(itemIndex) as CategorizedGroupAdapter.GroupItem).group.position

                val group = categorizedGroupAdapter.groupData[groupIndex]

//                swipedItemIndex = itemIndex
                isSwiped = true

                blurBackground()
                showDeleteRuleDialog(itemIndex, group)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val card: LinearLayout = itemView.findViewById(R.id.group_card)
                val backgroundDrawable = card.background as GradientDrawable

                val trash: ImageView = itemView.findViewById(R.id.trash_icon)
                val upArrow: ImageView = itemView.findViewById(R.id.ivUpArrow)
                val downArrow: ImageView = itemView.findViewById(R.id.ivDownArrow)
                val position: TextView = itemView.findViewById(R.id.tvPosition)

                // Swiping horizontally
                if (Math.abs(dX) > Math.abs(dY)) {
                    val swipeRatio = Math.abs(dX) / itemView.width * 1.6
                    val alphaSwipeRatio = (swipeRatio * 2).coerceIn(0.0, 1.0).toFloat()

                    val startColor = arrayOf(255, 255, 255)
                    val endColor = arrayOf(219, 88, 86)

                    val red = (startColor[0] + (endColor[0] - startColor[0]) * swipeRatio).toInt()
                    val green = (startColor[1] + (endColor[1] - startColor[1]) * swipeRatio).toInt()
                    val blue = (startColor[2] + (endColor[2] - startColor[2]) * swipeRatio).toInt()

                    val strokeColor = Color.rgb(red, green, blue)
                    backgroundDrawable.setStroke(3, strokeColor)

                    trash.alpha = alphaSwipeRatio
                    trash.visibility = View.VISIBLE

                    // Swipe vertically
                } else if (Math.abs(dX) < Math.abs(dY)) {
                    card.setBackgroundResource(R.drawable.bg_inventory_relic_item_selected)

                    upArrow.setColorFilter(Color.parseColor("#FFC65C"));
                    downArrow.setColorFilter(Color.parseColor("#FFC65C"));
                    upArrow.alpha = 1f
                    downArrow.alpha = 1f

                    position.setTextColor(Color.parseColor("#FFC65C"));
                    position.alpha = 1f

                    // Stationary
                } else {
                    card.setBackgroundResource(R.drawable.bg_dark)
                    trash.visibility = View.GONE

                    upArrow.setColorFilter(Color.parseColor("#FFFFFF"));
                    downArrow.setColorFilter(Color.parseColor("#FFFFFF"));
                    upArrow.alpha = 0.5f
                    downArrow.alpha = 0.5f

                    position.setTextColor(Color.parseColor("#FFFFFF"));
                    position.alpha = 0.5f
                }

                itemView.translationX = dX

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun blurBackground() {
        requireActivity().findViewById<View>(R.id.activity_main_layout).blur()
    }

    private fun showDeleteRuleDialog(index: Int, group: ActionGroup) {
        val dialog = DeleteRuleDialogFragment(index, group, this)
        dialog.show(parentFragmentManager, "DeleteRuleDialog")
    }
}