package com.example.hsrrelicmanager.core.exe

import com.google.android.datatransport.runtime.logging.Logging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//abstract class Instance<T>: TaskScope<T>() {
//    protected abstract suspend fun run(): MyResult<T>
//    override fun start(scope: CoroutineScope) {
//        super.start(scope)
//        launch {
//            try {
//                val res = run()
//                exit(res)
//            } finally {
//                onClose()
//                close()
//            }
//        }
//    }
//    protected suspend fun exit(msg: MyResult<T>): Nothing = complete(msg)
//    protected open suspend fun onClose() {}
//}

abstract class Instance<T> private constructor(
    protected val scope: TaskScope<T>,
): TaskInstance<T> by scope {
    constructor(): this(TaskScope())
    protected abstract suspend fun run(): MyResult<T>
    override fun start(scope: CoroutineScope) {
        this.scope.start(scope)
        this.scope.launch {
            try {
                val res = run()
                exit(res)
            } catch (e: Throwable) {
                Logging.e("Instance", "Error in instance", e)
            } finally {
                onClose()
                close()
            }
        }
    }
    protected val tick get() = scope.tick
    protected suspend fun <U> join(r: TaskInstance<U>) = scope.join(r)
    protected suspend fun awaitTick() = scope.awaitTick()
    protected suspend fun exit(msg: MyResult<T>): Nothing = scope.complete(msg)
    protected open suspend fun onClose() {}
}