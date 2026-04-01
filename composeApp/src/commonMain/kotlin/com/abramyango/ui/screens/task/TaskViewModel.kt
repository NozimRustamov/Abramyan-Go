package com.abramyango.ui.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abramyango.domain.model.*
import com.abramyango.domain.repository.PlayerRepository
import com.abramyango.domain.repository.TaskRepository
import com.abramyango.domain.usecase.SolveTaskUseCase
import com.abramyango.domain.usecase.TaskReward
import com.abramyango.ui.base.MviIntent
import com.abramyango.ui.base.MviSideEffect
import com.abramyango.ui.base.MviState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * State для экрана задачи
 */
data class TaskState(
    val isLoading: Boolean = true,
    val task: Task? = null,
    val selectedLanguage: ProgrammingLanguage = ProgrammingLanguage.PYTHON,
    val currentAttempt: Int = 1,
    val comboState: ComboState = ComboState(),
    val taskPhase: TaskPhase = TaskPhase.SOLVING,
    val userAnswer: UserAnswer? = null,
    val lastReward: TaskReward? = null,
    val error: String? = null
) : MviState

enum class TaskPhase {
    SOLVING,        // Решение задачи
    CHECKING,       // Проверка ответа
    CORRECT,        // Правильный ответ
    INCORRECT,      // Неправильный ответ
    COMPLETED       // Задача завершена
}

sealed class UserAnswer {
    data class DragDropAnswer(val orderedBlocks: List<String>) : UserAnswer()
    data class FillBlanksAnswer(val answers: Map<Int, String>) : UserAnswer()
    data class BugHuntAnswer(val selectedLine: Int) : UserAnswer()
    data class CodeTraceAnswer(val answers: List<String>) : UserAnswer()
    data class OutputPredictionAnswer(val selectedOutput: String) : UserAnswer()
    data class RefactoringAnswer(val selectedOption: String) : UserAnswer()
}

/**
 * Intent для экрана задачи
 */
sealed interface TaskIntent : MviIntent {
    data class LoadTask(val taskId: String) : TaskIntent
    data class SubmitAnswer(val answer: UserAnswer) : TaskIntent
    data object UseHint : TaskIntent
    data object NextTask : TaskIntent
    data object RetryTask : TaskIntent
    data object ExitTask : TaskIntent
}

/**
 * Side Effects
 */
sealed interface TaskSideEffect : MviSideEffect {
    data object NavigateBack : TaskSideEffect
    data class NavigateToNextTask(val taskId: String) : TaskSideEffect
    data class ShowReward(val reward: TaskReward) : TaskSideEffect
    data class PlaySound(val soundType: SoundType) : TaskSideEffect
    data object TriggerHaptic : TaskSideEffect
    data class ShowError(val message: String) : TaskSideEffect
}

enum class SoundType {
    CORRECT, INCORRECT, COMBO, LEVEL_UP
}

/**
 * ViewModel для экрана задачи
 */
class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val playerRepository: PlayerRepository,
    private val solveTaskUseCase: SolveTaskUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(TaskState())
    val state: StateFlow<TaskState> = _state.asStateFlow()
    
    private val _sideEffect = MutableSharedFlow<TaskSideEffect>()
    val sideEffect: SharedFlow<TaskSideEffect> = _sideEffect.asSharedFlow()
    
    private var startTime: Long = 0
    
    fun processIntent(intent: TaskIntent) {
        when (intent) {
            is TaskIntent.LoadTask -> loadTask(intent.taskId)
            is TaskIntent.SubmitAnswer -> submitAnswer(intent.answer)
            is TaskIntent.UseHint -> useHint()
            is TaskIntent.NextTask -> nextTask()
            is TaskIntent.RetryTask -> retryTask()
            is TaskIntent.ExitTask -> exitTask()
        }
    }
    
    private fun loadTask(taskId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                combine(
                    taskRepository.getTask(taskId),
                    playerRepository.getProfile()
                ) { task, profile ->
                    Pair(task, profile)
                }.collect { (task, profile) ->
                    if (task != null) {
                        startTime = System.currentTimeMillis()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                task = task,
                                selectedLanguage = profile.selectedLanguage,
                                taskPhase = TaskPhase.SOLVING,
                                currentAttempt = 1,
                                userAnswer = null,
                                error = null
                            )
                        }
                    } else {
                        _state.update { it.copy(isLoading = false, error = "Task not found") }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
    
    private fun submitAnswer(answer: UserAnswer) {
        viewModelScope.launch {
            val task = _state.value.task ?: return@launch
            
            _state.update { it.copy(taskPhase = TaskPhase.CHECKING, userAnswer = answer) }
            
            val isCorrect = checkAnswer(task, answer)
            val timeSpent = ((System.currentTimeMillis() - startTime) / 1000).toInt()
            
            if (isCorrect) {
                val reward = solveTaskUseCase.execute(
                    taskId = task.id,
                    isCorrect = true,
                    attemptNumber = _state.value.currentAttempt,
                    timeSpentSeconds = timeSpent,
                    comboState = _state.value.comboState
                )
                
                _state.update {
                    it.copy(
                        taskPhase = TaskPhase.CORRECT,
                        comboState = reward.newComboState,
                        lastReward = reward
                    )
                }
                
                _sideEffect.emit(TaskSideEffect.PlaySound(SoundType.CORRECT))
                _sideEffect.emit(TaskSideEffect.TriggerHaptic)
                
                if (reward.newComboState.multiplier >= 3) {
                    _sideEffect.emit(TaskSideEffect.PlaySound(SoundType.COMBO))
                }
            } else {
                _state.update {
                    it.copy(
                        taskPhase = TaskPhase.INCORRECT,
                        currentAttempt = it.currentAttempt + 1,
                        comboState = it.comboState.reset()
                    )
                }
                
                _sideEffect.emit(TaskSideEffect.PlaySound(SoundType.INCORRECT))
            }
        }
    }
    
    private fun checkAnswer(task: Task, answer: UserAnswer): Boolean {
        return when (task.mechanic) {
            TaskMechanic.DRAG_DROP -> {
                // Для drag & drop проверяем порядок блоков
                // TODO: реализовать проверку
                true
            }
            TaskMechanic.FILL_BLANKS -> {
                // Для fill blanks проверяем ответы
                // TODO: реализовать проверку
                true
            }
            TaskMechanic.BUG_HUNT -> {
                // Для bug hunt проверяем выбранную строку
                val bugAnswer = answer as? UserAnswer.BugHuntAnswer ?: return false
                // TODO: добавить данные о правильной строке
                true
            }
            TaskMechanic.CODE_TRACE -> {
                // Для трассировки проверяем значения переменных
                // TODO: реализовать проверку
                true
            }
            TaskMechanic.OUTPUT_PREDICTION -> {
                // Для output prediction проверяем выбранный вывод
                // TODO: реализовать проверку
                true
            }
            TaskMechanic.REFACTORING -> {
                // Для рефакторинга проверяем выбранный вариант
                // TODO: реализовать проверку
                true
            }
        }
    }
    
    private fun useHint() {
        viewModelScope.launch {
            // TODO: использовать способность подсказки
            _sideEffect.emit(TaskSideEffect.ShowError("Подсказки пока не реализованы"))
        }
    }
    
    private fun nextTask() {
        viewModelScope.launch {
            // TODO: получить следующую задачу
            _sideEffect.emit(TaskSideEffect.NavigateBack)
        }
    }
    
    private fun retryTask() {
        viewModelScope.launch {
            startTime = System.currentTimeMillis()
            _state.update {
                it.copy(
                    taskPhase = TaskPhase.SOLVING,
                    userAnswer = null
                )
            }
        }
    }
    
    private fun exitTask() {
        viewModelScope.launch {
            _sideEffect.emit(TaskSideEffect.NavigateBack)
        }
    }
}
