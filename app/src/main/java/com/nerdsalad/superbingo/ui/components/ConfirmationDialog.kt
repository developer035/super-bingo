package com.nerdsalad.superbingo.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDestructive: Boolean = false
) {
    BingoDialog(
        onDismissRequest = onDismiss,
        title = title,
        confirmButton = {
            BingoPrimaryButton(
                text = "Confirm",
                onClick = {
                    onConfirm()
                    onDismiss()
                },
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
