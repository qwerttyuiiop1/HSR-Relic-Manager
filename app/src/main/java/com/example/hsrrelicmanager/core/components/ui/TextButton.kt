package com.example.hsrrelicmanager.core.components.ui

import android.graphics.Rect
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.impl.MTextButtonImpl
import com.example.hsrrelicmanager.core.components.ui.TextArea.Companion.getDefaultScale


interface TextButton: TextArea, UIButton {
    companion object {
        operator fun invoke(
            area: Rect,
            ctx: UIContext,
            clickArea: Rect = area,
            label: Set<String>? = null,
            scale: Float = getDefaultScale(area),
        ): TextButton = MTextButtonImpl(
            area, scale, label, clickArea, ctx
        )
    }

    override val rect
        get() = Rect(area).apply {
            union(clickArea)
        }
}
interface MTextButton: TextButton, MTextArea, MUIButton {
    companion object {
        operator fun invoke(
            area: Rect,
            ctx: UIContext,
            clickArea: Rect = area,
            label: Set<String>? = null,
            scale: Float = getDefaultScale(area),
        ): MTextButton = MTextButtonImpl(
            area, scale, label, clickArea, ctx
        )
    }
}