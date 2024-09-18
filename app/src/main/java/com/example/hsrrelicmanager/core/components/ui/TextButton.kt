package com.example.hsrrelicmanager.core.components.ui

import android.graphics.Rect
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.children
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.impl.MButtonImpl
import com.example.hsrrelicmanager.core.components.impl.MTextAreaImpl
import com.example.hsrrelicmanager.core.components.impl.MTextButtonImpl
import com.example.hsrrelicmanager.core.components.ui.TextArea.Companion.getDefaultScale
import com.example.hsrrelicmanager.core.ext.rect


interface TextButton: TextArea, UIButton
interface MTextButton: TextButton, MTextArea, MUIButton

typealias TBtnBldr = TextButtonBuilder
class TextButtonBuilder(
    var rect: Rect? = null,
    var scale: Float? = null,
    var label: Set<String>? = null,
    var clickArea: Rect? = null,
    var ctx: UIContext? = null,
) {
    constructor(
        rect: Rect,
        btn: UIButton,
        ctx: UIContext,
        scale: Float = getDefaultScale(rect),
    ): this(rect, scale, null, btn.clickArea, ctx)
    constructor(
        rect: Rect,
        clickArea: Rect = rect,
        ctx: UIContext,
        scale: Float = getDefaultScale(rect),
    ) : this(rect, scale, null, rect, ctx)
    constructor(
        v: TextView, ctx: UIContext, btn: Button? = null,
    ): this() {
        textView(v, ctx, btn)
    }
    constructor(
        g: ViewGroup, ctx: UIContext
    ): this() {
        viewGroup(g, ctx)
    }

    fun textArea(value: TextArea) {
        rect = value.area
        scale = value.scale
        label = value.label
    }
    fun button(value: UIButton) {
        clickArea = value.clickArea
    }
    fun textView(
        v: TextView, ctx: UIContext, btn: Button? = null,
    ) {
        rect = v.rect
        scale = null
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
            val text = MTextAreaImpl(
                rect!!, ctx!!,
                scale ?: getDefaultScale(rect!!)
            )
            val btn = MButtonImpl(clickArea!!, ctx!!)
            return MTextButtonImpl(text, btn)
        }
}