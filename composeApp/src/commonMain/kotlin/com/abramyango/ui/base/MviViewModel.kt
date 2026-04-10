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
