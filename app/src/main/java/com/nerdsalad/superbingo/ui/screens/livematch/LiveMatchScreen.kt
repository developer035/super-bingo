package com.nerdsalad.superbingo.ui.screens.livematch

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nerdsalad.superbingo.domain.model.PrizeCategory
import com.nerdsalad.superbingo.navigation.Screen
import com.nerdsalad.superbingo.ui.components.*
import com.nerdsalad.superbingo.ui.screens.winner.RecordWinnerDialog
import com.nerdsalad.superbingo.ui.theme.GoldenYellow
import com.nerdsalad.superbingo.ui.theme.SaffronOrange
import com.nerdsalad.superbingo.ui.theme.SuccessGreen
import com.nerdsalad.superbingo.ui.theme.TextMuted
import com.nerdsalad.superbingo.util.Constants
import com.nerdsalad.superbingo.util.UiEvent
import com.nerdsalad.superbingo.util.toRupees

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
                title = {
                    val potText = "Pot: ${state.prizeCategories.sumOf { it.amount }.toRupees()}"
                    val playersText = "${state.players.size} Players"
                    Text(
                        text = "$potText · $playersText",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    OutlinedButton(
                        onClick = { viewModel.undoLastNumber() },
                        enabled = state.calledNumbers.isNotEmpty(),
                        modifier = Modifier.padding(start = 8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        border = BorderStroke(1.dp, SaffronOrange),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SaffronOrange)
                    ) {
                        Icon(Icons.Default.Undo, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Undo", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    OutlinedIconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp),
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, Color.Black),
                        colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = Color.Black)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Menu",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Restart Game") },
                            onClick = {
                                showMenu = false
                                viewModel.onRestartClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("End Game") },
                            onClick = {
                                showMenu = false
                                viewModel.onEndMatchClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Exit Game") },
                            onClick = {
                                showMenu = false
                                viewModel.onExitClick()
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .size(width = 280.dp, height = 240.dp),
                color = if (state.isGameComplete) GoldenYellow else SaffronOrange,
                shape = MaterialTheme.shapes.extraLarge,
                border = BorderStroke(2.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.isGameComplete) {
                        Text(
                            text = "All 90\nNumbers\nCalled! 🎉",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 48.sp
                        )
                    } else {
                        AnimatedContent(
                            targetState = state.currentNumber,
                            transitionSpec = {
                                scaleIn() togetherWith scaleOut()
                            },
                            label = "number"
                        ) { number ->
                            Text(
                                text = number?.toString() ?: "Welcome",
                                fontSize = if (number == null) 56.sp else 120.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        if (state.currentNumber != null) {
                            Text(
                                text = "Current Number",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            // Recently Called
            if (state.recentNumbers.isNotEmpty()) {
                RecentNumbersRow(
                    numbers = state.recentNumbers,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Call Next Number Button
            Button(
                onClick = { 
                    if (state.isGameComplete) {
                        viewModel.onEndMatchClick()
                    } else {
                        viewModel.callNextNumber()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(64.dp),
                enabled = true,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldenYellow,
                    contentColor = Color.Black,
                    disabledContainerColor = GoldenYellow.copy(alpha = 0.5f)
                ),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                val buttonText = if (state.isGameComplete) "CHECKOUT SUMMARY 📊" else "CALL NEXT NUMBER 🎱"
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Number Board
            NumberBoard(
                calledNumbers = state.calledNumbers.toSet(),
                currentNumber = state.currentNumber,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            // Prize Claims Section
            Text(
                text = "Prize Claims",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            state.prizeCategories.forEach { category ->
                val winner = state.winners.find { it.prizeCategoryId == category.id }
                PrizeClaimCard(
                    category = category,
                    winnerName = winner?.playerName,
                    onClaim = { viewModel.showRecordWinnerFor(category) },
                    onRemoveClick = { winner?.let { viewModel.onRemoveWinnerClick(it) } }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.onRestartClick() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    border = BorderStroke(2.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Restart Game", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                
                BingoPrimaryButton(
                    text = "End Game",
                    onClick = { viewModel.onEndMatchClick() },
                    modifier = Modifier.weight(1f).height(56.dp),
                    isDestructive = true
                )
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
                title = "Restart Game",
                message = "Are you sure you want to restart? This will clear all called numbers and winners for this game.",
                countdownSeconds = Constants.RESTART_COUNTDOWN_SECONDS,
                onConfirm = { viewModel.restartMatch() },
                onDismiss = { viewModel.onDismissRestart() },
                isDestructive = true
            )
        }

        if (state.showEndMatchConfirm) {
            CountdownConfirmDialog(
                title = "End Game",
                message = "Are you sure you want to end this game? You will be taken to the summary screen.",
                countdownSeconds = 2,
                onConfirm = { viewModel.endMatch() },
                onDismiss = { viewModel.onDismissEndMatch() },
                isDestructive = true
            )
        }

        if (state.showExitConfirm) {
            ConfirmationDialog(
                title = "Exit Game",
                message = "Are you sure you want to exit the game? You can resume it from history later.",
                onConfirm = { navController.popBackStack() },
                onDismiss = { viewModel.onDismissExit() }
            )
        }

        if (state.showRemoveWinnerConfirm && state.selectedWinnerForRemoval != null) {
            ConfirmationDialog(
                title = "Remove Claim",
                message = "Do you really want to remove ${state.selectedWinnerForRemoval!!.playerName} from claim?",
                onConfirm = { viewModel.removeWinner() },
                onDismiss = { viewModel.onDismissRemoveWinner() },
                isDestructive = true
            )
        }
    }
}

@Composable
fun PrizeClaimCard(
    category: PrizeCategory,
    winnerName: String?,
    onClaim: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name, 
                    style = MaterialTheme.typography.titleMedium, 
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = category.amount.toRupees(), 
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }

            if (winnerName != null) {
                Surface(
                    color = SuccessGreen.copy(alpha = 0.1f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, SuccessGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(start = 8.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onRemoveClick,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close, 
                                contentDescription = "Remove winner", 
                                modifier = Modifier.size(16.dp),
                                tint = SuccessGreen
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = winnerName,
                            style = MaterialTheme.typography.labelLarge,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Button(
                    onClick = onClaim,
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronOrange)
                ) {
                    Text("Claim", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
