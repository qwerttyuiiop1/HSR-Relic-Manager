@file:Suppress("unused")

package com.example.hsrrelicmanager.core.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.media.Image
import com.example.hsrrelicmanager.core.components.RecyclableBitmap

fun Bitmap.cropped(rect: Rect, recycle: Boolean = false): Bitmap {
    return Bitmap.createBitmap(
        this,
        rect.left, rect.top,
        rect.width(), rect.height()
    ).also {
        if (recycle) recycle()
    }
}
fun Bitmap.cropped(rect: Rect, dst: RecyclableBitmap): Bitmap {
    dst.setSize(rect.width(), rect.height())
    val canvas = Canvas(dst.get())
    canvas.drawBitmap(
        this, rect,
        Rect(0, 0, rect.width(), rect.height()), null
    )
    return dst.get()
}

fun Bitmap.scaled(
    scale: Float,
    recycle: Boolean = false,
    filter: Boolean = false
): Bitmap {
    return Bitmap.createScaledBitmap(
        this,
        (width * scale).toInt(),
        (height * scale).toInt(),
        filter
    ).also {
        if (recycle) recycle()
    }
}
fun Bitmap.scaled(
    scale: Float,
    dst: RecyclableBitmap,
    filter: Boolean = false
): Bitmap {
    val w = (width * scale).toInt()
    val h = (height * scale).toInt()
    dst.setSize(w, h)
    val canvas = Canvas(dst.get())
    val paint = Paint().apply {
        isFilterBitmap = filter
    }
    canvas.drawBitmap(this, null,
        Rect(0, 0, w, h), paint)
    return dst.get()
}


fun Image.rgbaToBitmap(): Bitmap {
    if (format != PixelFormat.RGBA_8888)
        throw UnsupportedOperationException("Unsupported image format: $format")
    val bitmapWidth = planes[0].rowStride / planes[0].pixelStride
    val bitmap = Bitmap.createBitmap(bitmapWidth, height, Bitmap.Config.ARGB_8888)
    bitmap.copyPixelsFromBuffer(planes[0].buffer)
    return bitmap
}
fun Image.rgbaToBitmap(bitmap: RecyclableBitmap): Bitmap {
    if (format != PixelFormat.RGBA_8888)
        throw UnsupportedOperationException("Unsupported image format: $format")
    val bitmapWidth = planes[0].rowStride / planes[0].pixelStride
    bitmap.setSize(bitmapWidth, height)
    bitmap.get().copyPixelsFromBuffer(planes[0].buffer)
    return bitmap.get()
}