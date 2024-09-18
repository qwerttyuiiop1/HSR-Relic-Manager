package com.example.hsrrelicmanager.andorid

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.example.hsrrelicmanager.R
import com.example.hsrrelicmanager.core.android.SharedForegroundNotif
import com.google.android.flexbox.FlexboxLayoutManager
import com.torrydo.floatingbubbleview.service.expandable.BubbleBuilder
import com.torrydo.floatingbubbleview.service.expandable.ExpandableBubbleService
import com.torrydo.floatingbubbleview.service.expandable.ExpandedBubbleBuilder

/**
 * the bubble service, by default, the bubble is not shown
 * open class to allow sharing of the foreground notification
 */
class BubbleMenu: ExpandableBubbleService(), LifecycleOwner {
    inner class Binder : android.os.Binder() {
        fun getService() = this@BubbleMenu
    }
    override fun onBind(intent: Intent?): IBinder {
        return Binder()
    }

    //lifecycle part
    override val lifecycle get() = LifecycleRegistry(this)
    init {
        lifecycle.currentState = Lifecycle.State.CREATED
    }
    override fun onCreate() {
        super.onCreate()
        lifecycle.currentState = Lifecycle.State.STARTED
        startForeground(
            SharedForegroundNotif.NOTIFICATION_ID,
            SharedForegroundNotif.build(this, true)
        )
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycle.currentState = Lifecycle.State.RESUMED
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onDestroy() {
        super.onDestroy()
        lifecycle.currentState = Lifecycle.State.DESTROYED
        close()
    }

    private var isClosed = false
    fun close() {
        if (isClosed) return
        isClosed = true

        removeAll()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private lateinit var controller: BubbleController

    private val _bubble: ImageView
    val bubbleView get() = _bubble
    private val closeBubble: View
    private lateinit var bubbbleContent: View
    init {
        val bubblePx = resources.getDimension(R.dimen.bubble_size).toInt()
        val closeSize = resources.getDimension(R.dimen.close_bubble_size).toInt()
        _bubble = ImageView(this).apply {
            setImageResource(R.mipmap.ic_launcher_round)
            layoutParams = FrameLayout.LayoutParams(bubblePx, bubblePx)
            imageAlpha = 255/2
        }
        _bubble.setOnClickListener {
            expand()
        }
        closeBubble = ImageView(this).apply {
            setImageResource(com.torrydo.floatingbubbleview.R.drawable.ic_close_bubble)
            layoutParams = FrameLayout.LayoutParams(closeSize, closeSize)
        }
    }
    fun setController(controller: BubbleController) {
        @SuppressLint("InflateParams")
        bubbbleContent = LayoutInflater.from(this)
            .inflate(R.layout.bubble_activity, null).apply {
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = FlexboxLayoutManager(this@BubbleMenu)
                recyclerView.adapter = TaskAdapter(
                    controller.getTasks(),
                    this@BubbleMenu, controller,
                    this@BubbleMenu
                )
                //TODO: add gap
            }
        minimize()
    }

    override fun configBubble(): BubbleBuilder {
        return BubbleBuilder(this)
            .bubbleView(bubbleView)
            .closeBubbleView(closeBubble)
    }
    override fun configExpandedBubble(): ExpandedBubbleBuilder {
        return ExpandedBubbleBuilder(this)
            .expandedView(bubbbleContent)
            .onDispatchKeyEvent {
                if(it.keyCode == KeyEvent.KEYCODE_BACK)
                    minimize()
                null
            }
            .fillMaxWidth(true)
            .dimAmount(0.8f)
            .draggable(false)
            .enableAnimateToEdge(false)
    }


    val handler = Handler(Looper.getMainLooper())
    val runImageAlpha = Runnable {
        bubbleView.imageAlpha = 255/2
    }
    fun show() {
        bubbleView.imageAlpha = 255
        handler.removeCallbacks(runImageAlpha)
        controller.onVisible()
    }
    fun hide() {
        handler.postDelayed(runImageAlpha, 500)
        controller.onHidden()
    }
}