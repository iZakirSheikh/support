package com.primex.core
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.primex.core.Result.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import androidx.compose.runtime.State as AndroidState

private const val TAG = "Result"

interface Result<T> {

    /* The data of this class*/
    val data: AndroidState<T>

    /*The state of the housed data*/
    val state: AndroidState<State>

    operator fun component2(): T = data.value

    operator fun component1(): State = state.value

    /**
     * The [State] of the [Result] class.
     */
    sealed interface State {
        object Loading : State

        /**
         * @param what pass different values for different states like Searching, Processing etc.
         */
        @JvmInline
        value class Processing(val what: Int = -1) : State

        /**
         * @param what: can be anything like string message or some error code as per requirements of user
         */
        @JvmInline
        value class Error(val throwable: Throwable?) : State

        object Empty : State

        object Success : State
    }
}

class MutableResult<T>(initial: T) : Result<T> {

    override val data: AndroidState<T> = mutableStateOf(initial)

    override val state: AndroidState<State> = mutableStateOf(State.Loading)

    fun emit(value: T) {
        (this.data as MutableState).value = value
        (this.state as MutableState).value = State.Success
    }

    fun emit(value: State) {
        require(value != State.Success) {
            Log.e("Result", "success will be invoked when value is emitted.")
        }
        (this.state as MutableState).value = value
    }
}


inline fun <T> buildResult(initial: T, init: MutableResult<T>.() -> Unit): Result<T> {
    val result = MutableResult(initial)
    init.invoke(result)
    return result
}

fun <T> buildResultOfList(
    initial: List<T>,
    scope: CoroutineScope,
    flow: Flow<List<T>>
): Result<List<T>> {
    return buildResult(initial) {
        flow.onEach {
            emit(it)
            // change state to empty.
            if (it.isEmpty()) {
                emit(Result.State.Empty)
            }
        }
            .catch { emit(Result.State.Error(it)) }
            .launchIn(scope)
    }
}

fun <K, T> buildResultOfMap(
    initial: Map<K, T>,
    scope: CoroutineScope,
    flow: Flow<Map<K, T>>
): Result<Map<K, T>> {
    return buildResult(initial) {
        flow.onEach {
            emit(it)
            // change state to empty.
            if (it.isEmpty()) {
                emit(Result.State.Empty)
            }
        }
            .catch { emit(Result.State.Error(it)) }
            .launchIn(scope)
    }
}