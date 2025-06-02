package com.trianglz.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trianglz.ui.utils.safeCollect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


abstract class BaseViewModel<
    UiState : ViewState,
    Event : ViewEvent,
    Effect : ViewSideEffect
> : ViewModel() {

    protected abstract fun setInitialState(): UiState

    protected abstract fun handleEvents(event: Event)

    private val initialState: UiState by lazy { setInitialState() }

    private val _viewState: MutableStateFlow<UiState> = MutableStateFlow(initialState)
    val viewState: StateFlow<UiState> = _viewState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()


    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }

    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    protected fun getState() = _viewState.value

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    fun <T> Flow<T>.launchAndCollectResult(
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onStart: () -> Unit = {},
        onComplete: (Throwable?) -> Unit = {},
        skipAfterInitial: Boolean = true
    ): Job? {
        return if (initialState == _viewState && skipAfterInitial) {
            null
        } else {
            viewModelScope.launch {
                this@launchAndCollectResult.flowOn(
                    Dispatchers.IO
                ).onStart {
                    onStart()
                }.onCompletion { cause ->
                    onComplete(cause)
                }.catch { exception ->
                    onError(exception)
                }.safeCollect { result ->
                    onSuccess(result)
                }
            }
        }
    }
}
