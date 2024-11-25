package com.example.hsrrelicmanager.core.components.ui

import android.graphics.Rect
import com.example.hsrrelicmanager.core.components.MUIElement
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.UIElement
import com.example.hsrrelicmanager.core.components.impl.MButtonImpl
import com.example.hsrrelicmanager.core.io.Clicker

interface UIButton: UIElement {
    companion object {
        operator fun invoke(
            clickArea: Rect,
            ctx: UIContext,
        ): UIButton = MButtonImpl(clickArea, ctx)
    }
    val clicker: Clicker
        get() = ctx.clicker
    val clickArea: Rect
    suspend fun click()

    override val rect get() = clickArea
}

interface MUIButton: UIButton, MUIElement {
    companion object {
        operator fun invoke(
            clickArea: Rect,
            ctx: UIContext,
        ): MUIButton = MButtonImpl(clickArea, ctx)
    }
}