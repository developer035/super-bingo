package com.nerdsalad.superbingo.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val SuperBingoShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),   // Default card radius
    large = RoundedCornerShape(20.dp),    // Buttons, prominent cards
    extraLarge = RoundedCornerShape(28.dp), // Dialogs, hero cards
)
