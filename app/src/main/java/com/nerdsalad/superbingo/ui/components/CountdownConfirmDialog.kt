package com.nerdsalad.superbingo.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@Composable
fun CountdownConfirmDialog(
    title: String,
    message: String,
    countdownSeconds: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDestructive: Boolean = false
) {
    var secondsLeft by remember { mutableStateOf(countdownSeconds) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    BingoDialog(
        onDismissRequest = onDismiss,
        title = title,
        confirmButton = {
            BingoPrimaryButton(
                text = if (secondsLeft > 0) "OK ($secondsLeft)" else "OK",
                onClick = onConfirm,
                enabled = secondsLeft == 0,
                isDestructive = isDestructive
            )
        },
        dismissButton = {
            BingoTextButton(
                text = "Cancel",
                onClick = onDismiss
            )
        }
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
