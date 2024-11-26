package com.example.hsrrelicmanager.core.exe

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

typealias ResList<T> = List<MyResult.Success<out T>>
fun <T> ResList<T>.flatten() = map { it.data }
/**
 * wrapper Instance running multiple instances
 * with synchronized ticks in a single scope
 */
open class MultiInstance<T>(
    val tasks: List<TaskInstance<out T>>
): Instance<ResList<T>>() {
    override suspend fun run(): MyResult<ResList<T>> {
        tasks.forEach { it.start(scope) }
        val results = MutableList<MyResult<out T>?>(tasks.size) { null }
        var tasks = tasks.mapIndexed { i, it ->
            i to it
        }
        while (true) {
            awaitTick()
            val list = tasks.map { (_, task) ->
                scope.async {
                    val res = task.awaitResult()
                    res ?: task.nextTick(tick)
                    res
                }
            }
            tasks = tasks.filterIndexed { listI, (resI, _) ->
                val res = list[listI].await()
                if (res != null) {
                    results[resI] = res
                    if (res !is MyResult.Success)
                        return MyResult.Fail(results)
                    false
                } else {
                    true
                }
            }
            if (tasks.isEmpty())
                @Suppress("UNCHECKED_CAST")
                return MyResult.Success(results as ResList<T>)
        }
    }
    override fun close() {
        tasks.forEach { it.close() }
        super.close()
    }
}
/**
 * wrapper Instance running multiple instances
 * with synchronized ticks in a single scope
 */
fun <T> TaskInstance.Companion.multi(
    tasks: List<TaskInstance<out T>>
) = MultiInstance(tasks)
fun <T> TaskInstance.Companion.multi(
    vararg tasks: TaskInstance<out T>
) = multi(tasks.toList())

/**
 * wrapper Instance running multiple instances one after another
 */
open class ChainedInstance<T>(
    val tasks: List<TaskInstance<out T>>,
): Instance<ResList<T>>() {
    override suspend fun run(): MyResult<ResList<T>> {
        awaitTick()
        val results = MutableList<MyResult<out T>?>(tasks.size) { null }
        tasks.forEachIndexed { i, it ->
            val res = scope.tryJoin(it)
            results[i] = res
            if (res !is MyResult.Success)
                return MyResult.Fail(results)
        }
        @Suppress("UNCHECKED_CAST")
        return MyResult.Success(results as ResList<T>)
    }
}
/**
 * wrapper Instance running multiple instances one after another
 */
fun <T> TaskInstance.Companion.chained(
    tasks: List<TaskInstance<out T>>
) = ChainedInstance(tasks)
fun <T> TaskInstance.Companion.chained(
    vararg tasks: TaskInstance<out T>
) = chained(tasks.toList())

/**
 * For tasks that only need a single tick
 */
fun <T> TaskInstance.Companion.run(
    a: suspend (Bitmap) -> MyResult<T>
) = object: TaskInstance<T> {
    private var scope: CoroutineScope? = null
    private var res: Deferred<MyResult<T>>? = null
    override suspend fun nextTick(tick: Bitmap) {
        res = scope!!.async { a(tick) }
    }
    override suspend fun awaitResult() = res?.let {
        close()
        it.await()
    }
    override fun start(scope: CoroutineScope) {
        this.scope = scope
    }
    override fun close() {
        scope = null
        res = null
    }
}

/**
 * For tasks that need multiple ticks
 */
operator fun <T> TaskInstance.Companion.invoke(
    a: suspend TaskScope<T>.() -> MyResult<T>
) = object: TaskScope<T>() {
    override fun start(scope: CoroutineScope) {
        super.start(scope)
        launch {
            complete(run(a))
        }
    }
} as TaskInstance<T>

fun <T> TaskInstance.Companion.default(
    a: suspend TaskScope<T>.() -> MyResult<T>
) = invoke(a)

fun <T> TaskInstance.Companion.instant(
    a: suspend () -> MyResult<T>
) = object: TaskInstance<T> {
    override suspend fun nextTick(tick: Bitmap) {
        throw IllegalStateException("This should never be called")
    }
    override suspend fun awaitResult() = a()
    override fun start(scope: CoroutineScope) {}
    override fun close() {}
}