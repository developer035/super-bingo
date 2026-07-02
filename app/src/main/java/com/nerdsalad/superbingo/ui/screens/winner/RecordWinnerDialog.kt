package com.nerdsalad.superbingo.ui.screens.winner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nerdsalad.superbingo.domain.model.Player
import com.nerdsalad.superbingo.domain.model.PrizeCategory
import com.nerdsalad.superbingo.ui.components.BingoDialog
import com.nerdsalad.superbingo.ui.components.BingoPrimaryButton
import com.nerdsalad.superbingo.ui.components.BingoTextButton
import com.nerdsalad.superbingo.ui.theme.Cream
import com.nerdsalad.superbingo.ui.theme.TextMuted
import com.nerdsalad.superbingo.util.toRupees

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordWinnerDialog(
    category: PrizeCategory,
    players: List<Player>,
    onSave: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    var expanded by remember { mutableStateOf(false) }

    BingoDialog(
        onDismissRequest = onDismiss,
        title = "Record Winner",
        subtitle = "${category.name} · ${category.amount.toRupees()}",
        confirmButton = {
            BingoPrimaryButton(
                text = "Save Winner",
                onClick = { selectedPlayer?.let { onSave(it.id) } },
                enabled = selectedPlayer != null
            )
        },
        dismissButton = {
            BingoTextButton(
                text = "Cancel",
                onClick = onDismiss
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Custom Dropdown Box
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(Cream)
                        .clickable { expanded = !expanded }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedPlayer?.name ?: "Select a player",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedPlayer == null) TextMuted else MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = TextMuted
                    )
                }

                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        shadowElevation = 4.dp,
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())) {
                            players.forEach { player ->
                                val isSelected = selectedPlayer?.id == player.id
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (isSelected) Color(0xFFFFE8D6) else Color.Transparent)
                                        .clickable {
                                            selectedPlayer = player
                                            expanded = false
                                        }
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = player.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
