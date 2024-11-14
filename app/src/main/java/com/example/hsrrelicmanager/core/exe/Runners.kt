package com.example.hsrrelicmanager.core.exe

import android.graphics.Bitmap
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext
import kotlinx.coroutines.CoroutineScope

/**
 * For executing short, simple tasks
 */
abstract class ResetRunner: TaskRunner {
    private var currTask: TaskInstance<*>? = null
    abstract fun newInstance(): TaskInstance<*>

    override suspend fun nextTick(tick: Bitmap) =
        currTask!!.nextTick(tick)
    override suspend fun awaitResult(): TaskResult? =
        currTask!!.awaitResult()?.let {
            stop()
            it.asTaskResult(task)
        }

    protected lateinit var uiCtx: UIContext
    override suspend fun initialize(uiCtx: UIContext) =
        this.apply {
            this.uiCtx = uiCtx
        }

    override fun start(scope: CoroutineScope){
        if (currTask != null) return
        currTask = newInstance().also { it.start(scope) }
    }
    override fun stop() {
        currTask?.close()
        currTask = null
    }
    override fun close() = stop()
}

fun TaskRunner.Companion.resetRunner(
    task: Task,
    factory: (uiCtx: UIContext) -> TaskInstance<*>,
) = object: ResetRunner() {
    override fun newInstance() = factory(uiCtx)
    override val task = task
}

/**
 * Default [TaskRunner] implementation
 */
operator fun TaskRunner.Companion.invoke(
    task: Task,
    run: suspend TaskScope<String>.(uiCtx: UIContext) -> MyResult<String>
) = object: ResetRunner() {
    override fun newInstance() = TaskInstance{
        run(uiCtx)
    }
    override val task = task
}

/**
 * If you somehow want to handle everything within a single instance
 *
 * ex: a task with multiple / continuous states
 *
 * recommend to use [ResetRunner] and coordinate state in the runner instead
 */
abstract class ResumeRunner: TaskRunner {
    private var currTask: TaskInstance<*>? = null
    abstract fun newInstance(): TaskInstance<*>
    private var isRunning = false

    override suspend fun nextTick(tick: Bitmap) {
        if (!isRunning)
            throw IllegalStateException("task is not running")
        currTask!!.nextTick(tick)
    }
    override suspend fun awaitResult(): TaskResult? {
        if (!isRunning)
            throw IllegalStateException("task is not running")
        return currTask!!.awaitResult()?.let {
            close()
            it.asTaskResult(task)
        }
    }

    protected lateinit var uiCtx: UIContext
    override suspend fun initialize(uiCtx: UIContext) =
        this.apply {
            this.uiCtx = uiCtx
        }

    override fun start(scope: CoroutineScope){
        isRunning = true
        if (currTask == null)
            currTask = newInstance().apply { start(scope) }
    }
    override fun stop() {
        isRunning = false
    }
    override fun close() {
        stop()
        currTask?.close()
        currTask = null
    }
}
fun TaskRunner.Companion.resumeRunner(
    task: Task,
    factory: (uiCtx: UIContext) -> TaskInstance<*>,
) = object: ResumeRunner() {
    override fun newInstance() = factory(uiCtx)
    override val task = task
}