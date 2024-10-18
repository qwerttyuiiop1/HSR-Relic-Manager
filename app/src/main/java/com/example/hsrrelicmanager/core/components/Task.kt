package com.example.hsrrelicmanager.core.components

import androidx.annotation.StringRes
import com.example.hsrrelicmanager.R

/**
 * the tasks available on the bubble menu
 */
data class Task(
    @StringRes
    val displayName: Int,
    val isLongRunning: Boolean,
    val name: String
) {
    companion object {
        val NONE = Task(
            R.string.pause, true, "NONE"
        )
        val CLOSE = Task(
            R.string.close, false, "CLOSE"
        )
        val SCREENSHOT = Task(
            R.string.take_screenshot,false, "SCREENSHOT"
        )
    }
}