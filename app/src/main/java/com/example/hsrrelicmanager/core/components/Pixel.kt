@file:Suppress("unused")
package com.example.hsrrelicmanager.core.components

// kotlin alias magic :))

typealias Pixel = Int
typealias PixVal = Int

val Pixel.a: PixVal
    inline get() = this ushr 24
val Pixel.r: PixVal
    inline get() = (this ushr 16) and 0xff
val Pixel.g: PixVal
    inline get() = (this ushr 8) and 0xff
val Pixel.b: PixVal
    inline get() = this and 0xff
val PixVal.clamped: PixVal
    inline get() = coerceIn(0, 255)

fun Pixel.rgb(): String {
    return "#${Integer.toHexString(this and 0x00ffffff).padStart(6, '0')}"
}
fun Pixel.rgba(): String {
    return rgb() + Integer.toHexString(this ushr 24).padStart(2, '0')
}

/**
 * @return the square of the euclidean distance between two pixels
 */
fun Pixel.pixDiff(p: Pixel): Int {
    val r = this.r - p.r
    val g = this.g - p.g
    val b = this.b - p.b
    return r * r + g * g + b * b
}

/**
 * threshold represents how far each color channel on average can be off
 * using the euclidean distance
 */
fun Pixel.similarTo(p: Pixel, threshold: Int) =
    this.pixDiff(p) <= threshold * threshold * 3