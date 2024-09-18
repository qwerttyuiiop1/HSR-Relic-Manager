package com.example.hsrrelicmanager.core.components

import android.graphics.Rect

/**
 * all UI elements implement this interface
 */
interface UIElement {
    val ctx: UIContext
}

/**
 * a UI element that can be moved
 */
interface MUIElement: UIElement {
    fun setPos(x: Int, y: Int)
    fun setRect(rect: Rect)
    fun reset()
}

/**
 * a group of UI elements
 */
interface UIGroup: UIElement