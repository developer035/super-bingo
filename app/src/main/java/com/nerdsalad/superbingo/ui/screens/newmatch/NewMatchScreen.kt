package com.nerdsalad.superbingo.ui.screens.newmatch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nerdsalad.superbingo.navigation.Screen
import com.nerdsalad.superbingo.ui.components.BingoPrimaryButton
import com.nerdsalad.superbingo.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMatchScreen(
    navController: NavController,
    viewModel: NewMatchViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.NavigateTo -> navController.navigate(event.route) {
                    popUpTo(Screen.Home.route) { inclusive = false }
                }
                is UiEvent.NavigateBack -> navController.popBackStack()
                else -> {}
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "New Game",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                BingoPrimaryButton(
                    text = "Start Game",
                    onClick = { viewModel.startMatch() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isStartEnabled
                )
            }
        },
        modifier = Modifier.padding()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Pot Amount
            SectionHeader(title = "Total Pot Amount")
            Spacer(modifier = Modifier.height(8.dp))
            PotAmountField(
                value = state.potAmount,
                onValueChange = { viewModel.updatePotAmount(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section 2: Select Players
            SectionHeader(
                title = "Select Players",
                badgeText = "${state.selectedPlayerIds.size} selected"
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (state.allPlayers.isEmpty()) {
                Text(
                    text = "No players found. Please add players first.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Button(
                    onClick = { navController.navigate(Screen.PlayerManagement.route) },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Go to Player Management")
                }
            } else {
                state.allPlayers.forEach { player ->
                    PlayerSelectionCard(
                        name = player.name,
                        isSelected = state.selectedPlayerIds.contains(player.id),
                        onToggle = { viewModel.togglePlayer(player.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 3: Prize Categories
            SectionHeader(title = "Prize Categories")
            Spacer(modifier = Modifier.height(12.dp))

            state.prizeCategories.forEachIndexed { index, category ->
                CategoryEditCard(
                    name = category.name,
                    amount = if (category.amount == 0.0) "" else category.amount.toInt().toString(),
                    onNameChange = { viewModel.updatePrizeCategory(index, it, category.amount.toString()) },
                    onAmountChange = { viewModel.updatePrizeCategory(index, category.name, it) },
                    onDelete = { viewModel.removePrizeCategory(index) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            AddCategoryButton(onClick = { viewModel.addPrizeCategory() })

            if (state.canUndo) {
                Spacer(modifier = Modifier.height(8.dp))
                UndoRemoveButton(
                    onClick = { viewModel.undoRemovePrizeCategory() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
