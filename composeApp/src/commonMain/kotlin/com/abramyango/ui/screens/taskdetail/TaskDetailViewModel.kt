package com.abramyango.ui.screens.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abramyango.data.CategoryRepository
import com.abramyango.data.CategoryTask
import com.abramyango.ui.MviIntent
import com.abramyango.ui.MviSideEffect
import com.abramyango.ui.MviState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskDetailState(
    val task: CategoryTask? = null,
    val categoryId: String = "",
    val expandedSolutions: Set<String> = emptySet(),
    val isLoading: Boolean = true,
    val error: String? = null
) : MviState

sealed interface TaskDetailIntent : MviIntent {
    data class LoadTask(val categoryId: String, val taskIndex: Int) : TaskDetailIntent
    data class ToggleSolution(val language: String) : TaskDetailIntent
    data object Back : TaskDetailIntent
}

sealed interface TaskDetailSideEffect : MviSideEffect {
    data object NavigateBack : TaskDetailSideEffect
}

class TaskDetailViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TaskDetailState())
    val state: StateFlow<TaskDetailState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<TaskDetailSideEffect>()
    val sideEffect: SharedFlow<TaskDetailSideEffect> = _sideEffect.asSharedFlow()

    fun processIntent(intent: TaskDetailIntent) {
        when (intent) {
            is TaskDetailIntent.LoadTask -> loadTask(intent.categoryId, intent.taskIndex)
            is TaskDetailIntent.ToggleSolution -> toggleSolution(intent.language)
            is TaskDetailIntent.Back -> back()
        }
    }

    private fun loadTask(categoryId: String, taskIndex: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, categoryId = categoryId) }
            try {
                val tasks = categoryRepository.getTasksForCategory(categoryId)
                _state.update { it.copy(isLoading = false, task = tasks.getOrNull(taskIndex)) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun toggleSolution(language: String) {
        _state.update { state ->
            val expanded = state.expandedSolutions.toMutableSet()
            if (language in expanded) expanded.remove(language) else expanded.add(language)
            state.copy(expandedSolutions = expanded)
        }
    }

    private fun back() {
        viewModelScope.launch { _sideEffect.emit(TaskDetailSideEffect.NavigateBack) }
    }
}
