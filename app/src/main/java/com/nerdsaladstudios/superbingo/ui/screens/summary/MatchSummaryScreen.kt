package com.nerdsaladstudios.superbingo.ui.screens.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nerdsaladstudios.superbingo.navigation.Screen
import com.nerdsaladstudios.superbingo.ui.components.WinnerCard
import com.nerdsaladstudios.superbingo.util.toFormattedDate
import com.nerdsaladstudios.superbingo.util.toFormattedTime
import com.nerdsaladstudios.superbingo.util.toRupees

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchSummaryScreen(
    navController: NavController,
    matchId: Long,
    viewModel: MatchSummaryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(matchId) {
        viewModel.loadMatchDetail(matchId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Match Summary 🎉") })
        }
    ) { padding ->
        state.matchDetail?.let { detail ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Game Over!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(text = "${detail.match.createdAt.toFormattedDate()} at ${detail.match.createdAt.toFormattedTime()}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Total Pot: ${detail.match.totalPotAmount.toRupees()}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Players: ${detail.match.playerIds.size}")
                        Text(text = "Numbers Called: ${detail.calledNumbers.size} / 90")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Winners",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                if (detail.winners.isEmpty()) {
                    Text(text = "No winners recorded for this match.", modifier = Modifier.padding(vertical = 16.dp))
                } else {
                    detail.winners.forEach { winner ->
                        Spacer(modifier = Modifier.height(8.dp))
                        WinnerCard(winner = winner)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { 
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Home")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { navController.navigate(Screen.MatchHistory.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View in History")
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
