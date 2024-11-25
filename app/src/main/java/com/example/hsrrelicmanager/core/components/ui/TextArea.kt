package com.example.hsrrelicmanager.core.components.ui

import android.graphics.Bitmap
import android.graphics.Rect
import com.example.hsrrelicmanager.core.components.MUIElement
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.UIElement
import com.example.hsrrelicmanager.core.components.impl.MTextButtonImpl
import com.example.hsrrelicmanager.core.components.ui.TextArea.Companion.getDefaultScale
import com.example.hsrrelicmanager.core.io.TextRecognizer
import com.google.mlkit.vision.text.Text
import kotlin.math.max
import kotlin.math.min

interface TextArea: UIElement {
    companion object {
        const val DEFAULT_SCALE_FACTOR = 0.75f
        fun getDefaultScale(rect: Rect): Float {
            // 36 is the minimum text size for the recognizer
            return max(
                DEFAULT_SCALE_FACTOR,
                36f / min(rect.width(), rect.height())
            )
        }

        operator fun invoke(
            area: Rect,
            ctx: UIContext,
            label: Set<String>? = null,
            scale: Float = getDefaultScale(area)
        ): TextArea = MTextButtonImpl(
            area, scale, label, area, ctx
        )
    }
    val area: Rect
    override val rect get() = area

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
interface MTextArea: TextArea, MUIElement {
    companion object {
        operator fun invoke(
            area: Rect,
            ctx: UIContext,
            label: Set<String>? = null,
            scale: Float = getDefaultScale(area)
        ): MTextArea = MTextButtonImpl(
            area, scale, label, area, ctx
        )
    }
}