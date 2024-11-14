package com.example.hsrrelicmanager.ui.bubble

import android.content.Intent
import android.content.res.Configuration
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.android.SharedForegroundNotif
import com.example.hsrrelicmanager.core.io.IBubbleController
import com.torrydo.floatingbubbleview.FloatingBubbleListener
import com.torrydo.floatingbubbleview.service.expandable.BubbleBuilder
import com.torrydo.floatingbubbleview.service.expandable.ExpandableBubbleService
import com.torrydo.floatingbubbleview.service.expandable.ExpandedBubbleBuilder

class HSRBubbleService: ExpandableBubbleService() {
    inner class Binder : android.os.Binder() {
        fun getService() = this@HSRBubbleService
    }
    override fun onBind(intent: Intent?): IBinder {
        return Binder()
    }

    override fun onDestroy() {
        super.onDestroy()
        close()
    }

    private var controller: IBubbleController? = null
    private val bubbleSizePx by lazy {
        resources.getDimensionPixelSize(R.dimen.bubble_size)
    }
    val bubbleView by lazy {
        ImageView(this).apply {
            setImageResource(R.drawable.ic_app)
            setOnClickListener {
                showBubble()
            }
            imageAlpha = 255 / 2
            layoutParams = FrameLayout.LayoutParams(bubbleSizePx, bubbleSizePx)
        }
    }

    val handler = Handler(Looper.getMainLooper())
    val runImageAlpha = Runnable {
        bubbleView.imageAlpha = 255/2
    }

    private var isVisible = false
    fun showBubble() {
        if (!isVisible) {
            isVisible = true
            bubbleView.imageAlpha = 255
            handler.removeCallbacks(runImageAlpha)
            controller!!.setVisibility(true)
        }

        // library specific hack >:(
        expandedBubble!!.layoutParams.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        expandedBubble!!.rootGroup.apply {
            setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        view.performClick()
                        true
                    }
                    else -> false
                }
            }
            setOnClickListener {
                hideBubble()
            }
        }
        expand()
    }
    fun hideBubble() {
        isVisible = false
        handler.postDelayed(runImageAlpha, 1000)
        controller!!.setVisibility(false)
        minimize()
    }


    override fun configBubble(): BubbleBuilder {
        val closeBubbleView =
            ImageView(this).apply {
                setImageResource(R.drawable.ic_bubble_close)
                layoutParams = FrameLayout.LayoutParams(bubbleSizePx, bubbleSizePx)
            }
        bubbleView.parent?.let {
            (it as ViewGroup).removeView(bubbleView)
        }
        return BubbleBuilder(this)
            .bubbleView(bubbleView)
            .bubbleStyle(null)
            .startLocationPx(100, 100)
            .enableAnimateToEdge(true)
            .closeBubbleView(closeBubbleView)
            .distanceToClose(100)

            .addFloatingBubbleListener(object : FloatingBubbleListener {
                override fun onFingerUp(x: Float, y: Float) {
                    handler.postDelayed(runImageAlpha, 1000)
                }
                override fun onFingerDown(x: Float, y: Float) {
                    bubbleView.imageAlpha = 255
                }
            })
    }

    var expandedView: BubbleMenu? = null
    override fun configExpandedBubble(): ExpandedBubbleBuilder {
        expandedView?.onDestroy()
        expandedView = BubbleMenu(this)
        if (controller != null)
            expandedView!!.onCreate(controller!!)
        return expandedView!!.builder()
            .onDispatchKeyEvent {
                if (it.keyCode == KeyEvent.KEYCODE_BACK) {
                    hideBubble()
                }
                null
            }
    }

    private var isClosed = false
    fun close() {
        if (isClosed) return
        isClosed = true
        removeAll()
        expandedView?.onDestroy()
        expandedView = null
    }

    fun setBubbleController(controller: IBubbleController) {
        this.controller = controller
        showBubble()
        this.expandedView?.onCreate(controller)
    }

    override fun startNotificationForeground() {
        startForeground(
            SharedForegroundNotif.NOTIFICATION_ID,
            SharedForegroundNotif.build(this, true)
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (isVisible) {
            showBubble()
        }
    }
}