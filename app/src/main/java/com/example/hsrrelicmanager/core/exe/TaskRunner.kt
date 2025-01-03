package com.example.hsrrelicmanager.core.exe

import android.graphics.Bitmap
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext
import kotlinx.coroutines.CoroutineScope

/**
 * an instance cannot be restarted; once closed, it is stopped forever
 * since users can freely interrupt / pause the task,
 * and it might be a headache to keep re-assessing the game state
 */
interface TaskInstance<T> {
    companion object {}
    /**
     * send the next tick to the task (blocking until the tick is received)
     * can only be called if the task is running
     */
    suspend fun nextTick(tick: Bitmap)

    /**
     * await the result of the task (blocking until the result is sent)
     * can only be called if the task is running
     * @return null if the task is still running and requires a tick
     * @return the result of the task if it is complete
     */
    suspend fun awaitResult(): MyResult<T>?

    /**
     * start the task
     * @param scope the scope to run the task in
     */
    fun start(scope: CoroutineScope)
    /**
     * close the task and free resources
     */
    fun close()
}
/**
 * The execution flow of a task runner is:
 * 1. The task starts or resumes by calling [start].
 * 2. [awaitResult] is called:
 *    - If the result is null, the next tick is sent through [nextTick],
 *    and the process repeats.
 *    - Else, if it is not null, the task is stopped by calling [stop].
 *
 * The task runner can be stopped at any time, even when not yet finished.
 * When stopped, calling [start] would restart the task.
 */
interface TaskRunner: TaskInstance<String?> {
    companion object {
        const val DEFAULT_TICK_RATE = 500L
    }
    /**
     * The name of the task
     */
    val task: Task

    /**
     * How long to wait between ticks
     */
    val tickRate: Long
        get() = DEFAULT_TICK_RATE

    override suspend fun awaitResult(): TaskResult?
    /**
     * stop the task
     * when the task is paused, and no processing should be done
     *
     * ex: when the user is interacting with the overlay
     *
     * so that the focus of the runner is on the task itself;
     * the runner can assume that there are no sudden changes
     * in the state of the game while it is not stopped
     */
    fun stop()

    /**
     * setup / reset; called when the task is started
     */
    suspend fun initialize(uiCtx: UIContext): TaskRunner
}