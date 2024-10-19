package com.example.hsrrelicmanager

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

class ActionItemAdapter(private val items: MutableList<String>) : RecyclerView.Adapter<ActionItemAdapter.ActionViewHolder>() {

    private var selectedOption: String = ""

    class ActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val choiceText: TextView = itemView.findViewById(R.id.action_choice_text)
        val actionImage: ImageView = itemView.findViewById(R.id.action_image) // Reference to the ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.action_item, parent, false)
        return ActionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val choice = items[0]

        holder.choiceText.text = if (choice.isEmpty()) "+" else choice
        holder.actionImage.setImageResource(getImageResource(choice))

        holder.choiceText.setOnClickListener {
            showDialog(it.context, holder)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showDialog(context: Context, holder: ActionViewHolder) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_buttons, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)

        val dialog = builder.create()

        val activity = context as MainActivity
        val bgView = activity.findViewById<View>(R.id.activity_main_layout)
        bgView.setRenderEffect(
            RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
        )

        dialogView.findViewById<ImageView>(R.id.radio_button_enhance).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_enhance)
            selectedOption = "Enhance"
        }
        dialogView.findViewById<ImageView>(R.id.radio_button_lock).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_lock)
            selectedOption = "Lock"
        }
        dialogView.findViewById<ImageView>(R.id.radio_button_reset).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_reset)
            selectedOption = "Reset"
        }
        dialogView.findViewById<ImageView>(R.id.radio_button_filter).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_filter)
            selectedOption = "Filter Group"
        }
        dialogView.findViewById<ImageView>(R.id.radio_button_trash).setOnClickListener {
            updateRadioButtonState(dialogView, R.id.radio_button_trash)
            selectedOption = "Trash"
        }

        val cancelButton = dialogView.findViewById<View>(R.id.cancel_action_group_dialog_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        val confirmButton = dialogView.findViewById<View>(R.id.confirm_action_group_dialog_button)
        confirmButton.setOnClickListener {
            items[0] = selectedOption
            notifyItemChanged(0)

            holder.actionImage.setImageResource(getImageResource(selectedOption))

            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            bgView.setRenderEffect(null)
        }

        dialog.show()
    }

    private fun getImageResource(option: String): Int {
        return when (option) {
            "Enhance" -> R.drawable.enhance
            "Lock" -> R.drawable.lock
            "Reset" -> R.drawable.reset
            "Filter Group" -> R.drawable.filter_group
            "Trash" -> R.drawable.trash
            else -> R.drawable.transparent
        }
    }

    private fun updateRadioButtonState(dialogView: View, radioButtonId: Int) {
        val radioButtons = listOf(
            R.id.radio_button_enhance,
            R.id.radio_button_lock,
            R.id.radio_button_reset,
            R.id.radio_button_filter,
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
}
