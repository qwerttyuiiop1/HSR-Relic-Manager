package com.example.hsrrelicmanager.ui

import android.graphics.Canvas
import android.graphics.Color
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
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import com.example.hsrrelicmanager.ui.CategorizedGroupAdapter.GroupViewHolder
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
        val groupData = (activity as MainActivity).groupData
        val groupAdapter = GroupAdapter(groupData, this)

        Log.d(this::class.toString(), groupData.joinToString(",\n"))

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = groupAdapter

        var swipedItemIndex: Int = -1

        // Listens for action from delete dialog
        parentFragmentManager.setFragmentResultListener("delete_rule_request", viewLifecycleOwner) { _, bundle ->
            val index = bundle.getInt("index")
            val action = bundle.getString("action")

            // Delete confirmed
            if (action == "confirm" && index >= 0) {
                groupAdapter.groupData.removeAt(index)

                for (i in index until groupAdapter.groupData.size) {
                    groupData[i].position = i
                }

                groupAdapter.notifyItemRemoved(index)
                groupAdapter.notifyItemRangeChanged(index, groupAdapter.groupData.size - index)

                Toast.makeText(requireContext(), "Rule trashed.", Toast.LENGTH_SHORT).show()

            // Delete cancelled
            } else if (action == "cancel" && swipedItemIndex != -1) {
                groupAdapter.notifyItemChanged(swipedItemIndex)
            }
            swipedItemIndex = -1
        }

        // Listens for new rules created
        parentFragmentManager.setFragmentResultListener("new_group", viewLifecycleOwner) { _, bundle ->
            val group = bundle.getParcelable<ActionGroup>("group")

            // Check if a new group was created
            if (group != null) {
                groupData.add(group)
                groupAdapter.notifyDataSetChanged()

                // Update list position indices
                for (index in 0..<groupData.size) {
                    groupData[index].position = index
                }
            }
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
                Collections.swap(groupAdapter.groupData, viewHolder.adapterPosition, target.adapterPosition);

//                Log.d(this::class.toString(), "From ${viewHolder.adapterPosition} to ${target.adapterPosition}")

//                groupAdapter.groupData[viewHolder.adapterPosition].position = target.adapterPosition;
//                groupAdapter.groupData[target.adapterPosition].position = viewHolder.adapterPosition;

                val lo = minOf(viewHolder.adapterPosition, target.adapterPosition)
                val hi = maxOf(viewHolder.adapterPosition, target.adapterPosition)

                for (index in lo..hi) {
                    groupData[index].position = index
                }

                (viewHolder as GroupAdapter.ViewHolder).updatePosition(target.adapterPosition);
                (target as GroupAdapter.ViewHolder).updatePosition(viewHolder.adapterPosition);
                groupAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition);

//                Log.d(this::class.toString(), groupData.joinToString(",\n"))

                return true;
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val index = viewHolder.adapterPosition
                val group = groupAdapter.groupData[index]

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
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun blurBackground() {
        requireActivity().findViewById<View>(R.id.activity_main_layout).blur()
    }


    private fun showDeleteRuleDialog(index: Int, group: ActionGroup) {
        val dialog = DeleteRuleDialogFragment.newInstance(index, group)
        dialog.show(parentFragmentManager, "DeleteRuleDialog")
    }
}

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

        //Log.d(this::class.toString(), groupData.joinToString(",\n"))

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = categorizedGroupAdapter

        var swipedItemIndex: Int = -1

        // Listens for action from delete dialog
        parentFragmentManager.setFragmentResultListener("delete_rule_request", viewLifecycleOwner) { _, bundle ->
            val index = bundle.getInt("index")
            val action = bundle.getString("action")

            // Delete confirmed
            if (action == "confirm" && index >= 0) {
                val itemIndex = index
                val groupIndex = (categorizedGroupAdapter.items.get(itemIndex) as CategorizedGroupAdapter.GroupItem).group.position;

                categorizedGroupAdapter.items.removeAt(itemIndex)
                categorizedGroupAdapter.groupData.removeAt(groupIndex)

                for (i in groupIndex..<categorizedGroupAdapter.groupData.size) {
                    groupData.get(i).position = i
                }

                categorizedGroupAdapter.notifyDataSetChanged()

                Toast.makeText(requireContext(), "Rule trashed.", Toast.LENGTH_SHORT).show()

                // Delete cancelled
            } else if (action == "cancel" && swipedItemIndex != -1) {
                categorizedGroupAdapter.notifyItemChanged(swipedItemIndex)
            }
            swipedItemIndex = -1
        }

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
                val groupIndex = (categorizedGroupAdapter.items.get(itemIndex) as CategorizedGroupAdapter.GroupItem).group.position

                val group = categorizedGroupAdapter.groupData[groupIndex]

                swipedItemIndex = itemIndex

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

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun blurBackground() {
        requireActivity().findViewById<View>(R.id.activity_main_layout).blur()
    }

    private fun showDeleteRuleDialog(index: Int, group: ActionGroup) {
        val dialog = DeleteRuleDialogFragment.newInstance(index, group)
        dialog.show(parentFragmentManager, "DeleteRuleDialog")
    }
}