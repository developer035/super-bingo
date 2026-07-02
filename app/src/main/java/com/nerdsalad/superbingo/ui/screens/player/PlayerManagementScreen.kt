package com.nerdsalad.superbingo.ui.screens.player

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nerdsalad.superbingo.ui.components.*
import com.nerdsalad.superbingo.ui.components.BannerAdView
import com.nerdsalad.superbingo.ui.theme.Cream
import com.nerdsalad.superbingo.ui.theme.SaffronOrange
import com.nerdsalad.superbingo.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerManagementScreen(
    navController: NavController,
    viewModel: PlayerManagementViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.NavigateTo -> navController.navigate(event.route)
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Players",
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
            BannerAdView()
        },
        floatingActionButton = {
            Surface(
                onClick = { viewModel.onAddClick() },
                color = SaffronOrange,
                contentColor = Color.White,
                shape = androidx.compose.foundation.shape.CircleShape,
                border = BorderStroke(2.dp, Color.Black),
                shadowElevation = 6.dp,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, contentDescription = "Add Player")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.players.isEmpty()) {
                PlayerEmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.players) { player ->
                        PlayerItemCard(
                            name = player.name,
                            onEdit = { viewModel.onEditClick(player) },
                            onDelete = { viewModel.onDeleteClick(player) }
                        )
                    }
                }
            }
        }

        if (state.showAddDialog) {
            PlayerDialog(
                title = "Add Player",
                onConfirm = { viewModel.addPlayer(it) },
                onDismiss = { viewModel.onDismissDialog() },
                error = state.nameError
            )
        }

        if (state.showEditDialog && state.editingPlayer != null) {
            PlayerDialog(
                title = "Edit Player",
                initialName = state.editingPlayer!!.name,
                onConfirm = { viewModel.editPlayer(state.editingPlayer!!.id, it) },
                onDismiss = { viewModel.onDismissDialog() },
                error = state.nameError
            )
        }

        if (state.showDeleteConfirm && (state.deletingPlayer != null)) {
            ConfirmationDialog(
                title = "Delete Player",
                message = "Are you sure you want to delete ${state.deletingPlayer!!.name}? This won't affect past game history.",
                onConfirm = { viewModel.deletePlayer(state.deletingPlayer!!.id) },
                onDismiss = { viewModel.onDismissDialog() },
                isDestructive = true
            )
        }
    }
}

@Composable
fun PlayerDialog(
    title: String,
    initialName: String = "",
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    error: String? = null
) {
    var name by remember { mutableStateOf(initialName) }

    BingoDialog(
        onDismissRequest = onDismiss,
        title = title,
        confirmButton = {
            BingoPrimaryButton(
                text = "Save",
                onClick = { onConfirm(name) }
            )
        },
        dismissButton = {
            BingoTextButton(
                text = "Cancel",
                onClick = onDismiss
            )
        }
    ) {
        Column {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Player Name") },
                singleLine = true,
                isError = error != null,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Cream,
                    focusedContainerColor = Cream
                )
            )
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}
