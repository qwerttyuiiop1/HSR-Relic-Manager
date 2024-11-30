package com.example.hsrrelicmanager.ui

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

// TODO: update String to Action
class ActionItemAdapter(private val items: MutableList<String>) : RecyclerView.Adapter<ActionItemAdapter.ActionViewHolder>() {

    private var selectedOption: String = ""
    private var levelNumber: Int = 3

    class ActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val choiceText: TextView = itemView.findViewById(R.id.action_choice_text)
        val actionImage: ImageView = itemView.findViewById(R.id.action_image)
        val levelTitle: TextView = itemView.findViewById(R.id.level_title)
        val levelNumberText: TextView = itemView.findViewById(R.id.level_number_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_action, parent, false)
        return ActionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val choice = items[0]

        holder.choiceText.text = if (choice.isEmpty()) "+ Default" else choice
        holder.actionImage.setImageResource(getImageResource(choice))

        // Show or hide level information based on choice
        if (choice == "Enhance") {
            holder.levelTitle.visibility = View.VISIBLE
            holder.levelNumberText.visibility = View.VISIBLE
            holder.levelNumberText.text = levelNumber.toString()
        } else {
            holder.levelTitle.visibility = View.GONE
            holder.levelNumberText.visibility = View.GONE
        }

        holder.choiceText.setOnClickListener {
            selectedOption = choice
            showDialog(it.context, holder)
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

        when (selectedOption) {
            "Enhance" -> updateRadioButtonState(dialogView, R.id.radio_button_enhance)
            "Lock" -> updateRadioButtonState(dialogView, R.id.radio_button_lock)
            "Reset" -> updateRadioButtonState(dialogView, R.id.radio_button_reset)
//            "Filter Group" -> updateRadioButtonState(dialogView, R.id.radio_button_filter)
            "Trash" -> updateRadioButtonState(dialogView, R.id.radio_button_trash)
        }

        // Set up radio button click listeners
        dialogView.findViewById<View>(R.id.enhance_action_group).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_enhance)
            selectedOption = "Enhance"
        }
        dialogView.findViewById<View>(R.id.radio_button_enhance).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_enhance)
            selectedOption = "Enhance"
        }
        dialogView.findViewById<View>(R.id.lock_action_group).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_lock)
            selectedOption = "Lock"
        }
        dialogView.findViewById<View>(R.id.radio_button_lock).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_lock)
            selectedOption = "Lock"
        }
        dialogView.findViewById<View>(R.id.reset_action_group).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_reset)
            selectedOption = "Reset"
        }
        dialogView.findViewById<View>(R.id.radio_button_reset).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_reset)
            selectedOption = "Reset"
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
            selectedOption = "Trash"
        }
        dialogView.findViewById<View>(R.id.radio_button_trash).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_trash)
            selectedOption = "Trash"
        }

        // Update level number
        dialogView.findViewById<ImageView>(R.id.ic_add_level).setOnClickListener {
            if (levelNumber < 15) {
                levelNumber += 3
                updateLevelNumberDisplay(levelNumberTextView)
                holder.levelNumberText.text = levelNumber.toString()
            }
        }

        dialogView.findViewById<ImageView>(R.id.ic_subtract_level).setOnClickListener {
            if (levelNumber > 3) {
                levelNumber -= 3
                updateLevelNumberDisplay(levelNumberTextView)
                holder.levelNumberText.text = levelNumber.toString()
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
            items[0] = selectedOption
            notifyItemChanged(holder.adapterPosition)

            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            bgView.setRenderEffect(null)
        }

        dialog.show()
    }

    private fun updateLevelNumberDisplay(levelTextView: TextView) {
        levelTextView.text = levelNumber.toString()
    }

    private fun getImageResource(option: String): Int {
        return when (option) {
            "Enhance" -> R.drawable.enhance
            "Lock" -> R.drawable.lock
            "Reset" -> R.drawable.reset
//            "Filter Group" -> R.drawable.filter_group
            "Trash" -> R.drawable.trash
            else -> R.drawable.transparent
        }
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
        return levelNumber
    }
}
