package com.nerdsalad.superbingo.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nerdsalad.superbingo.data.repository.PlayerRepository
import com.nerdsalad.superbingo.domain.model.Player
import com.nerdsalad.superbingo.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class PlayerManagementUiState(
    val players: List<Player> = emptyList(),
    val isLoading: Boolean = false,
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val editingPlayer: Player? = null,
    val nameError: String? = null,
    val showDeleteConfirm: Boolean = false,
    val deletingPlayer: Player? = null
)

@HiltViewModel
class PlayerManagementViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerManagementUiState())
    val state: StateFlow<PlayerManagementUiState> = _state.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadPlayers()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            playerRepository.getAllPlayers().collect { players ->
                _state.update { it.copy(players = players, isLoading = false) }
            }
        }
    }

    fun onAddClick() {
        _state.update { it.copy(showAddDialog = true, nameError = null) }
    }

    fun onEditClick(player: Player) {
        _state.update { it.copy(showEditDialog = true, editingPlayer = player, nameError = null) }
    }

    fun onDeleteClick(player: Player) {
        _state.update { it.copy(showDeleteConfirm = true, deletingPlayer = player) }
    }

    fun onDismissDialog() {
        _state.update { it.copy(
            showAddDialog = false, 
            showEditDialog = false, 
            showDeleteConfirm = false,
            editingPlayer = null,
            deletingPlayer = null,
            nameError = null
        ) }
    }

    fun addPlayer(name: String) {
        if (name.isBlank()) {
            _state.update { it.copy(nameError = "Name cannot be empty") }
            return
        }
        if (_state.value.players.any { it.name.equals(name, ignoreCase = true) }) {
            _state.update { it.copy(nameError = "Player name already exists") }
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playerRepository.insertPlayer(name)
            }
            _events.send(UiEvent.ShowSnackbar("Player added successfully"))
            onDismissDialog()
        }
    }

    fun editPlayer(id: Long, name: String) {
        if (name.isBlank()) {
            _state.update { it.copy(nameError = "Name cannot be empty") }
            return
        }
        if (_state.value.players.any { it.id != id && it.name.equals(name, ignoreCase = true) }) {
            _state.update { it.copy(nameError = "Player name already exists") }
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playerRepository.updatePlayer(id, name)
            }
            _events.send(UiEvent.ShowSnackbar("Player updated successfully"))
            onDismissDialog()
        }
    }

    fun deletePlayer(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playerRepository.deletePlayer(id)
            }
            _events.send(UiEvent.ShowSnackbar("Player deleted successfully"))
            onDismissDialog()
        }
    }
}
