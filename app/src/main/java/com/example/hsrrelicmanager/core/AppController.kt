package com.example.hsrrelicmanager.core

import android.content.Intent
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.exe.TaskHandler
import com.example.hsrrelicmanager.core.io.IBubbleController
import com.example.hsrrelicmanager.core.io.Overlay
import com.example.hsrrelicmanager.core.io.RandomClicker
import com.example.hsrrelicmanager.core.io.Screenshot
import com.example.hsrrelicmanager.core.io.TextRecognizer

/**
 * Links the components of the app
 */
open class AppController (
    private val svc: AutoclickService,
    private val uiController: IBubbleController? = null,
    intent: Intent
) {
    companion object {
        const val ACTION_BUBBLE = "action_bubble"
    }
    protected val uiCtx: UIContext
    protected val taskHandler: TaskHandler

    init {
        val hasBubble = intent.getBooleanExtra(
            AutoclickService.EXTRA_HAS_BUBBLE, true)
        val clicker = RandomClicker(svc)
        val screenshot = Screenshot(svc, intent)
        uiCtx = UIContext(
            svc, clicker, TextRecognizer(), svc.handler, screenshot
        )
        taskHandler = TaskHandler(uiCtx).apply {
            onTaskChangeListener = { task ->
                uiCtx.uiHandler.post {
                    uiController?.setSelectedTask(task)
                }
            }
            onCompleteListener = { res ->
                uiCtx.uiHandler.post {
                    uiController?.alert(res)
                }
            }
        }

        if (hasBubble) {
            uiController!!.apply {
                onShowListener = { taskHandler.pause() }
                onHideListener = { taskHandler.resume() }
                onConfirmTaskListener = { handleTask(it.name) }
                onBindListener = {
                    screenshot.preprocess = Overlay(getOverlaysToHide()[0])
                }
                start()
            }
        }
    }

    /**
     * close this app controller
     */
    fun close() {
        taskHandler.close()
        uiController?.close()
        uiCtx.screenshot.close()
    }

    fun handleAction(action: String) {
        when (action) {
            ACTION_BUBBLE -> uiController?.showView()
            AutoclickService.ACTION_CLOSE -> svc.handleAction(action)
            Task.CLOSE.name -> svc.handleAction(AutoclickService.ACTION_CLOSE)
            else -> handleTask(action)
        }
    }

    open fun handleTask(task: String) {
        taskHandler.performTask(task)
    }
}