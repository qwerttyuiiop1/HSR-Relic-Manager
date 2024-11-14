package com.example.hsrrelicmanager.core.exe

import android.graphics.Bitmap
import com.example.hsrrelicmanager.core.io.Screenshot
import com.google.android.datatransport.runtime.logging.Logging
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore

/**
 * For executing tasks
 */
class TaskExecutor(
    private val screenshot: Screenshot,
) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private var job: Job? = null
    var runner: TaskRunner? = null
        set(value) {
            stop()
            field = value
        }
    var onTaskComplete: ((TaskResult) -> Unit)? = null

    fun stop() {
        if (job == null) return
        job?.cancel()
        runner?.stop()
        job = null
    }

    fun start() {
        if (job != null || runner == null) return
        job = scope.launch { startLoop(runner!!) }
    }
    private suspend fun startLoop(runner: TaskRunner) = coroutineScope {
        runner.start(this)
        while (true) {
            runner.awaitResult()?.let { stopSync(it) }
            var tick: Bitmap?
            do {
                delay(runner.tickRate)
                tick = screenshot.latestRawBitmap
            } while (tick == null)
            //Log.d("COROUTINE_TEST", "TaskExecutor: sending tick")
            runner.nextTick(tick)
        }
    }
    private suspend fun stopSync(res: TaskResult): Nothing {
        // needs to run on a separate scope since
        // the current scope is being cancelled
        scope.launch {
            job?.cancelAndJoin()
            stop()
            onTaskComplete?.invoke(res)
        }.join()
        throw CancellationException()
    }

    fun runSingleTick(
        runner: TaskRunner, onComplete: ((TaskResult) -> Unit)? = null
    ) = scope.launch {
        runner.apply {
            start(scope)
            awaitResult()?.let {
                onTaskComplete?.invoke(it)
                onComplete?.invoke(it)
                stop()
                return@launch
            }
            var tick: Bitmap?
            while (true) {
                tick = screenshot.latestRawBitmap
                if (tick != null) break
                delay(tickRate)
            }
            nextTick(tick!!)
            awaitResult()!!.let {
                onTaskComplete?.invoke(it)
                onComplete?.invoke(it)
                stop()
                return@launch
            }
        }
    }

    fun close() {
        scope.launch {
            job?.cancelAndJoin()
            stop()
            scope.cancel()
        }
    }

    private val queue = Semaphore(1)
    fun queue(func: suspend TaskExecutor.() -> Unit) {
        scope.launch {
            try {
                queue.acquire()
                func()
            } finally {
                queue.release()
            }
        }
    }
}