package com.abramyango.ui.screens.worldmap

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.World
import com.abramyango.domain.model.WorldProgress
import com.abramyango.ui.components.*
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing
import com.abramyango.ui.theme.WorldColors

/**
 * Экран карты миров - главный экран приложения
 */
@Composable
fun WorldMapScreen(
    state: WorldMapState,
    onIntent: (WorldMapIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.backgroundGradientStart,
                        colors.backgroundGradientEnd
                    )
                )
            )
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Bar с профилем
                WorldMapTopBar(
                    profile = state.playerProfile,
                    onProfileClick = { onIntent(WorldMapIntent.OpenProfile) },
                    onSettingsClick = { onIntent(WorldMapIntent.OpenSettings) }
                )
                
                // Карта миров
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.default),
                    contentPadding = PaddingValues(
                        top = Spacing.medium,
                        bottom = Spacing.massive
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.default),
                    state = rememberLazyListState()
                ) {
                    items(
                        items = state.worlds,
                        key = { it.world.id }
                    ) { worldWithProgress ->
                        WorldCard(
                            world = worldWithProgress.world,
                            progress = worldWithProgress.progress,
                            onClick = { onIntent(WorldMapIntent.SelectWorld(worldWithProgress.world.id)) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Top Bar с информацией о игроке
 */
@Composable
private fun WorldMapTopBar(
    profile: com.abramyango.domain.model.PlayerProfile,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.default),
        glassAlpha = 0.12f,
        cornerRadius = 20.dp,
        contentPadding = Spacing.medium
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Профиль
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onProfileClick() }
            ) {
                Text(
                    text = profile.rank.titleKey.replace("rank_", "").replaceFirstChar { it.uppercaseChar() },
                    style = typography.titleMedium,
                    color = colors.textPrimary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                XpProgressBar(
                    progress = profile.rankProgress,
                    currentXp = profile.totalXp,
                    targetXp = profile.nextRank?.minXp ?: profile.totalXp
                )
            }
            
            Spacer(modifier = Modifier.width(Spacing.medium))
            
            // Стрик
            if (profile.currentStreak > 0) {
                StreakCounter(
                    days = profile.currentStreak,
                    modifier = Modifier.padding(end = Spacing.small)
                )
            }
            
            // Энергия
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "⚡ ${profile.currentEnergy}",
                    style = typography.labelMedium,
                    color = colors.textPrimary
                )
                
                Text(
                    text = "🪙 ${profile.coins}",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
            }
        }
    }
}

/**
 * Карточка мира
 */
@Composable
private fun WorldCard(
    world: World,
    progress: WorldProgress,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    val worldColor = getWorldColor(world.id)
    val isLocked = !progress.isUnlocked
    
    GlassCardAccent(
        accentColor = if (isLocked) Color.Gray else worldColor,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isLocked) 0.6f else 1f)
            .clickable(enabled = !isLocked) { onClick() },
        glassAlpha = 0.15f,
        cornerRadius = 20.dp,
        contentPadding = Spacing.default
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Progress indicator
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                WorldProgressIndicator(
                    progress = progress.progressPercent,
                    accentColor = worldColor,
                    size = 56
                )
                
                // World number or lock
                if (isLocked) {
                    Text(
                        text = "🔒",
                        style = typography.titleLarge
                    )
                } else {
                    Text(
                        text = "${world.order}",
                        style = typography.headlineMedium,
                        color = worldColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(Spacing.default))
            
            // World info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = getWorldDisplayName(world.id),
                    style = typography.titleMedium,
                    color = colors.textPrimary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = getWorldDescription(world.id),
                    style = typography.bodyMedium,
                    color = colors.textSecondary,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Progress text
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${progress.completedTasks}/${progress.totalTasks}",
                        style = typography.labelMedium,
                        color = worldColor
                    )
                    
                    if (progress.isBossDefeated) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "⭐",
                            style = typography.labelMedium
                        )
                    }
                }
            }
            
            // Arrow
            if (!isLocked) {
                Text(
                    text = "→",
                    style = typography.headlineMedium,
                    color = colors.textTertiary
                )
            }
        }
    }
}

/**
 * Получить цвет мира по ID
 */
private fun getWorldColor(worldId: String): Color {
    return when (worldId) {
        "valley_of_beginnings" -> WorldColors.ValleyOfBeginnings
        "fields_of_truth" -> WorldColors.FieldsOfTruth
        "crossroads_of_fate" -> WorldColors.CrossroadsOfFate
        "time_loop" -> WorldColors.TimeLoop
        "data_river" -> WorldColors.DataRiver
        "ocean_of_arrays" -> WorldColors.OceanOfArrays
        "matrix_citadel" -> WorldColors.MatrixCitadel
        "text_forest" -> WorldColors.TextForest
        "architect_archives" -> WorldColors.ArchitectArchives
        else -> Color.Gray
    }
}

/**
 * Получить название мира (в будущем из ресурсов)
 */
private fun getWorldDisplayName(worldId: String): String {
    return when (worldId) {
        "valley_of_beginnings" -> "Долина Начинаний"
        "fields_of_truth" -> "Поля Истины"
        "crossroads_of_fate" -> "Развилка Судьбы"
        "time_loop" -> "Петля Времени"
        "data_river" -> "Река Данных"
        "ocean_of_arrays" -> "Океан Массивов"
        "matrix_citadel" -> "Цитадель Матриц"
        "text_forest" -> "Текстовый Лес"
        "architect_archives" -> "Архивы Архитектора"
        else -> worldId
    }
}

/**
 * Получить описание мира (в будущем из ресурсов)
 */
private fun getWorldDescription(worldId: String): String {
    return when (worldId) {
        "valley_of_beginnings" -> "Переменные и формулы. Научись видеть данные."
        "fields_of_truth" -> "Логика. Всё — правда или ложь."
        "crossroads_of_fate" -> "Условия. Каждый выбор имеет последствия."
        "time_loop" -> "Циклы. Найди выход из повторений."
        "data_river" -> "Последовательности. Научись ловить потоки данных."
        "ocean_of_arrays" -> "Массивы. Подводный мир хранения данных."
        "matrix_citadel" -> "Матрицы. Лабиринт двумерных массивов."
        "text_forest" -> "Строки. Заколдованный лес живых слов."
        "architect_archives" -> "Функции и файлы. Библиотека всех знаний."
        else -> ""
    }
}
