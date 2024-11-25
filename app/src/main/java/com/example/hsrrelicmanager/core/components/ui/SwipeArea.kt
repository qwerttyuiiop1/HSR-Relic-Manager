package com.example.hsrrelicmanager.core.components.ui

import android.graphics.Rect
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.UIElement
import com.example.hsrrelicmanager.core.io.Clicker

class SwipeArea(
    override val rect: Rect,
    val speed: Float = Clicker.SWIPE_SPEED,
    val hold: Long = 1000,
    override val ctx: UIContext
): UIElement {
    val clicker: Clicker
        get() = ctx.clicker
    suspend fun swipeV(distance: Int) = clicker.swipeV(rect, distance, speed, hold)
    suspend fun swipeH(distance: Int) = clicker.swipeH(rect, distance, speed, hold)
}