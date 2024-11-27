package com.example.hsrrelicmanager.task

import android.content.Intent
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.AutoclickService
import com.example.hsrrelicmanager.core.android.ImageDump
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.TaskRunner
import com.example.hsrrelicmanager.core.exe.invoke
import com.example.hsrrelicmanager.core.io.IBubbleController
import com.example.hsrrelicmanager.task.autobattle.AutobattleTask
import com.example.hsrrelicmanager.task.scan.OrganizeInventoryTask
import com.example.hsrrelicmanager.task.scan.ScanInventoryTask
import com.example.hsrrelicmanager.ui.MainActivity
import com.example.hsrrelicmanager.ui.bubble.BubbleController
import com.example.hsrrelicmanager.ui.bubble.PermissionLauncher

class HSRAutoClickService : AutoclickService() {
    override fun setupBubbleController(): IBubbleController {
        return BubbleController(this)
    }

    override fun startWithPermissions(intent: Intent?) {
        startActivity(
            Intent(
                this,
                PermissionLauncher::class.java
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (intent != null) putExtras(intent)
            }
        )
    }

    override fun onInit() {
        addHandler(TaskRunner(Task.NONE) {
            throw IllegalStateException("Task NONE should not be handled")
        })

        val screenshot = Task(
            false, "SCREENSHOT", R.string.take_screenshot,
        )
        addHandler(TaskRunner(screenshot) { uiCtx ->
            awaitTick()
            ImageDump(uiCtx.ctx).dump(tick)
            MyResult.Success("")
        })

        val app = Task(false, "APP")
        addHandler(TaskRunner(app) { uiCtx ->
            // start main activity
            val i = Intent(uiCtx.ctx, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            uiCtx.ctx.startActivity(i)
            MyResult.Success("")
        })

        val github = Task(false, "GITHUB")
        addHandler(TaskRunner(github) { uiCtx ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse("https://github.com/qwerttyuiiop1/HSR-Relic-Manager/")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            uiCtx.ctx.startActivity(intent)
            MyResult.Success("")
        })

        addHandler(AutobattleTask())
        addHandler(ScanInventoryTask())
        addHandler(OrganizeInventoryTask())
    }
}