package com.example.hsrrelicmanager.core.exe

import com.example.hsrrelicmanager.core.components.Task

/**
 * This is for passing results between tasks (i think)
 * The result of an instance
 */
sealed interface MyResult<T> {
    open class Success<T>(
        val data: T,
    ) : MyResult<T> {
        override fun toString(): String = "Success($data)"
    }
    open class Fail<T>(
        val error: Any,
    ) : MyResult<T> {
        override fun toString(): String = "Fail($error)"
    }
    fun asTaskResult(task: Task): TaskResult =
        when (this) {
            is Success -> TaskResult.Success(task, data.toString())
            is Fail -> TaskResult.Fail(task, error)
            else -> throw IllegalStateException()
        }
}

/**
 * This is for passing results to the user
 * The overall result of a task
 */
sealed interface TaskResult : MyResult<String?> {
    val task: Task
    class Success(
        override val task: Task,
        message: String? = null,
    ) : MyResult.Success<String?>(message), TaskResult {
        val message get() = data
    }
    class Fail(
        override val task: Task,
        data: Any,
    ) : MyResult.Fail<String?>(data), TaskResult
}