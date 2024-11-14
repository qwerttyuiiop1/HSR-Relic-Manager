package com.example.hsrrelicmanager.ui.bubble

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.Observable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.hsrrelicmanager.core.android.SharedForegroundNotif
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.io.IBubbleController
import com.example.hsrrelicmanager.databinding.ControllerLayoutBinding
import com.torrydo.floatingbubbleview.service.expandable.ExpandedBubbleBuilder


@SuppressLint("UseCompatLoadingForDrawables")
class BubbleMenu(
    private val ctx: Context
): LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    override val lifecycle = lifecycleRegistry
    private lateinit var controller: IBubbleController
    init {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    private val view = ControllerLayoutBinding.inflate(LayoutInflater.from(ctx))
    private val tasks = mapOf(
        "NONE" to view.btnPause,
        "APP" to view.btnApp,
        "AUTOBATTLE" to view.btnAutobattle,
        "CLOSE" to view.btnClose,
        "GITHUB" to view.btnGithub,
        "ORGANIZE" to view.btnOrganizeInventory,
        "SCAN" to view.btnScanInventory,
        "SCREENSHOT" to view.btnScreenshot,
    )
    private val onTaskChange = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            tasks.forEach { (name, button) ->
                button.background = if (name == controller.selectedTask.get()) {
                    ctx.getDrawable(com.example.hsrrelicmanager.R.drawable.bg_selected)
                } else {
                    null
                }
            }
        }
    }

    fun onCreate(controller: IBubbleController) {
        this.controller = controller
        controller.selectedTask.addOnPropertyChangedCallback(onTaskChange)
    }

    init {
        tasks.forEach { (name, button) ->
            if (name == Task.CLOSE.name) {
                button.setOnClickListener {
                    SharedForegroundNotif.showConfirmExit(
                        ctx,
                        onConfirm = {
                            controller.setSelectedTask(Task.CLOSE.name)
                        }
                    )
                }
            } else {
                button.setOnClickListener {
                    controller.setSelectedTask(name)
                }
            }
        }

        view.mainContainer.setOnClickListener {
            // prevent click from going to parent
        }
    }

    fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        controller.selectedTask.removeOnPropertyChangedCallback(onTaskChange)
    }

    fun builder() = ExpandedBubbleBuilder(ctx)
        .expandedView(view.root)
        .startLocation(0, 0)
        .dimAmount(0.75F)
        .fillMaxWidth(true)
}