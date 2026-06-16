package com.nerdsaladstudios.superbingo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nerdsaladstudios.superbingo.ui.theme.NumberCalled
import com.nerdsaladstudios.superbingo.ui.theme.NumberCurrent
import com.nerdsaladstudios.superbingo.ui.theme.NumberUncalled

enum class NumberState { UNCALLED, CALLED, CURRENT }

@Composable
fun NumberCell(
    number: Int,
    state: NumberState,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            NumberState.UNCALLED -> NumberUncalled
            NumberState.CALLED -> NumberCalled
            NumberState.CURRENT -> NumberCurrent
        },
        label = "color"
    )

    val contentColor = when (state) {
        NumberState.UNCALLED -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> Color.White
    }

    val alpha = if (state == NumberState.CURRENT) {
        val infiniteTransition = rememberInfiniteTransition(label = "alpha")
        val animatedAlpha by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alpha"
        )
        animatedAlpha
    } else {
        1f
    }

    Card(
        modifier = modifier
            .padding(2.dp)
            .aspectRatio(1f)
            .alpha(alpha),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = contentColor
            )
        }
    }
}
