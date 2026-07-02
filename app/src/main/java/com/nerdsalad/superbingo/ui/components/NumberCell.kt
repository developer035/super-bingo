package com.nerdsalad.superbingo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nerdsalad.superbingo.ui.theme.NumberCalled
import com.nerdsalad.superbingo.ui.theme.NumberCurrent
import com.nerdsalad.superbingo.ui.theme.NumberUncalled

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
        NumberState.UNCALLED -> Color.Black
        else -> Color.White
    }

    Surface(
        modifier = modifier
            .padding(1.dp)
            .aspectRatio(1f),
        color = backgroundColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.8f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
