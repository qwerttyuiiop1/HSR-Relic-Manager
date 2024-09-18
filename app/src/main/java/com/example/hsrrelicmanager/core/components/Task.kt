package com.example.hsrrelicmanager.core.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.hsrrelicmanager.R

/**
 * the tasks available on the bubble menu
 */
data class Task(
    @DrawableRes
    val icon: Int,
    @StringRes
    val displayName: Int,
    val isLongRunning: Boolean,
    val name: String
) {
    companion object {
        val NONE = Task(
            R.drawable.ic_launcher_foreground, R.string.pause,
            true, "NONE"
        )
        val CLOSE = Task(
            R.drawable.ic_launcher_background, R.string.close,
            false, "CLOSE"
        )
        val SCREENSHOT = Task(
            R.drawable.ic_launcher_background, R.string.take_screenshot,
            false, "SCREENSHOT"
        )
    }
}