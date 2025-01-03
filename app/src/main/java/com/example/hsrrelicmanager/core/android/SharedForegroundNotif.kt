package com.example.hsrrelicmanager.core.android

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import com.example.hsrrelicmanager.task.HSRAutoClickService
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.AppController
import com.example.hsrrelicmanager.core.AutoclickService
import com.example.hsrrelicmanager.core.exe.TaskResult


class SharedForegroundNotif(
    ctx: Context
): NotifHelper(ctx) {
    companion object {
        const val NOTIFICATION_ID = 100

        fun build(ctx: Context, hasBubble: Boolean): Notification {
            val PendingIntent: (String) -> PendingIntent = {
                PendingIntent.getService(
                    ctx, AutoclickService.INTENT_REQUEST_CODE,
                    // TODO: fix this hack
                    Intent(ctx, HSRAutoClickService::class.java).setAction(it),
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
            return ctx.run {
                NotifHelper(ctx).createNotification(
                    getString(R.string.app_name),
                    R.mipmap.ic_launcher_round,
                    getString(R.string.foreground_desc)
                ).apply {
                    if (hasBubble) {
                        addAction(
                            R.drawable.ic_launcher_foreground, getString(R.string.open_bubble),
                            PendingIntent(AppController.ACTION_BUBBLE)
                        )
                        setContentIntent(
                            PendingIntent(AppController.ACTION_BUBBLE)
                        )
                    }
                    addAction(
                        R.drawable.ic_launcher_foreground, getString(R.string.close),
                        PendingIntent(AutoclickService.ACTION_CLOSE)
                    )
                    setOngoing(true)
                    setCategory(Notification.CATEGORY_SERVICE)
                    setSilent(true)
                }.build()
            }
        }

        fun showConfirmExit(ctx: Context, onConfirm: ()->Unit) {
            ctx.apply {
                val alertDialog = android.app.AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.confirm))
                    setMessage(getString(R.string.confirm_exit))
                    setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                        onConfirm()
                        dialog.dismiss()
                    }
                    setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                }.create()
                alertDialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                alertDialog.show()
            }
        }

        fun alert(ctx: Context, res: TaskResult) {
            ctx.apply {
                val alertDialog = android.app.AlertDialog.Builder(this).apply {
//                    setTitle(getString(res.task.displayName))
                    setTitle(res.task.name)
                    val msg = when (res) {
                        is TaskResult.Success -> res.message
                        is TaskResult.Fail -> res.error.toString()
                    }
                    setMessage(msg)
                    setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                        dialog.dismiss()
                    }
                }.create()
                alertDialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
                alertDialog.show()
            }
        }
    }
}