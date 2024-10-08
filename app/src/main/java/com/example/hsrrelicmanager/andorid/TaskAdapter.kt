package com.example.hsrrelicmanager.andorid


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.android.SharedForegroundNotif
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.databinding.TaskItemLayoutBinding

/**
 * adapter for the bubble recycler view
 */
class TaskAdapter(
    private val taskList: List<Task>,
    private val lifecycle: LifecycleOwner,
    private val bubbleController: BubbleController,
    private val ctx: Context
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount() = taskList.size

    inner class TaskViewHolder(
        private val binding: TaskItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var task: Task? = null
        init {
            binding.container.setOnClickListener {
                if (task == Task.CLOSE) {
                    SharedForegroundNotif.showConfirmExit(
                        ctx,
                        onConfirm = {
                            bubbleController.setSelectedTask(Task.CLOSE)
                        }
                    )
                } else {
                    bubbleController.setSelectedTask(task!!)
                }
            }
            bubbleController.selectedTask.observe(lifecycle, ::updateBackground)
        }
        private fun updateBackground(currentTask: Task) {
            itemView.background = if (currentTask == task)
                ContextCompat.getDrawable(itemView.context, R.drawable.bg_selected)
            else null
        }
        fun bind(task: Task) {
            this.task = task

            binding.taskIcon.setImageResource(task.icon)
            binding.taskName.text = itemView.context.getString(task.displayName)
            updateBackground(bubbleController.selectedTask.value!!)
        }
    }
}
