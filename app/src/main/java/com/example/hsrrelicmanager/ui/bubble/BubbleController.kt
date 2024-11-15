package com.example.hsrrelicmanager.ui.bubble


import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.databinding.ObservableField
import com.example.hsrrelicmanager.core.android.SharedForegroundNotif
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.exe.TaskResult
import com.example.hsrrelicmanager.core.io.IBubbleController
import com.example.hsrrelicmanager.core.io.IBubbleController.Companion.UI_MILLIS_DELAY


/**
 * wrapper class providing an interface to bubble menu that
 * is responsible for controlling the bubble
 */
class BubbleController(
    private val svc: Service
) : IBubbleController {
    /**
     * when the view is added to the window manager,
     * the view is not immediately visible
     */
    override var onShowListener: (()->Unit)? = null
    /**
     * when the view is completely hidden,
     * currently, it only uses a delay
     */
    override var onHideListener: (()->Unit)? = null
    /**
     * The task is confirmed when the user clicks
     * on a task and after the view is hidden
     */
    override var onConfirmTaskListener: ((Task)->Unit)? = null
    /**
     * when the overlay is available
     */
    override var onBindListener: (()->Unit)? = null


    private val handler = Handler(Looper.getMainLooper())


    //Bubble menu part
    override val selectedTask = ObservableField(Task.NONE)

    private var hasSelected = false
    private val onHidden = Runnable {
        this.onHideListener?.invoke()
        if (hasSelected)
            onConfirmTaskListener?.invoke(selectedTask.get()!!)
    }
    override fun onSetVisibility(visible: Boolean) {
        if (visible) {
            hasSelected = false
            onShowListener?.invoke()
            handler.removeCallbacks(onHidden)
        } else {
            handler.removeCallbacks(onHidden)
            handler.postDelayed(onHidden, UI_MILLIS_DELAY)
        }
    }
    override fun onTaskSelect(task: Task) {
        hasSelected = true
        selectedTask.set(task)
        hideView()
    }
    override fun setSelectedTask(task: Task) {
        selectedTask.set(task)
    }


    //service part
    private var _bubbleMenu: HSRBubbleService? = null
    private val bubbleMenu get() = _bubbleMenu!!

    private val connection = object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            _bubbleMenu = (p1 as HSRBubbleService.Binder).getService()
            bubbleMenu.setBubbleController(this@BubbleController)
            handler.postDelayed({
                onBindListener?.invoke()
            }, UI_MILLIS_DELAY)
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            _bubbleMenu = null
        }
    }

    override fun start() =
        svc.bindService(
            Intent(svc, HSRBubbleService::class.java),
            connection, Context.BIND_AUTO_CREATE
        )
    override fun close() {
        handler.removeCallbacksAndMessages(null)
        bubbleMenu.close()
        _bubbleMenu = null
        svc.unbindService(connection)
    }
    override fun hideView() = bubbleMenu.hideBubble()
    override fun showView() = bubbleMenu.showBubble()
    override fun getBubble() = bubbleMenu.bubbleView
    override fun alert(res: TaskResult) {
        SharedForegroundNotif.alert(svc, res)
    }
}