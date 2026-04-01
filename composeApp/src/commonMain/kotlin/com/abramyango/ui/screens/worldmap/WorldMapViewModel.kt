package com.abramyango.ui.screens.worldmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abramyango.domain.model.*
import com.abramyango.domain.repository.PlayerRepository
import com.abramyango.domain.repository.WorldRepository
import com.abramyango.ui.base.MviIntent
import com.abramyango.ui.base.MviSideEffect
import com.abramyango.ui.base.MviState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * State для экрана карты миров
 */
data class WorldMapState(
    val isLoading: Boolean = true,
    val worlds: List<WorldWithProgress> = emptyList(),
    val playerProfile: PlayerProfile = PlayerProfile(),
    val error: String? = null
) : MviState

data class WorldWithProgress(
    val world: World,
    val progress: WorldProgress
)

/**
 * Intent для экрана карты миров
 */
sealed interface WorldMapIntent : MviIntent {
    data object LoadData : WorldMapIntent
    data class SelectWorld(val worldId: String) : WorldMapIntent
    data object OpenProfile : WorldMapIntent
    data object OpenSettings : WorldMapIntent
}

/**
 * Side Effects для навигации и событий
 */
sealed interface WorldMapSideEffect : MviSideEffect {
    data class NavigateToWorld(val worldId: String) : WorldMapSideEffect
    data object NavigateToProfile : WorldMapSideEffect
    data object NavigateToSettings : WorldMapSideEffect
    data class ShowError(val message: String) : WorldMapSideEffect
}

/**
 * ViewModel для экрана карты миров
 */
class WorldMapViewModel(
    private val worldRepository: WorldRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(WorldMapState())
    val state: StateFlow<WorldMapState> = _state.asStateFlow()
    
    private val _sideEffect = MutableSharedFlow<WorldMapSideEffect>()
    val sideEffect: SharedFlow<WorldMapSideEffect> = _sideEffect.asSharedFlow()
    
    init {
        processIntent(WorldMapIntent.LoadData)
    }
    
    fun processIntent(intent: WorldMapIntent) {
        when (intent) {
            is WorldMapIntent.LoadData -> loadData()
            is WorldMapIntent.SelectWorld -> selectWorld(intent.worldId)
            is WorldMapIntent.OpenProfile -> openProfile()
            is WorldMapIntent.OpenSettings -> openSettings()
        }
    }
    
    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                // Загружаем миры и прогресс
                combine(
                    worldRepository.getAllWorlds(),
                    worldRepository.getAllWorldProgress(),
                    playerRepository.getProfile()
                ) { worlds, progressList, profile ->
                    Triple(worlds, progressList, profile)
                }.collect { (worlds, progressList, profile) ->
                    val worldsWithProgress = worlds.map { world ->
                        val progress = progressList.find { it.worldId == world.id }
                            ?: WorldProgress(
                                worldId = world.id,
                                completedTasks = 0,
                                totalTasks = world.totalTasks,
                                isUnlocked = world.order == 1,
                                isBossDefeated = false
                            )
                        WorldWithProgress(world, progress)
                    }.sortedBy { it.world.order }
                    
                    _state.update {
                        it.copy(
                            isLoading = false,
                            worlds = worldsWithProgress,
                            playerProfile = profile,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }
    
    private fun selectWorld(worldId: String) {
        viewModelScope.launch {
            val world = _state.value.worlds.find { it.world.id == worldId }
            
            if (world?.progress?.isUnlocked == true) {
                _sideEffect.emit(WorldMapSideEffect.NavigateToWorld(worldId))
            } else {
                _sideEffect.emit(WorldMapSideEffect.ShowError("Мир ещё не открыт"))
            }
        }
    }
    
    private fun openProfile() {
        viewModelScope.launch {
            _sideEffect.emit(WorldMapSideEffect.NavigateToProfile)
        }
    }
    
    private fun openSettings() {
        viewModelScope.launch {
            _sideEffect.emit(WorldMapSideEffect.NavigateToSettings)
        }
    }
}
