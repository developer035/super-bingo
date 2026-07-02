package com.nerdsalad.superbingo.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nerdsalad.superbingo.navigation.Screen
import com.nerdsalad.superbingo.ui.components.BannerAdView
import com.nerdsalad.superbingo.ui.theme.DeepNavy

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BannerAdView()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Super Bingo 🎱",
                style = MaterialTheme.typography.displaySmall,
                color = DeepNavy,
                fontWeight = FontWeight.Black
            )
            
            Text(
                text = "Tambola · Housie · Family Fun",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            MainActionCard(
                title = "Start New Game",
                subtitle = "Host a fresh bingo session",
                icon = Icons.Default.PlayArrow,
                onClick = { navController.navigate(Screen.NewMatch.route) }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SecondaryActionCard(
                    title = "History",
                    subtitle = "Past games",
                    icon = Icons.Default.History,
                    onClick = { navController.navigate(Screen.MatchHistory.route) },
                    modifier = Modifier.weight(1f)
                )
                SecondaryActionCard(
                    title = "Players",
                    subtitle = "Manage list",
                    icon = Icons.Default.Group,
                    onClick = { navController.navigate(Screen.PlayerManagement.route) },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SecondaryActionCard(
                    title = "Statistics",
                    subtitle = "Player stats",
                    icon = Icons.Default.BarChart,
                    onClick = { navController.navigate(Screen.Statistics.route) },
                    modifier = Modifier.weight(1f)
                )
                SecondaryActionCard(
                    title = "Settings",
                    subtitle = "Preferences",
                    icon = Icons.Default.Settings,
                    onClick = { navController.navigate(Screen.Settings.route) },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
