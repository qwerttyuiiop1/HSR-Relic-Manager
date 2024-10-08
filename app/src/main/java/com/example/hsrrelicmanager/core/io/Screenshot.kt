package com.example.hsrrelicmanager.core.io

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import com.example.hsrrelicmanager.core.AutoclickService
import com.example.hsrrelicmanager.core.components.RecyclableBitmap
import com.example.hsrrelicmanager.core.ext.rgbaToBitmap


interface Preprocess {
    fun applyTo(b: Bitmap)
}
/**
 * class for taking screenshots, automatically handles configuration changes
 */
class Screenshot(
    private val context: Context,
    intent: Intent
): AutoCloseable {
    companion object {
        private const val TAG = "ScreenshotTag"
    }

    private val mediaProjectionManager: MediaProjectionManager
    private val windowManager: WindowManager

    private val projection: MediaProjection
    private var virtualDisplay: VirtualDisplay? = null
    private val configChangeReceiver: BroadcastReceiver
    var imageReader: ImageReader? = null
        private set

    private var bitmap = RecyclableBitmap()
    private var width: Int = 0
    private var height: Int = 0

    var preprocess: Preprocess? = null

    val latestRawImage: Image?
        get() = imageReader!!.acquireLatestImage()
    val latestRawBitmap: Bitmap?
        get() {
            val image = latestRawImage ?: return null
            image.use { it.rgbaToBitmap(bitmap) }
            return bitmap.get()
        }
    val latestBitmap: Bitmap?
        get() {
            val b = latestRawBitmap ?: return null
            preprocess?.applyTo(b)
            return b
        }
    val cached: Bitmap
        get() = bitmap.get()

    private val screenSize: Rect
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.maximumWindowMetrics.bounds
            /**
             *  final WindowMetrics metrics = windowManager.getCurrentWindowMetrics();
            // Gets all excluding insets
            final WindowInsets windowInsets = metrics.getWindowInsets();
            Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars()
            | WindowInsets.Type.displayCutout());

            int insetsWidth = insets.right + insets.left;
            int insetsHeight = insets.top + insets.bottom;

            // Legacy size that Display#getSize reports
            final Rect bounds = metrics.getBounds();
            final Size legacySize = new Size(bounds.width() - insetsWidth,
            bounds.height() - insetsHeight);
            Returns
             */
        } else {
            val metrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(metrics)
            // https://stackoverflow.com/a/15699681
            try {
                val size = Point()
                Display::class.java.getMethod("getRealSize", Point::class.java)
                    .invoke(metrics, size)
                Rect(0, 0, size.x, size.y)
            } catch (e: Exception) {
                Rect(0, 0, metrics.widthPixels, metrics.heightPixels)
            }
        }

    init {
        mediaProjectionManager =
            context.getSystemService(MediaProjectionManager::class.java)
        windowManager =
            context.getSystemService(WindowManager::class.java)

        val resultCode = intent.getIntExtra(AutoclickService.EXTRA_RESULT_CODE, Activity.RESULT_CANCELED)
        val resultData = intent.getParcelableExtra<Intent>(AutoclickService.EXTRA_RESULT_DATA)!!
        projection = mediaProjectionManager.getMediaProjection(resultCode, resultData)

        val intentFilter = IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED)
        configChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Intent.ACTION_CONFIGURATION_CHANGED)
                    configureVirtualDisplay(screenSize)
            }
        }
        context.registerReceiver(configChangeReceiver, intentFilter)

        configureVirtualDisplay(screenSize)
    }

    fun configureVirtualDisplay(screen: Rect) {
        virtualDisplay?.release()
        imageReader?.close()

        width = screen.width()
        height = screen.height()

        // ??? ImageFormat.YUV_420_888
        @SuppressLint("WrongConstant")
        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)

        virtualDisplay = projection.createVirtualDisplay(
            TAG,
            width, height,
            context.resources.displayMetrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader!!.surface,
            null, null
        )
    }

    override fun close() {
        imageReader?.close()
        virtualDisplay?.release()
        //bitmap?.recycle()
        projection.stop()
        context.unregisterReceiver(configChangeReceiver)

        imageReader = null
        virtualDisplay = null
        bitmap.bitmap = null
    }
}