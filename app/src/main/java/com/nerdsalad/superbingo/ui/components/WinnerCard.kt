package com.nerdsalad.superbingo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nerdsalad.superbingo.domain.model.Winner
import com.nerdsalad.superbingo.ui.theme.SaffronOrange
import com.nerdsalad.superbingo.ui.theme.TextMuted
import com.nerdsalad.superbingo.util.toRupees

@Composable
fun WinnerCard(
    winner: Winner,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = winner.categoryName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = winner.prizeAmount.toRupees(),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
            
            Surface(
                color = SaffronOrange.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = winner.playerName,
                    style = MaterialTheme.typography.labelLarge,
                    color = SaffronOrange,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
