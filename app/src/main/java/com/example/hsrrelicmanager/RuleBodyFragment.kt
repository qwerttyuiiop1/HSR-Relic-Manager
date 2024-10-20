package com.example.hsrrelicmanager

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
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

                for (i in index..<groupAdapter.groupData.size) {
                    groupData.get(i).position = i;
                }

                groupAdapter.notifyItemRemoved(index);
                groupAdapter.notifyItemRangeChanged(index, groupAdapter.groupData.size-index);
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

                } else {
                    card.setBackgroundResource(R.drawable.bg_dark)
                    trash.visibility = View.GONE

                    upArrow.setColorFilter(Color.parseColor("#FFFFFF"));
                    downArrow.setColorFilter(Color.parseColor("#FFFFFF"));
                    upArrow.alpha = 0.3f
                    downArrow.alpha = 0.3f

                    position.setTextColor(Color.parseColor("#FFFFFF"));
                    position.alpha = 0.3f
                }

                itemView.translationX = dX

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
