package com.example.hsrrelicmanager.core.io

import android.view.View
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.exe.TaskResult

interface IBubbleController {
    companion object {
        const val UI_MILLIS_DELAY = 500L
    }
    /**
     * when the view is added to the window manager,
     * the view is not immediately visible
     */
    var onShowListener: (() -> Unit)?

    /**
     * when the view is completely hidden,
     * currently, it only uses a delay
     */
    var onHideListener: (() -> Unit)?

    /**
     * The task is confirmed when the user clicks
     * on a task and after the view is hidden
     */
    var onConfirmTaskListener: ((Task) -> Unit)?

    /**
     * when the overlay is available
     */
    var onBindListener: (() -> Unit)?

    fun setSelectedTask(task: Task)
    fun start(): Boolean
    fun close()
    fun hideView()
    fun showView()
    fun getOverlaysToHide(): List<View>
    fun alert(res: TaskResult)
}