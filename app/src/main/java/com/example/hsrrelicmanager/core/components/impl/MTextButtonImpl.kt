package com.example.hsrrelicmanager.core.components.impl

import android.graphics.Bitmap
import android.graphics.Rect
import com.example.hsrrelicmanager.core.components.RecyclableBitmap
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.ui.MTextButton
import com.example.hsrrelicmanager.core.components.ui.TextArea
import com.example.hsrrelicmanager.core.ext.cropped
import com.example.hsrrelicmanager.core.ext.norm
import com.example.hsrrelicmanager.core.ext.scaled
import com.example.hsrrelicmanager.core.ext.toWords
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.yield

internal open class MTextButtonImpl(
    // text
    final override val area: Rect,
    override val scale: Float = TextArea.getDefaultScale(area),
    label: Set<String>? = null,
    // button
    clickArea: Rect,
    ctx: UIContext,
): MButtonImpl(
    clickArea, ctx
), MTextButton {
    // button
    override suspend fun click() {
        yield() // check for cancellation before clicking
        // TODO: check if clickArea is valid based on tick
        clicker.click(clickArea)
    }
    override fun setPos(x: Int, y: Int) {
        super.setPos(x, y)
        area.offsetTo(x, y)
    }

    protected val cropped = RecyclableBitmap(area.width(), area.height())
    protected val scaled = RecyclableBitmap(area.width(), area.height())

    protected fun getCropped(bitmap: Bitmap): Bitmap {
        return bitmap.cropped(area, cropped)
    }
    override suspend fun getText(tick: Bitmap): Text {
        getCropped(tick).scaled(
            scale, scaled, filter = false
        )
        return recognizer.getText(scaled)
    }

    override var label: Set<String>? = label
        set(value) {
            if (value == null) throw IllegalArgumentException("Text cannot be null")
            if (field != null) throw IllegalStateException("Text already set")
            field = value.mapTo(HashSet()) { it.norm }
        }
    override suspend fun isRecognized(tick: Bitmap): Boolean {
        if (area.right > tick.width ||
            area.bottom > tick.height)
            return false
        return isRecognized(getText(tick))
    }
    override fun isRecognized(text: Text): Boolean {
        val words = text.toWords()
        return (label!! - words).isEmpty()
    }
    override fun isRecognized(words: Set<String>): Boolean {
        return (label!! - words).isEmpty()
    }
}