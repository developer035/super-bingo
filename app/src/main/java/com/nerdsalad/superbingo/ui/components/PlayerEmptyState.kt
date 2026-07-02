package com.nerdsalad.superbingo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nerdsalad.superbingo.ui.theme.DeepNavy
import com.nerdsalad.superbingo.ui.theme.TextMuted

@Composable
fun PlayerEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Character Illustration
        // Note: You should add an image named 'ill_no_players' to your res/drawable folder
        // For now, we use a placeholder icon if the resource is not found
        Box(
            modifier = Modifier
                .size(280.dp)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Using a try-catch style approach or checking for resource is not easy in Compose
            // We'll just put the Image call here. If it fails to compile, the user can add the asset.
            // To make it safe for now, I will use a simple placeholder if you don't have the image yet.
            Text(
                text = "🙋‍♂️", // Placeholder emoji
                fontSize = 120.sp
            )
            /* 
            Image(
                painter = painterResource(id = com.nerdsaladstudios.superbingo.R.drawable.ill_no_players),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            */
        }
        
        Text(
            text = "No players yet!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = DeepNavy,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Add your family and friends\nto get started.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            fontSize = 18.sp
        )
    }
}
