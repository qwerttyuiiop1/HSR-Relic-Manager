package com.example.hsrrelicmanager.andorid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hsrrelicmanager.core.AutoclickService

//to handle the case when the user launches the app while it is running
class DefaultLauncher : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForegroundService(
            Intent(this, AutoclickService::class.java).apply {
                action = AutoclickService.ACTION_INIT
                if (intent != null) putExtras(intent)
            }
        )
        finish()
    }
}
