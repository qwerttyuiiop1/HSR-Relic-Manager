package com.example.hsrrelicmanager.core.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.view.doOnPreDraw
import androidx.viewbinding.ViewBinding
import com.example.hsrrelicmanager.core.components.UIContext
import kotlinx.coroutines.CompletableDeferred

typealias Promise<T> = CompletableDeferred<T>
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Promise(): Promise<T> = CompletableDeferred()

val View.rect: Rect
    get() {
        val loc = IntArray(2)
        getLocationOnScreen(loc)
        return Rect(loc[0], loc[1], loc[0] + width, loc[1] + height)
    }

fun View.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
}

fun <T: ViewBinding> T.measure(
    ctx: UIContext
): T {
    val metrics = ctx.wmgr.maximumWindowMetrics
    val w = metrics.bounds.width()
    val h = metrics.bounds.height()
    root.measure(
        View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY)
    )
    root.layout(0, 0, w, h)
    return this
}

suspend fun <T, U: ViewBinding> U.doOnMeasure(
    ctx: UIContext,
    onMeasure: (U)->T
): T {
    val res = Promise<T>()
    root.alpha = 0f
    ctx.uiHandler.post {
        // use android's layout file to get
        // the dimensions of the UI elements
        // this method renders the ViewBinding
        // to get the dimensions of the UI elements
        root.doOnPreDraw {
            res.complete(onMeasure(this))
            ctx.wmgr.removeView(root)
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            ,
            PixelFormat.TRANSPARENT
        ).apply {
            gravity = Gravity.FILL
            layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        ctx.wmgr.addView(root, params)
    }
    return res.await()
}