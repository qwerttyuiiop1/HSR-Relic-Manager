package com.example.hsrrelicmanager.core.components.ui

import android.graphics.Rect
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.children
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.impl.MButtonImpl
import com.example.hsrrelicmanager.core.components.impl.MTextButtonImpl
import com.example.hsrrelicmanager.core.components.ui.TextArea.Companion.getDefaultScale
import com.example.hsrrelicmanager.core.components.views.TextAreaView
import com.example.hsrrelicmanager.core.ext.rect


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

typealias UIBldr = TextButtonBuilder
class TextButtonBuilder(
    var area: Rect? = null,
    var scale: Float? = null,
    var ctx: UIContext? = null,
    var label: Set<String>? = null,
    var clickArea: Rect? = null,
) {
    constructor(
        v: TextView, ctx: UIContext,
        btn: Button? = null, scale: Float? = null,
    ): this() {
        textView(v, ctx, btn, scale)
    }
    constructor(
        v: TextAreaView, ctx: UIContext, btn: Button? = null,
    ): this() {
        textView(v, ctx, btn, v.scale)
    }
    constructor(
        g: ViewGroup, ctx: UIContext
    ): this() {
        viewGroup(g, ctx)
    }

    fun textArea(value: TextArea) {
        area = value.area
        scale = value.scale
        ctx = value.ctx
        label = value.label
    }
    fun button(value: UIButton) {
        clickArea = value.clickArea
        ctx = value.ctx
    }
    fun textView(
        v: TextView, ctx: UIContext,
        btn: Button? = null,
        scale: Float? = null
    ) {
        area = v.rect
        this.ctx = ctx
        this.scale = scale
        label = v.text.let {
            if (it == null || it == "") null
            else it.split(",").toSet()
        }
        clickArea = btn?.rect ?: v.rect
    }
    fun viewGroup(g: ViewGroup, ctx: UIContext) {
        var btn: Button? = null
        var textView: TextView? = null
        for (child in g.children) {
            when (child) {
                is Button -> btn = child
                is TextView -> textView = child
            }
        }
        textView(textView!!, ctx, btn)
    }

    val textButton: TextButton
        get() = mTextButton
    val mTextButton: MTextButton
        get() {
            val area = this.area ?: clickArea!!
            return MTextButtonImpl(
                area,
                scale ?: getDefaultScale(area),
                label,
                clickArea ?: area,
                ctx!!
            )
        }

    val textArea: TextArea
        get() = mTextArea
    val mTextArea: MTextArea
        get() = MTextButtonImpl(
            area!!,
            scale ?: getDefaultScale(area!!),
            label,
            clickArea ?: area!!,
            ctx!!
        )

    val button: UIButton
        get() = mButton
    val mButton: MUIButton
        get() {
            return MButtonImpl(
                clickArea!!,
                ctx!!
            )
        }
}