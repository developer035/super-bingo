package com.nerdsaladstudios.superbingo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nerdsaladstudios.superbingo.domain.model.PlayerStatistics
import com.nerdsaladstudios.superbingo.util.toFormattedDate
import com.nerdsaladstudios.superbingo.util.toRupees

@Composable
fun StatCard(
    stats: PlayerStatistics
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stats.player.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                StatItem(
                    icon = Icons.Default.Games,
                    value = stats.gamesPlayed.toString(),
                    label = "Games"
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatItem(
                    icon = Icons.Default.EmojiEvents,
                    value = stats.totalWins.toString(),
                    label = "Wins"
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatItem(
                    icon = Icons.Default.Payments,
                    value = stats.totalPrizeMoney.toRupees(),
                    label = "Prize"
                )
            }
            
            if (stats.lastWinDate != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Last win: ${stats.lastWinDate.toFormattedDate()}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
