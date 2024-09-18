package com.example.hsrrelicmanager.core.components.impl

import android.graphics.Rect
import com.example.hsrrelicmanager.core.components.ui.MTextArea
import com.example.hsrrelicmanager.core.components.ui.MTextButton
import com.example.hsrrelicmanager.core.components.ui.MUIButton
import com.example.hsrrelicmanager.core.components.ui.UIButton

internal open class MTextButtonImpl(
    protected val text: MTextArea,
    protected val btn: MUIButton,
) : MTextButton, MTextArea by text, UIButton by btn {
    override val ctx
        get() = text.ctx // assume text and btn have the same context

    override fun setPos(x: Int, y: Int) {
        text.setPos(x, y)
        btn.setPos(x, y)
    }
    override fun reset() {
        text.reset()
        btn.reset()
    }
    override fun setRect(rect: Rect) {
        text.setRect(rect)
        btn.setRect(rect)
    }
}