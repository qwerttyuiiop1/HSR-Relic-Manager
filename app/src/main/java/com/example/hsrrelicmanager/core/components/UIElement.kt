package com.example.hsrrelicmanager.core.components

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
}

/**
 * a group of UI elements
 */
interface UIGroup: UIElement