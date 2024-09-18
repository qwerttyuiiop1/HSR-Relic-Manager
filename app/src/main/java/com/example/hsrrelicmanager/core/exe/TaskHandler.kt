package com.example.hsrrelicmanager.core.exe

import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext

/**
 * performs tasks
 */
class TaskHandler(private val uiCtx: UIContext) {
    private val executor = TaskExecutor(uiCtx.screenshot)
    private var currentTask = Task.NONE

    private var handler = mutableMapOf<Task, (UIContext)-> TaskRunner>()
    private var taskMap = mutableMapOf<String, Task>()

    var onTaskChangeListener: ((Task)->Unit)? = null
    var onCompleteListener: ((TaskResult)->Unit)? = null


    private var isPaused = false

    init {
        executor.onTaskComplete = { res ->
            if (currentTask == executor.runner?.task)
                setTask(Task.NONE)
            onCompleteListener?.invoke(res)
        }
        setHandler(Task.NONE) {
            throw IllegalStateException("Task NONE should not be handled")
        }
    }

    fun setHandler(task: Task, factory: (UIContext)-> TaskRunner) {
        handler[task] = factory
        taskMap[task.name] = task
    }

    fun pause() {
        isPaused = true
        executor.stop()
    }
    fun resume() {
        isPaused = false
        executor.start()
    }

    private fun setTask(task: Task) {
        currentTask = task
        executor.runner = null
        onTaskChangeListener?.invoke(task)
    }
    fun performTask(task: Task) {
        if (currentTask == task && task.isLongRunning)
            return

        setTask(task)
        val factory = handler[task] ?: throw IllegalArgumentException("Task not found")

        if (task == Task.NONE) {
            // pass
        } else if (!task.isLongRunning) {
            executor.runSingleTick(factory(uiCtx)) {
                setTask(Task.NONE)
            }
        } else if (!isPaused){
            executor.runner = factory(uiCtx)
            executor.start()
        }
    }

    fun performTask(task: String) {
        performTask(taskMap[task] ?: throw IllegalArgumentException("Task not found"))
    }

    fun close() = executor.close()
}