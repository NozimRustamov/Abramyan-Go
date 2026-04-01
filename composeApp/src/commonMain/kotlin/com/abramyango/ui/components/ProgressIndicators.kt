package com.abramyango.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.abramyango.ui.theme.AppTheme

/**
 * XP Progress Bar - анимированный прогресс-бар для опыта
 */
@Composable
fun XpProgressBar(
    progress: Float,
    currentXp: Int,
    targetXp: Int,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )
    
    Column(modifier = modifier) {
        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                colors.xpBar,
                                colors.xpBar.copy(alpha = 0.8f)
                            )
                        )
                    )
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // XP text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$currentXp XP",
                style = typography.labelSmall,
                color = colors.textSecondary
            )
            Text(
                text = "$targetXp XP",
                style = typography.labelSmall,
                color = colors.textTertiary
            )
        }
    }
}

/**
 * Energy Bar - шкала энергии
 */
@Composable
fun EnergyBar(
    current: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val progress = (current.toFloat() / max).coerceIn(0f, 1f)
    
    val barColor = if (progress > 0.3f) {
        colors.energyFull
    } else {
        colors.energyLow
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(400)
    )
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Energy icon (lightning bolt style)
        Text(
            text = "⚡",
            style = typography.labelMedium
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor)
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = "$current/$max",
            style = typography.labelSmall,
            color = colors.textSecondary
        )
    }
}

/**
 * Combo Indicator - индикатор комбо с эффектом огня
 */
@Composable
fun ComboIndicator(
    comboCount: Int,
    multiplier: Int,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    if (comboCount == 0) return
    
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Fire emoji for high combos
        if (multiplier >= 3) {
            Text(
                text = "🔥",
                style = typography.titleLarge,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
        
        GlassCard(
            glassAlpha = 0.2f,
            cornerRadius = 12.dp,
            contentPadding = 8.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "×$multiplier",
                    style = typography.headlineMedium,
                    color = colors.accentCombo
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = "($comboCount)",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
            }
        }
    }
}

/**
 * Streak Counter - счётчик дней стрика
 */
@Composable
fun StreakCounter(
    days: Int,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    GlassCard(
        modifier = modifier,
        glassAlpha = 0.15f,
        cornerRadius = 16.dp,
        contentPadding = 12.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔥",
                style = typography.titleLarge
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column {
                Text(
                    text = "$days",
                    style = typography.headlineMedium,
                    color = colors.streakFlame
                )
                Text(
                    text = if (days == 1) "день" else "дней",
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
            }
        }
    }
}

/**
 * World Progress Indicator - круговой индикатор прогресса мира
 */
@Composable
fun WorldProgressIndicator(
    progress: Float,
    accentColor: Color,
    modifier: Modifier = Modifier,
    size: Int = 48
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800, easing = FastOutSlowInEasing)
    )
    
    Canvas(
        modifier = modifier.size(size.dp)
    ) {
        val strokeWidth = 4.dp.toPx()
        val radius = (this.size.minDimension - strokeWidth) / 2
        val center = Offset(this.size.width / 2, this.size.height / 2)
        
        // Background circle
        drawCircle(
            color = Color.White.copy(alpha = 0.1f),
            radius = radius,
            center = center,
            style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth)
        )
        
        // Progress arc
        drawArc(
            color = accentColor,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            topLeft = Offset(
                center.x - radius,
                center.y - radius
            ),
            size = Size(radius * 2, radius * 2),
            style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth)
        )
    }
}

/**
 * Rank Badge - бейдж ранга
 */
@Composable
fun RankBadge(
    rankTitle: String,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colors.accentPrimary,
                        colors.accentPrimary.copy(alpha = 0.7f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rankTitle,
            style = typography.labelMedium,
            color = colors.textOnAccent
        )
    }
}
