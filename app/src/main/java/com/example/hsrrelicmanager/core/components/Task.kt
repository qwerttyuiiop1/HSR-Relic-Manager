package com.example.hsrrelicmanager.core.components

import androidx.annotation.StringRes
import com.example.hsrrelicmanager.R

/**
 * the tasks available on the bubble menu
 */
data class Task(
    val isLongRunning: Boolean,
    val name: String,
    @StringRes val displayName: Int = -1
) {
    companion object {
        val NONE = Task(
            true, "NONE", R.string.pause
        )
        val CLOSE = Task(
            false, "CLOSE", R.string.close,
        )
        val SCREENSHOT = Task(
            false, "SCREENSHOT", R.string.take_screenshot,
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is Task && other.name == name
    }
    override fun hashCode(): Int {
        return name.hashCode()
    }
}