package com.example.hsrrelicmanager

import android.content.Intent
import com.example.hsrrelicmanager.core.AutoclickService
import com.example.hsrrelicmanager.core.io.IBubbleController
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

    override fun onCreate() {
        super.onCreate()
    }
}