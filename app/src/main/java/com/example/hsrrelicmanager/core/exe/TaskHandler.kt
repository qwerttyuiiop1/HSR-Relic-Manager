package com.example.hsrrelicmanager.core.exe

import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext

/**
 * performs tasks
 */
class TaskHandler(private val uiCtx: UIContext) {
    private val executor = TaskExecutor(uiCtx.screenshot)
    private var currentTask = Task.NONE

    private var handler = mutableMapOf<Task, TaskRunner>()
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
    }

    fun setHandler(runner: TaskRunner) {
        val task = runner.task
        handler[task] = runner
        taskMap[task.name] = task
    }

    fun pause() {
        executor.queue {
            isPaused = true
            executor.stop()
        }
    }
    private var shouldInitialize = false
    fun resume() {
        executor.queue {
            isPaused = false
            if (shouldInitialize) {
                runner?.initialize(uiCtx)
                shouldInitialize = false
            }
            executor.start()
        }
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
        val newTask = handler[task] ?: throw IllegalArgumentException("Task not found: $task")

        if (task == Task.NONE) {
            // pass
        } else if (!task.isLongRunning) {
            executor.queue {
                newTask.initialize(uiCtx)
                runSingleTick(newTask) {
                    setTask(Task.NONE)
                }
            }
        } else {
            executor.queue {
                if (!isPaused) {
                    newTask.initialize(uiCtx)
                    executor.runner = newTask
                    start()
                } else {
                    shouldInitialize = true
                }
            }
        }
    }

    fun performTask(task: String) {
        performTask(taskMap[task] ?: throw IllegalArgumentException("Task not found"))
    }

    fun close() = executor.close()
}