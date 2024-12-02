package com.example.hsrrelicmanager.ui.rules

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
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
import com.example.hsrrelicmanager.databinding.FragmentFilterGroupBodyBinding
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import com.example.hsrrelicmanager.ui.MainActivity
import com.example.hsrrelicmanager.ui.blur
import com.example.hsrrelicmanager.ui.db.DBManager
import java.util.Collections
import kotlin.properties.Delegates

class GroupBodyFragment(
    private val _groupChangeHandler: GroupChangeHandler,
    private val parentCallback: GroupChangeListener? = null
) : Fragment(), GroupChangeListener by _groupChangeHandler {
    constructor(
        parent: ActionGroup?,
        parentCallback: GroupChangeListener? = null
    ) : this(
        GroupChangeHandler(ActionGroup(parentGroup = parent)),
        parentCallback,
    ) {
        if (parentCallback == null) {
            parentFragmentManager.setFragmentResult("new_group", Bundle().apply {
                putParcelable("group", group)
            })
        } else {
            parentCallback.onChildCreate(group)
        }
    }
    constructor(): this(GroupChangeHandler(ActionGroup()), null) {
        parentFragmentManager.setFragmentResult("new_group", Bundle().apply {
            putParcelable("group", group)
        })
    }
    private val group by _groupChangeHandler

    init {
        _groupChangeHandler.fragment = this
    }
    override fun onUpdateAction(action: Action?) {
        _groupChangeHandler.onUpdateAction(action)
        actionItems[0] = action
        adapterAction.notifyDataSetChanged()
    }
    override fun onUpdateFilter(filter: Filter) {
        _groupChangeHandler.onUpdateFilter(filter)
        adapterFilter.notifyDataSetChanged()
    }
    override fun onChildCreate(group: ActionGroup) {
        _groupChangeHandler.onChildCreate(group)
        adapterGroup.notifyItemInserted(adapterGroup.groupData.size - 1)
    }
    override fun onChildChange(i: Int, group: ActionGroup) {
        _groupChangeHandler.onChildChange(i, group)
        adapterGroup.notifyItemChanged(i)
    }
    override fun onChildDelete(i: Int, group: ActionGroup) {
        _groupChangeHandler.onChildDelete(i, group)
        adapterGroup.notifyItemRemoved(i)
        adapterGroup.notifyItemRangeChanged(i, adapterGroup.groupData.size - i)
    }

    private lateinit var dbManager: DBManager

    private var thisId by Delegates.notNull<Long>()
    private lateinit var binding: FragmentFilterGroupBodyBinding

    private lateinit var adapterFilter: FilterAdapter

    private val actionItems = mutableListOf<Action?>(null)
    private lateinit var adapterAction: ActionItemAdapter

    private lateinit var adapterGroup: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbManager = (requireContext() as MainActivity).dbManager
        binding = FragmentFilterGroupBodyBinding.inflate(inflater, container, false)

        binding.apply {
//            creatingChild = false

//            // Create the group
//            dbManager.open()
//            thisId = dbManager.insertGroup(mutableMapOf(), null, null, null)
//            thisGroup = ActionGroup(thisId)
//            dbManager.close()

            // Initialize Filter Adapter
            adapterFilter = FilterAdapter(group.filters, this@GroupBodyFragment)
            recyclerViewFilterGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewFilterGroup.adapter = adapterFilter

            // Initialize Default Action Adapter
            actionItems[0] = group.action
            adapterAction = ActionItemAdapter(actionItems, this@GroupBodyFragment)
            recyclerViewActionItem.layoutManager = LinearLayoutManager(context)
            recyclerViewActionItem.adapter = adapterAction

            // Initialize Group Adapter
            adapterGroup = GroupAdapter(group.groupList, this@GroupBodyFragment)
            recyclerViewActionGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewActionGroup.adapter = adapterGroup


            /* GESTURES */

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
                    Collections.swap(adapterGroup.groupData, viewHolder.adapterPosition, target.adapterPosition);

                    val lo = minOf(viewHolder.adapterPosition, target.adapterPosition)
                    val hi = maxOf(viewHolder.adapterPosition, target.adapterPosition)

                    for (index in lo..hi) {
                        group.groupList[index].position = index
                    }

                    (viewHolder as GroupAdapter.ViewHolder).updatePosition(target.adapterPosition);
                    (target as GroupAdapter.ViewHolder).updatePosition(viewHolder.adapterPosition);
                    adapterGroup.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition);

                    return true;
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val index = viewHolder.adapterPosition
                    val group = adapterGroup.groupData[index]

                    blurBackground()
                    showDeleteRuleDialog(index, group)
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

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            })
            itemTouchHelper.attachToRecyclerView(recyclerViewActionGroup)


            /* ADD BUTTONS */

            // Add Filter Button Click Listener
            filterGroupSectonAdd.setOnClickListener {
                val dialog = AddFilterDialog(group.filters, this@GroupBodyFragment)
                dialog.show(parentFragmentManager, "AddFilterDialog")

                val activity = context as MainActivity
                val bgView = activity.findViewById<View>(R.id.activity_main_layout)
                bgView.setRenderEffect(
                    RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
                )
            }

            actionGroupOrderAdd.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_delayed,
                        android.R.anim.fade_out,
                        R.anim.fade_in_delayed,
                        R.anim.slide_out)
                    .replace(R.id.body_fragment_container, GroupBodyFragment(
                        group
                    ))
                    .addToBackStack(null)
                    .commit()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        Log.d("AddGroupBodyFragment", "Being destroyed!")
//        Log.d("AddGroupBodyFragment", "Filter Items:")
//        Log.d("AddGroupBodyFragment", filterBuilders.joinToString("\n"))
//        Log.d("AddGroupBodyFragment", "Filter Items in JSON:")
//        Log.d("AddGroupBodyFragment", filterBuilders.joinToString {
//            Json.encodeToString(it.buildFilter())
//        })
//        Log.d("AddGroupBodyFragment", "Action Item:")
//        Log.d("AddGroupBodyFragment", actionItem.toString())
//        Log.d("AddGroupBodyFragment", "Action Groups:")
//        Log.d("AddGroupBodyFragment", actionGroups.joinToString("\n"))
        //Log.d("AddGroupBodyFragment", "GroupData")
        //Log.d("AddGroupBodyFragment", (requireActivity() as MainActivity).groupData.joinToString("\n"))


//        // Filters
//        val filterMap: MutableMap<Filter.Type, Filter?> = mutableMapOf()
//        for (filterItem in filterBuilders) {
//            val filter = filterItem.buildFilter()
//            filter.filterType?.let { filterMap.put(it, filter) }
//        }
//
//        // Group List
//        var groupList: MutableList<ActionGroup> = mutableListOf()
//        if (actionGroups.isNotEmpty()) {
//            groupList = actionGroups
//        }
//
//        // Default Action
//        var action: Action? = null
//        if (actionItem[0].isNotEmpty()) {
//            action = when (actionItem[0]) {
//                "Enhance" -> EnhanceAction(adapterAction.getLevelNumber())
//                "Reset" -> StatusAction(Relic.Status.DEFAULT)
//                else -> StatusAction(Relic.Status.valueOf(actionItem[0].uppercase()))
//            }
//        }
//
//        // Newly-Created Group
//        if (group.filters.isNotEmpty() ||
////            groupList.isNotEmpty() ||
//            group.action != null) {

//            dbManager.open()
//            dbManager.updateGroup(group)
//            dbManager.close()
//        }
//        else {
//            dbManager.open()
//            dbManager.deleteGroup(thisId)
//            dbManager.close()
//        }
    }

    private fun blurBackground() {
        requireActivity().findViewById<View>(R.id.activity_main_layout).blur()
    }

    private fun showDeleteRuleDialog(index: Int, group: ActionGroup) {
        val dialog = DeleteRuleDialogFragment(index, group, this)
        dialog.show(parentFragmentManager, "DeleteRuleDialog")
    }
}