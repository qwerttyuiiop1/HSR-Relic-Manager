package com.example.hsrrelicmanager.ui.rules

import android.app.AlertDialog
import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.ui.MainActivity

// TODO: update String to Action
class ActionItemAdapter(
    private val items: MutableList<Action?>,
    private val callback: GroupChangeListener
) : RecyclerView.Adapter<ActionItemAdapter.ActionViewHolder>() {
    var _levelNumber = 3
    class ActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val choiceText: TextView = itemView.findViewById(R.id.action_choice_text)
        val actionImage: ImageView = itemView.findViewById(R.id.action_image)
        val levelTitle: TextView = itemView.findViewById(R.id.level_title)
        val levelNumberText: TextView = itemView.findViewById(R.id.level_number_item)
        var action: Action? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_action, parent, false)
        val holder = ActionViewHolder(view)
        holder.choiceText.text = "+ Default"
        return ActionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val choice = items[position]
        holder.action = choice
        holder.choiceText.setOnClickListener {
            showDialog(it.context, holder)
        }

        if (choice == null) {
            holder.choiceText.text = "+ Default"
            holder.levelTitle.visibility = View.GONE
            holder.levelNumberText.visibility = View.GONE
            return
        }
        holder.choiceText.text = choice.name
        holder.actionImage.setImageResource(choice.image)
        when (choice) {
            is EnhanceAction -> {
                holder.levelNumberText.text = choice.targetLevel.toString()
            }
            else -> {
                holder.levelTitle.visibility = View.GONE
                holder.levelNumberText.visibility = View.GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showDialog(context: Context, holder: ActionViewHolder) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_action_rule, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)

        val dialog = builder.create()

        val activity = context as MainActivity
        val bgView = activity.findViewById<View>(R.id.activity_main_layout)
        bgView.setRenderEffect(
            RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
        )

        val levelNumberTextView = dialogView.findViewById<TextView>(R.id.level_number)
        updateLevelNumberDisplay(levelNumberTextView)

        val action = holder.action
        when (action) {
            null -> {}
            is EnhanceAction -> {
                updateRadioButtonState(dialogView, R.id.radio_button_enhance)
            }
            is StatusAction -> {
                when (action.targetStatus) {
                    Relic.Status.LOCK -> updateRadioButtonState(dialogView, R.id.radio_button_lock)
                    Relic.Status.TRASH -> updateRadioButtonState(dialogView, R.id.radio_button_trash)
                    Relic.Status.DEFAULT -> updateRadioButtonState(dialogView, R.id.radio_button_reset)
                    else -> throw IllegalArgumentException("Invalid status")
                }
            }
        }

        // Set up radio button click listeners
        dialogView.findViewById<View>(R.id.enhance_action_group).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_enhance)
            holder.action = EnhanceAction(_levelNumber)
        }
        dialogView.findViewById<View>(R.id.radio_button_enhance).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_enhance)
            holder.action = EnhanceAction(_levelNumber)
        }
        dialogView.findViewById<View>(R.id.lock_action_group).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_lock)
            holder.action = StatusAction(Relic.Status.LOCK)
        }
        dialogView.findViewById<View>(R.id.radio_button_lock).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_lock)
            holder.action = StatusAction(Relic.Status.LOCK)
        }
        dialogView.findViewById<View>(R.id.reset_action_group).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_reset)
            holder.action = StatusAction(Relic.Status.DEFAULT)
        }
        dialogView.findViewById<View>(R.id.radio_button_reset).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_reset)
            holder.action = StatusAction(Relic.Status.DEFAULT)
        }
//        dialogView.findViewById<View>(R.id.filter_action_group).setOnClickListener {
//            updateRadioButtonState(dialogView, R.id.radio_button_filter)
//            selectedOption = "Filter Group"
//        }
//        dialogView.findViewById<View>(R.id.radio_button_filter).setOnClickListener {
//            updateRadioButtonState(dialogView, R.id.radio_button_filter)
//            selectedOption = "Filter Group"
//        }
        dialogView.findViewById<View>(R.id.trash_action_group).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_trash)
            holder.action = StatusAction(Relic.Status.TRASH)
        }
        dialogView.findViewById<View>(R.id.radio_button_trash).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_trash)
            holder.action = StatusAction(Relic.Status.TRASH)
        }

        // Update level number
        dialogView.findViewById<ImageView>(R.id.ic_add_level).setOnClickListener {
            if (_levelNumber < 15) {
                _levelNumber += 3
                updateLevelNumberDisplay(levelNumberTextView)
                holder.levelNumberText.text = _levelNumber.toString()
                holder.action = EnhanceAction(_levelNumber)
            }
        }

        dialogView.findViewById<ImageView>(R.id.ic_subtract_level).setOnClickListener {
            if (_levelNumber > 3) {
                _levelNumber -= 3
                updateLevelNumberDisplay(levelNumberTextView)
                holder.levelNumberText.text = _levelNumber.toString()
                holder.action = EnhanceAction(_levelNumber)
            }
        }

    val cancelButton = dialogView.findViewById<View>(R.id.cancel_action_group_dialog_button)
    cancelButton.setOnClickListener {
        if (activity.supportFragmentManager.backStackEntryCount > 0) {
            activity.supportFragmentManager.popBackStack()
        } else {
            dialog.dismiss()
        }
    }

        val confirmButton = dialogView.findViewById<View>(R.id.confirm_action_group_dialog_button)
        confirmButton.setOnClickListener {
            notifyItemChanged(holder.adapterPosition)
            val action = holder.action!!
            callback.onUpdateAction(action)
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            bgView.setRenderEffect(null)
        }

        dialog.show()
    }

    private fun updateLevelNumberDisplay(levelTextView: TextView) {
        levelTextView.text = _levelNumber.toString()
    }

    private fun updateRadioButtonState(dialogView: View, radioButtonId: Int) {
        val radioButtons = listOf(
            R.id.radio_button_enhance,
            R.id.radio_button_lock,
            R.id.radio_button_reset,
//            R.id.radio_button_filter,
            R.id.radio_button_trash
        )

        for (id in radioButtons) {
            val radioButton = dialogView.findViewById<ImageView>(id)
            if (id == radioButtonId) {
                radioButton.setImageResource(R.drawable.ic_radio_button_checked)
            } else {
                radioButton.setImageResource(R.drawable.ic_radio_button_unchecked)
            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    fun getLevelNumber(): Int {
        return _levelNumber
    }
}
