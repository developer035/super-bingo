package com.nerdsalad.superbingo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NumberBoard(
    calledNumbers: Set<Int>,
    currentNumber: Int?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        for (row in 0 until 9) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 1..10) {
                    val number = row * 10 + col
                    val state = when {
                        number == currentNumber -> NumberState.CURRENT
                        calledNumbers.contains(number) -> NumberState.CALLED
                        else -> NumberState.UNCALLED
                    }
                    NumberCell(
                        number = number,
                        state = state,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
