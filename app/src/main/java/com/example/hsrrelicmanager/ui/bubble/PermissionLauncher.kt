package com.example.hsrrelicmanager.ui.bubble

import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.hsrrelicmanager.task.HSRAutoClickService
import com.example.hsrrelicmanager.core.AutoclickService

class PermissionLauncher : AppCompatActivity() {
    private val mediaProjectionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode != RESULT_OK || res.data == null) {
            // finish()
            return@registerForActivityResult
        }
        onMediaProjection(res.data!!)
    }
    private val overlayLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}
    private val mediaProjectionManager by lazy {
        getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    private fun onMediaProjection(mediaProjection: Intent) {
        val i = Intent(this, HSRAutoClickService::class.java)
            .setAction(AutoclickService.ACTION_INIT_PERMISSION)
            .putExtra(AutoclickService.EXTRA_RESULT_CODE, RESULT_OK)
            .putExtra(AutoclickService.EXTRA_RESULT_DATA, mediaProjection)
            .putExtras(intent)
        startForegroundService(i)
        finishAndRemoveTask()
    }

    private fun requestAccessibility(): Boolean {
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        val packageName = packageName
        val className = HSRAutoClickService::class.java.name
        val enabled = enabledServices != null && "$packageName/$className" in enabledServices

        if (enabled) return true
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        return false
    }

    private fun requestOverlay(): Boolean {
        if (Settings.canDrawOverlays(this))
            return true
        overlayLauncher.launch(
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.parse("package:$packageName")
            }
        )
        return false
    }
    private fun requestMediaProjection() {
        mediaProjectionLauncher.launch(
            mediaProjectionManager.createScreenCaptureIntent()
        )
    }

    override fun onResume() {
        super.onResume()
        if (!requestOverlay()) return
        if (!requestAccessibility()) return
        requestMediaProjection()
    }
}