package com.example.hsrrelicmanager.ui

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Parcelable
import android.view.View
import kotlinx.serialization.json.Json

fun View.blur(radius: Int = 20) {
    val blurEffect = RenderEffect.createBlurEffect(
        radius.toFloat(),
        radius.toFloat(),
        Shader.TileMode.CLAMP
    )
    setRenderEffect(blurEffect)
}