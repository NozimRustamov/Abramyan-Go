package com.abramyango.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Базовый контракт MVI
 */
interface MviState
interface MviIntent
interface MviSideEffect

/**
 * Базовая ViewModel с MVI паттерном
 */
abstract class MviViewModel<S : MviState, I : MviIntent, E : MviSideEffect>(
    initialState: S
) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()
    
    private val _sideEffect = Channel<E>(Channel.BUFFERED)
    val sideEffect: Flow<E> = _sideEffect.receiveAsFlow()
    
    protected val currentState: S
        get() = _state.value
    
    /**
     * Обработка Intent от UI
     */
    abstract fun processIntent(intent: I)
    
    /**
     * Обновление состояния
     */
    protected fun updateState(reducer: S.() -> S) {
        _state.update { it.reducer() }
    }
    
    /**
     * Отправка side effect
     */
    protected suspend fun sendSideEffect(effect: E) {
        _sideEffect.send(effect)
    }
    
    /**
     * Отправка side effect из не-suspend контекста
     */
    protected fun CoroutineScope.emitSideEffect(effect: E) {
        launch { sendSideEffect(effect) }
    }
}
