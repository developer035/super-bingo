package com.nerdsaladstudios.superbingo.ui.screens.newmatch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nerdsaladstudios.superbingo.navigation.Screen
import com.nerdsaladstudios.superbingo.ui.components.PlayerCheckboxRow
import com.nerdsaladstudios.superbingo.ui.components.PrizeCategoryRow
import com.nerdsaladstudios.superbingo.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMatchScreen(
    navController: NavController,
    viewModel: NewMatchViewModel = hiltViewModel()
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
        topBar = {
            TopAppBar(
                title = { Text("New Match") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Section 1: Pot Amount
            Text(
                text = "Total Pot Amount",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = state.potAmount,
                onValueChange = { viewModel.updatePotAmount(it) },
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("₹") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section 2: Select Players
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Select Players",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "(${state.selectedPlayerIds.size} selected)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (state.allPlayers.isEmpty()) {
                Text(
                    text = "No players found. Please add players first.",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Button(onClick = { navController.navigate(Screen.PlayerManagement.route) }) {
                    Text("Go to Player Management")
                }
            } else {
                state.allPlayers.forEach { player ->
                    PlayerCheckboxRow(
                        player = player,
                        isSelected = state.selectedPlayerIds.contains(player.id),
                        onToggle = { viewModel.togglePlayer(player.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 3: Prize Categories
            Text(
                text = "Prize Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            state.prizeCategories.forEachIndexed { index, category ->
                PrizeCategoryRow(
                    category = category,
                    onNameChange = { viewModel.updatePrizeCategory(index, it, category.amount.toString()) },
                    onAmountChange = { viewModel.updatePrizeCategory(index, category.name, it) },
                    onDelete = { viewModel.removePrizeCategory(index) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            TextButton(onClick = { viewModel.addPrizeCategory() }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Category")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.startMatch() },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isStartEnabled
            ) {
                Text("Start Match")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
