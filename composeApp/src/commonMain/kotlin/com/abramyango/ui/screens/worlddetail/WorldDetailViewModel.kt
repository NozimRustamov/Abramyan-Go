package com.abramyango.ui.screens.worlddetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abramyango.domain.model.Task
import com.abramyango.domain.repository.TaskRepository
import com.abramyango.ui.base.MviIntent
import com.abramyango.ui.base.MviSideEffect
import com.abramyango.ui.base.MviState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class WorldDetailState(
    val worldId: String = "",
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) : MviState

sealed interface WorldDetailIntent : MviIntent {
    data class LoadTasks(val worldId: String) : WorldDetailIntent
    data class SelectTask(val taskId: String) : WorldDetailIntent
    data object Back : WorldDetailIntent
}

sealed interface WorldDetailSideEffect : MviSideEffect {
    data class NavigateToTask(val worldId: String, val taskId: String) : WorldDetailSideEffect
    data object NavigateBack : WorldDetailSideEffect
}

class WorldDetailViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WorldDetailState())
    val state: StateFlow<WorldDetailState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<WorldDetailSideEffect>()
    val sideEffect: SharedFlow<WorldDetailSideEffect> = _sideEffect.asSharedFlow()

    fun processIntent(intent: WorldDetailIntent) {
        when (intent) {
            is WorldDetailIntent.LoadTasks -> loadTasks(intent.worldId)
            is WorldDetailIntent.SelectTask -> selectTask(intent.taskId)
            is WorldDetailIntent.Back -> back()
        }
    }

    private fun loadTasks(worldId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, worldId = worldId) }
            try {
                taskRepository.getTasksForWorld(worldId).collect { tasks ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            tasks = tasks.sortedBy { t -> t.order }
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun selectTask(taskId: String) {
        viewModelScope.launch {
            _sideEffect.emit(WorldDetailSideEffect.NavigateToTask(_state.value.worldId, taskId))
        }
    }

    private fun back() {
        viewModelScope.launch {
            _sideEffect.emit(WorldDetailSideEffect.NavigateBack)
        }
    }
}
