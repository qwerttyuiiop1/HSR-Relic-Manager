package com.example.hsrrelicmanager.core.components

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.view.WindowManager
import com.example.hsrrelicmanager.core.io.Clicker
import com.example.hsrrelicmanager.core.io.Screenshot
import com.example.hsrrelicmanager.core.io.TextRecognizer

data class UIContext(
    val ctx: Context,
    val clicker: Clicker,
    val recognizer: TextRecognizer,
    val uiHandler: Handler,
    val screenshot: Screenshot,
    val wmgr: WindowManager = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager,
) {
    val tick: Bitmap
        get() = screenshot.cached
}