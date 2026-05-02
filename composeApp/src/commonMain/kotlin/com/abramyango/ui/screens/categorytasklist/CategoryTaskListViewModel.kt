package com.abramyango.ui.screens.categorytasklist

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

data class CategoryTaskListState(
    val categoryId: String = "",
    val categoryName: String = "",
    val tasks: List<CategoryTask> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) : MviState

sealed interface CategoryTaskListIntent : MviIntent {
    data class LoadTasks(val categoryId: String, val categoryName: String) : CategoryTaskListIntent
    data class SelectTask(val taskIndex: Int) : CategoryTaskListIntent
    data object Back : CategoryTaskListIntent
}

sealed interface CategoryTaskListSideEffect : MviSideEffect {
    data class NavigateToTaskDetail(val categoryId: String, val taskIndex: Int) : CategoryTaskListSideEffect
    data object NavigateBack : CategoryTaskListSideEffect
}

class CategoryTaskListViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryTaskListState())
    val state: StateFlow<CategoryTaskListState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<CategoryTaskListSideEffect>()
    val sideEffect: SharedFlow<CategoryTaskListSideEffect> = _sideEffect.asSharedFlow()

    fun processIntent(intent: CategoryTaskListIntent) {
        when (intent) {
            is CategoryTaskListIntent.LoadTasks -> loadTasks(intent.categoryId, intent.categoryName)
            is CategoryTaskListIntent.SelectTask -> selectTask(intent.taskIndex)
            is CategoryTaskListIntent.Back -> back()
        }
    }

    private fun loadTasks(categoryId: String, categoryName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, categoryId = categoryId, categoryName = categoryName) }
            try {
                val tasks = categoryRepository.getTasksForCategory(categoryId)
                _state.update { it.copy(isLoading = false, tasks = tasks) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun selectTask(taskIndex: Int) {
        viewModelScope.launch {
            _sideEffect.emit(
                CategoryTaskListSideEffect.NavigateToTaskDetail(_state.value.categoryId, taskIndex)
            )
        }
    }

    private fun back() {
        viewModelScope.launch { _sideEffect.emit(CategoryTaskListSideEffect.NavigateBack) }
    }
}
