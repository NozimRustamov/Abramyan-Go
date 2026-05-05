package tj.abramyan.go.ui.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import tj.abramyan.go.data.Category
import tj.abramyan.go.data.CategoryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoriesState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

sealed interface CategoriesIntent {
    data object LoadCategories : CategoriesIntent
    data class SelectCategory(val categoryId: String, val categoryName: String) : CategoriesIntent
}

sealed interface CategoriesSideEffect {
    data class NavigateToTaskList(val categoryId: String, val categoryName: String) : CategoriesSideEffect
}

class CategoriesViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<CategoriesSideEffect>()
    val sideEffect: SharedFlow<CategoriesSideEffect> = _sideEffect.asSharedFlow()

    init {
        processIntent(CategoriesIntent.LoadCategories)
    }

    fun processIntent(intent: CategoriesIntent) {
        when (intent) {
            is CategoriesIntent.LoadCategories -> loadCategories()
            is CategoriesIntent.SelectCategory -> selectCategory(intent.categoryId, intent.categoryName)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val categories = categoryRepository.getCategories()
                _state.update { it.copy(isLoading = false, categories = categories) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun selectCategory(categoryId: String, categoryName: String) {
        viewModelScope.launch {
            _sideEffect.emit(CategoriesSideEffect.NavigateToTaskList(categoryId, categoryName))
        }
    }
}
