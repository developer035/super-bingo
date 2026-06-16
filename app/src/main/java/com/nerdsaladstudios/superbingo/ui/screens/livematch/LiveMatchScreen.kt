package com.nerdsaladstudios.superbingo.ui.screens.livematch

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nerdsaladstudios.superbingo.domain.model.PrizeCategory
import com.nerdsaladstudios.superbingo.navigation.Screen
import com.nerdsaladstudios.superbingo.ui.components.ConfirmationDialog
import com.nerdsaladstudios.superbingo.ui.components.CountdownConfirmDialog
import com.nerdsaladstudios.superbingo.ui.components.NumberBoard
import com.nerdsaladstudios.superbingo.ui.components.RecentNumbersRow
import com.nerdsaladstudios.superbingo.ui.screens.winner.RecordWinnerDialog
import com.nerdsaladstudios.superbingo.util.Constants
import com.nerdsaladstudios.superbingo.util.UiEvent
import com.nerdsaladstudios.superbingo.util.toRupees

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveMatchScreen(
    navController: NavController,
    matchId: Long,
    viewModel: LiveMatchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(matchId) {
        viewModel.initMatch(matchId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.NavigateTo -> navController.navigate(event.route) {
                    popUpTo(Screen.LiveMatch.route) { inclusive = true }
                }
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    BackHandler {
        viewModel.onExitClick()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Match") },
                actions = {
                    IconButton(
                        onClick = { viewModel.undoLastNumber() },
                        enabled = state.calledNumbers.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Undo, contentDescription = "Undo")
                    }
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Restart Match") },
                            onClick = {
                                showMenu = false
                                viewModel.onRestartClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("End Match") },
                            onClick = {
                                showMenu = false
                                viewModel.endMatch()
                            }
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Current Number Display
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .size(200.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Current Number",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    AnimatedContent(
                        targetState = state.currentNumber,
                        transitionSpec = {
                            scaleIn() togetherWith scaleOut()
                        },
                        label = "number"
                    ) { number ->
                        Text(
                            text = number?.toString() ?: "🎱",
                            fontSize = 96.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Call Next Number Button
            Button(
                onClick = { viewModel.callNextNumber() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(64.dp),
                enabled = !state.isGameComplete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (state.isGameComplete) "GAME OVER 🎉" else "CALL NEXT NUMBER 🎱",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recently Called
            if (state.recentNumbers.isNotEmpty()) {
                Text(
                    text = "Recently Called",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                RecentNumbersRow(
                    numbers = state.recentNumbers,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Number Board
            Text(
                text = "Number Board",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            NumberBoard(
                calledNumbers = state.calledNumbers.toSet(),
                currentNumber = state.currentNumber,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Prize Claims Section
            Text(
                text = "Prize Claims",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            state.prizeCategories.forEach { category ->
                val winner = state.winners.find { it.prizeCategoryId == category.id }
                PrizeClaimRow(
                    category = category,
                    winnerName = winner?.playerName,
                    onClaim = { viewModel.showRecordWinnerFor(category) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            ElevatedButton(
                onClick = { viewModel.endMatch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("End Match")
            }
        }

        // Dialogs
        if (state.showRecordWinnerDialog && state.selectedCategoryForWinner != null) {
            RecordWinnerDialog(
                category = state.selectedCategoryForWinner!!,
                players = state.players,
                onSave = { viewModel.recordWinner(it) },
                onDismiss = { viewModel.dismissRecordWinnerDialog() }
            )
        }

        if (state.showRestartDialog) {
            CountdownConfirmDialog(
                title = "Restart Match",
                message = "Are you sure you want to restart? This will clear all called numbers and winners for this match.",
                countdownSeconds = Constants.RESTART_COUNTDOWN_SECONDS,
                onConfirm = { viewModel.restartMatch() },
                onDismiss = { viewModel.onDismissRestart() }
            )
        }

        if (state.showExitConfirm) {
            ConfirmationDialog(
                title = "Exit Match",
                message = "Are you sure you want to exit the match? You can resume it from history later.",
                onConfirm = { navController.popBackStack() },
                onDismiss = { viewModel.onDismissExit() }
            )
        }
    }
}

@Composable
fun PrizeClaimRow(
    category: PrizeCategory,
    winnerName: String?,
    onClaim: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = category.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(text = category.amount.toRupees(), style = MaterialTheme.typography.bodyMedium)
        }

        if (winnerName != null) {
            SuggestionChip(
                onClick = { },
                label = { Text(winnerName) },
                icon = { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    labelColor = Color(0xFF4CAF50),
                    iconContentColor = Color(0xFF4CAF50)
                )
            )
        } else {
            ElevatedButton(onClick = onClaim) {
                Text("Claim")
            }
        }
    }
}
