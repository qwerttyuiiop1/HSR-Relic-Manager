package com.example.hsrrelicmanager.core.components.ui

import android.graphics.Bitmap
import android.graphics.Rect
import android.widget.TextView
import com.example.hsrrelicmanager.core.components.MUIElement
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.UIElement
import com.example.hsrrelicmanager.core.components.impl.MTextAreaImpl
import com.example.hsrrelicmanager.core.components.views.TextAreaView
import com.example.hsrrelicmanager.core.ext.rect
import com.example.hsrrelicmanager.core.io.TextRecognizer
import com.google.mlkit.vision.text.Text
import kotlin.math.max
import kotlin.math.min

interface TextArea: UIElement {
    companion object {
        const val DEFAULT_SCALE_FACTOR = 0.5f
        fun getDefaultScale(rect: Rect): Float {
            // 36 is the minimum text size for the recognizer
            return max(
                DEFAULT_SCALE_FACTOR,
                36f / min(rect.width(), rect.height())
            )
        }
    }
    val area: Rect
    val recognizer: TextRecognizer
        get() = ctx.recognizer
    val scale: Float
    /**
     * the label of the button
     * the button is recognized if all of
     * the strings in this set are found in the text
     */
    var label: Set<String>?
    suspend fun getText(tick: Bitmap = ctx.tick): Text
    suspend fun isRecognized(tick: Bitmap = ctx.tick): Boolean
    fun isRecognized(text: Text): Boolean
}
interface MTextArea: TextArea, MUIElement

typealias TxtBldr = TextAreaBuilder
class TextAreaBuilder(
    var ctx: UIContext? = null,
    var rect: Rect? = null,
    var scale: Float? = null,
    var label: Set<String>? = null
) {
    constructor(
        v: TextView, ctx: UIContext, scale: Float? = null
    ): this() {
        textView(v, ctx, scale)
    }
    constructor(
        v: TextAreaView, ctx: UIContext
    ): this() {
        textView(v, ctx, v.scale)
    }
    fun textView(v: TextView, ctx: UIContext, scale: Float? = null) {
        rect = v.rect
        this.ctx = ctx
        this.scale = scale
        label = v.text.let {
            if (it == null || it == "") null
            else it.split(",").toSet()
        }
    }
    fun textAreaView(v: TextAreaView, ctx: UIContext) = textView(v, ctx, v.scale)

    val textArea: TextArea
        get() = mTextArea
    val mTextArea: MTextArea
        get() = MTextAreaImpl(
            rect!!,
            ctx!!,
            scale ?: TextArea.getDefaultScale(rect!!),
            label
        )
}