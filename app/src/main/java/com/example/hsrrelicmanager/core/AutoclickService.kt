package com.example.hsrrelicmanager.core

import android.accessibilityservice.AccessibilityService
import android.app.Activity
import android.content.Intent
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import com.example.hsrrelicmanager.core.android.SharedForegroundNotif
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.exe.TaskRunner
import com.example.hsrrelicmanager.core.io.IBubbleController


/**
 * handles init and close and service related stuff
 * ACTION_INIT:             starts the service; accepts a boolean if the bubble should be shown
 *                          if the service is already running, it will show the bubble
 *                          if the media projection permission is not granted, it will start the permission launcher
 * ACTION_INIT_PERMISSION:  ACTION_INIT but with a media projection intent
 * ACTION_CLOSE:            closes the service and frees resources,
 *                          and enters a sleep state where it will not be executing tasks
 * ELSE:                    forwards the task to AppController
 */
abstract class AutoclickService : AccessibilityService() {
    companion object {
        const val ACTION_INIT = "action_init"
        const val ACTION_CLOSE = "action_close"
        const val ACTION_INIT_PERMISSION = "action_init_permission"

        const val INTENT_REQUEST_CODE = 200
        const val EXTRA_RESULT_CODE = "extra_result_code"
        const val EXTRA_RESULT_DATA = "extra_result_data"
        const val EXTRA_HAS_BUBBLE = "extra_has_bubble"
    }

    protected var appController: AppController? = null
    val handler = android.os.Handler(Looper.getMainLooper())
    private var cached: Intent? = null

    override fun onDestroy() {
        super.onDestroy()
        close()
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?){}
    override fun onInterrupt() {}

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action != null)
            handleAction(intent.action!!, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    open fun setupBubbleController(): IBubbleController {
        throw NotImplementedError("BubbleController is required")
    }
    open fun startWithPermissions(intent: Intent?) {
        throw NotImplementedError("AutoclickService was not started with permissions. Implement this method or start a new launcher")
    }

    protected open fun setupNotif(hasBubble: Boolean) {
        startForeground(
            SharedForegroundNotif.NOTIFICATION_ID,
            SharedForegroundNotif.build(this, hasBubble)
        )
    }
    open fun handleAction(action: String, intent: Intent? = null) {
        when (action) {
            ACTION_CLOSE -> close()
            ACTION_INIT -> {
                if (appController == null) {
                    if (cached != null) {
                        setupNotif(false)
                        handleAction(ACTION_INIT_PERMISSION, cached)
                    } else {
                        startWithPermissions(intent)
                    }
                } else {
                    appController!!.handleAction(AppController.ACTION_BUBBLE)
                }
            }
            ACTION_INIT_PERMISSION -> {
                if (appController == null) {
                    val resultCode =
                        intent!!.getIntExtra(EXTRA_RESULT_CODE, Activity.RESULT_CANCELED)
                    if (resultCode == Activity.RESULT_OK) {
                        cached = intent.clone() as Intent
                        val hasBubble = intent.getBooleanExtra(EXTRA_HAS_BUBBLE, true)
                        setupNotif(hasBubble)
                        appController = AppController(this, setupBubbleController(), intent)
                    }
                }
            }
            else -> appController?.handleAction(action)
        }
    }

    private fun close() {
        appController?.close()
        appController = null
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    protected fun addHandler(task: Task, factory: (UIContext)-> TaskRunner) {
        appController?.taskHandler?.setHandler(task, factory)
    }
}