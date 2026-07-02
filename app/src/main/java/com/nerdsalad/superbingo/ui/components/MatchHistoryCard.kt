package com.nerdsalad.superbingo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nerdsalad.superbingo.domain.model.Match
import com.nerdsalad.superbingo.ui.theme.SaffronOrange
import com.nerdsalad.superbingo.ui.theme.TextMuted
import com.nerdsalad.superbingo.util.toFormattedDate
import com.nerdsalad.superbingo.util.toFormattedTime
import com.nerdsalad.superbingo.util.toRupees

@Composable
fun MatchHistoryCard(
    match: Match,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.6f)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = match.createdAt.toFormattedDate(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = match.createdAt.toFormattedTime(),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
                
                if (match.isCompleted) {
                    Surface(
                        color = Color(0xFFE7F5E8),
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            text = "Completed",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Surface(
                        color = Color(0xFFFFEDE1),
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            text = "In Progress",
                            style = MaterialTheme.typography.labelSmall,
                            color = SaffronOrange,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
             Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 4.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                HistoryStatItem(
                    icon = Icons.Default.Group,
                    value = "${match.playerIds.size}",
                    label = "Players"
                )
                HistoryStatItem(
                    icon = Icons.Default.Payments,
                    value = match.totalPotAmount.toRupees(),
                    label = "Total Pot"
                )
            }
        }
    }
}

@Composable
fun HistoryStatItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = SaffronOrange.copy(alpha = 0.1f),
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = SaffronOrange
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}
