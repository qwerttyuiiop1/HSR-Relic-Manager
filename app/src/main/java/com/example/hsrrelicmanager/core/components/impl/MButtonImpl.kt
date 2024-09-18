package com.example.hsrrelicmanager.core.components.impl

import android.graphics.Rect
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.ui.MUIButton
import kotlinx.coroutines.yield

internal open class MButtonImpl(
    final override val clickArea: Rect,
    override val ctx: UIContext
): MUIButton {
    private val orig = Rect(clickArea)
    override suspend fun click() {
        yield() // check for cancellation before clicking
        // TODO: check if clickArea is valid based on tick
        clicker.click(clickArea)
    }
    override fun setPos(x: Int, y: Int) = clickArea.offsetTo(x, y)
    override fun setRect(rect: Rect) {
        clickArea.set(rect)
    }

    override fun reset() = setRect(orig)
}