package com.example.hsrrelicmanager.andorid

import android.graphics.RenderEffect
import android.graphics.Shader
import android.view.View

fun View.blur(radius: Int = 20) {
    val blurEffect = RenderEffect.createBlurEffect(
        radius.toFloat(),
        radius.toFloat(),
        Shader.TileMode.CLAMP
    )
    setRenderEffect(blurEffect)
}