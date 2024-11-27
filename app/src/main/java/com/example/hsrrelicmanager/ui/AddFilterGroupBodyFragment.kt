package com.example.hsrrelicmanager.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.FilterItem
import com.example.hsrrelicmanager.databinding.FragmentFilterGroupBodyBinding
import com.example.hsrrelicmanager.model.Mainstat
import com.example.hsrrelicmanager.model.Slot
import com.example.hsrrelicmanager.model.Status
import com.example.hsrrelicmanager.model.Substat
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicSet
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.util.Collections

class AddFilterGroupBodyFragment : Fragment() {
    private lateinit var group: ActionGroup

    private var _binding: FragmentFilterGroupBodyBinding? = null
    private val binding get() = _binding!!

    private var RelicTracker = 0
    private var SlotTracker = 0
    private var MainstatTracker = 0
    private var SubstatTracker = 0
    private var RarityTracker = 0
    private var LevelTracker = 0
    private var StatusTracker = 0
    private var index = -1

    // Filters
    private val filterItems: MutableList<FilterItem> = mutableListOf()
    private lateinit var adapterFilter: FilterAdapter

    // Default Action
    private val actionItem = mutableListOf("")
    private lateinit var adapterAction: ActionItemAdapter

    // Groups
    private val actionGroups = mutableListOf<ActionGroup>()
    private lateinit var adapterGroup: GroupAdapter

    private var creatingChild = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterGroupBodyBinding.inflate(inflater, container, false)

        binding.apply {
            creatingChild = false

            // Listen for new groups
            parentFragmentManager.setFragmentResultListener("new_group", viewLifecycleOwner) { _, bundle ->
                if (!bundle.getBoolean("isChild")) {
                    val group = bundle.getParcelable<ActionGroup>("group")

                    if (group != null) {
                        actionGroups.add(group)
                        adapterGroup.notifyDataSetChanged()
                    }
                }
            }

            // Initialize Filter Adapter
            adapterFilter = FilterAdapter(filterItems)
            recyclerViewFilterGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewFilterGroup.adapter = adapterFilter

            // Initialize Default Action Adapter
            adapterAction = ActionItemAdapter(actionItem)
            recyclerViewActionItem.layoutManager = LinearLayoutManager(context)
            recyclerViewActionItem.adapter = adapterAction

            // Initialize Group Adapter
            adapterGroup = GroupAdapter(actionGroups)
            recyclerViewActionGroup.layoutManager = LinearLayoutManager(context)
            recyclerViewActionGroup.adapter = adapterGroup


            /* GESTURES */

            var swipedItemIndex: Int = -1

            // Listens for action from delete dialog
            parentFragmentManager.setFragmentResultListener("delete_rule_request", viewLifecycleOwner) { _, bundle ->
                val index = bundle.getInt("index")
                val action = bundle.getString("action")

                // Delete confirmed
                if (action == "confirm" && index >= 0) {
                    adapterGroup.groupData.removeAt(index)

                    for (i in index until adapterGroup.groupData.size) {
                        actionGroups[i].position = i
                    }

                    adapterGroup.notifyItemRemoved(index)
                    adapterGroup.notifyItemRangeChanged(index, adapterGroup.groupData.size - index)

                    Toast.makeText(requireContext(), "Rule trashed.", Toast.LENGTH_SHORT).show()

                    // Delete cancelled
                } else if (action == "cancel" && swipedItemIndex != -1) {
                    adapterGroup.notifyItemChanged(swipedItemIndex)
                }
                swipedItemIndex = -1
            }

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
                        actionGroups[index].position = index
                    }

                    (viewHolder as GroupAdapter.ViewHolder).updatePosition(target.adapterPosition);
                    (target as GroupAdapter.ViewHolder).updatePosition(viewHolder.adapterPosition);
                    adapterGroup.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition);

                    return true;
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val index = viewHolder.adapterPosition
                    val group = adapterGroup.groupData[index]

                    swipedItemIndex = index

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
                            filterItems[index] = FilterItem("Relic Set", selectedSets!!, mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1,0, false, mutableListOf(), mutableListOf<Status>())
                        }
                        else{
                            filterItems.add(FilterItem("Relic Set", selectedSets!!, mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1,0, false, mutableListOf(), mutableListOf<Status>()))
                        }
                        index = -1
                    }
                    else if (selectedSets != null && selectedSets.isNotEmpty() && RelicTracker == 0) {
                        val mutableSelectedSets: MutableList<RelicSet> = selectedSets.filterNotNull().toMutableList()

                        filterItems.add(FilterItem("Relic Set", mutableSelectedSets, mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1, 0, false, mutableListOf(), mutableListOf<Status>()))
                        RelicTracker = 1
                    }
                    adapterFilter.notifyDataSetChanged()
                }

                parentFragmentManager.setFragmentResultListener("slot", viewLifecycleOwner) { _, bundle ->
                    val slot = bundle.getParcelableArrayList<Slot>("slot")

                    if (slot == null || slot.isEmpty()) {
                        adapterFilter.notifyDataSetChanged()
                        return@setFragmentResultListener
                    }

                    if (SlotTracker == 1){
                        index = filterItems.indexOfFirst { it.title == "Slot" }

                        if (index != -1) {
                            filterItems[index] = FilterItem("Slot", mutableListOf<RelicSet>(), slot, mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1, 0, false, mutableListOf(), mutableListOf<Status>())
                        }
                        else{
                            filterItems.add(FilterItem("Slot", mutableListOf<RelicSet>(), slot, mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1, 0, false, mutableListOf(), mutableListOf<Status>()))
                        }
                        index = -1
                    }
                    else if (slot != null && slot.isNotEmpty() && SlotTracker == 0) {

                        filterItems.add(FilterItem("Slot", mutableListOf<RelicSet>(), slot, mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1, 0, false, mutableListOf(), mutableListOf<Status>()))
                        SlotTracker = 1
                    }
                    adapterFilter.notifyDataSetChanged()
                }


                parentFragmentManager.setFragmentResultListener("mainstat", viewLifecycleOwner) { _, bundle ->
                    val mainstat = bundle.getParcelableArrayList<Mainstat>("mainstat")

                    if (mainstat == null || mainstat.isEmpty()) {
                        adapterFilter.notifyDataSetChanged()
                        return@setFragmentResultListener
                    }

                    if (MainstatTracker == 1){
                        index = filterItems.indexOfFirst { it.title == "Mainstat" }

                        if (index != -1) {
                            filterItems[index] = FilterItem("Mainstat", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mainstat, mutableListOf<Substat>(), -1, 0, false, mutableListOf(), mutableListOf<Status>())
                        }
                        else{
                            filterItems.add(FilterItem("Mainstat", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mainstat, mutableListOf<Substat>(), -1, 0, false, mutableListOf(), mutableListOf<Status>()))
                        }
                        index = -1
                    }
                    else if (mainstat != null && mainstat.isNotEmpty() && MainstatTracker == 0) {

                        filterItems.add(FilterItem("Mainstat", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mainstat, mutableListOf<Substat>(), -1, 0, false, mutableListOf(), mutableListOf<Status>()))
                        MainstatTracker = 1
                    }
                    adapterFilter.notifyDataSetChanged()
                }


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
                            filterItems[index] = FilterItem("Substat", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>())
                        }
                        else{
                            filterItems.add(FilterItem("Substat", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>()))
                        }
                        index = -1
                    }
                    else if (substat != null && substat.isNotEmpty() && SubstatTracker == 0) {

                        filterItems.add(FilterItem("Substat", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), substat, weightLevel, 0, false, mutableListOf(), mutableListOf<Status>()))
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
                            filterItems[index] = FilterItem("Rarity", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1,0, false, rarityList, mutableListOf<Status>())
                        }
                        else{
                            filterItems.add(FilterItem("Rarity", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1,0, false, rarityList, mutableListOf<Status>()))
                        }
                        index = -1
                    }
                    else if (rarity1 != 0 || rarity2 != 0 || rarity3 != 0 || rarity4 != 0 || rarity5 != 0 && RarityTracker == 0) {
                        filterItems.add(FilterItem("Rarity",mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1,0, false, rarityList, mutableListOf<Status>()))
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
                            filterItems[index] = FilterItem("Level", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1, level, isAtLeast, mutableListOf(), mutableListOf<Status>())
                        }
                        else{
                            filterItems.add(FilterItem("Level", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1, level, isAtLeast, mutableListOf(), mutableListOf<Status>()))
                        }
                        index = -1
                    }
                    else if (level != null && LevelTracker == 0) {
                        filterItems.add(FilterItem("Level", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1, level, isAtLeast, mutableListOf(), mutableListOf<Status>()))
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
                            filterItems[index] = FilterItem("Status", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1,0, false, mutableListOf(), statusList)
                        }
                        else{
                            filterItems.add(FilterItem("Status", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1,0, false, mutableListOf(), statusList))
                        }
                        index = -1
                    }
                    else if (lock != null || trash != null || none != null && StatusTracker == 0) {
                        filterItems.add(FilterItem("Status", mutableListOf<RelicSet>(), mutableListOf<Slot>(), mutableListOf<Mainstat>(), mutableListOf<Substat>(), -1,0, false, mutableListOf(), statusList))
                        StatusTracker = 1
                    }
                    adapterFilter.notifyDataSetChanged()
                }
            }

            actionGroupOrderAdd.setOnClickListener {
                creatingChild = true

                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_delayed,
                        android.R.anim.fade_out,
                        R.anim.fade_in_delayed,
                        R.anim.slide_out)
                    .replace(R.id.body_fragment_container, AddFilterGroupBodyFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AddGroupBodyFragment", "Being destroyed!")
        Log.d("AddGroupBodyFragment", "Filter Items:")
        Log.d("AddGroupBodyFragment", filterItems.joinToString("\n"))
        Log.d("AddGroupBodyFragment", "Filter Items in JSON:")
        Log.d("AddGroupBodyFragment", filterItems.joinToString {
            Json.encodeToString(it.buildFilter())
        })
        Log.d("AddGroupBodyFragment", "Action Item:")
        Log.d("AddGroupBodyFragment", actionItem.toString())
        Log.d("AddGroupBodyFragment", "Action Groups:")
        Log.d("AddGroupBodyFragment", actionGroups.joinToString("\n"))
        //Log.d("AddGroupBodyFragment", "GroupData")
        //Log.d("AddGroupBodyFragment", (requireActivity() as MainActivity).groupData.joinToString("\n"))


        // Filters
        val filterMap: MutableMap<Filter.Type, Filter?> = mutableMapOf()
        for (filterItem in filterItems) {
            val filter = filterItem.buildFilter()
            filter.filterType?.let { filterMap.put(it, filter) }
        }

//        // Group List
//        var groupList: MutableList<ActionGroup> = mutableListOf()
//        if (actionGroups.isNotEmpty()) {
//            groupList = actionGroups
//        }

        // Default Action
        var action: Action? = null
        if (actionItem[0].isNotEmpty()) {
            action = when (actionItem[0]) {
                "Enhance" -> EnhanceAction(adapterAction.getLevelNumber())
                "Reset" -> StatusAction(Relic.Status.DEFAULT)
                else -> StatusAction(Relic.Status.valueOf(actionItem[0].uppercase()))
            }
        }

        // Newly-Created Group
        if (filterMap.isNotEmpty() ||
//            groupList.isNotEmpty() ||
            action != null) {
            val group = ActionGroup(
                filters=filterMap,
//                groupList=groupList,
                action=action
            )

            val resultBundle = Bundle().apply {
                putParcelable("group", group)
                putBoolean("isChild", creatingChild)
            }

            parentFragmentManager.setFragmentResult("new_group", resultBundle)
        }
    }

    private fun blurBackground() {
        requireActivity().findViewById<View>(R.id.activity_main_layout).blur()
    }

    private fun showDeleteRuleDialog(index: Int, group: ActionGroup) {
        val dialog = DeleteRuleDialogFragment.newInstance(index, group)
        dialog.show(parentFragmentManager, "DeleteRuleDialog")
    }
}