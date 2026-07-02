package com.nerdsalad.superbingo.ui.screens.newmatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nerdsalad.superbingo.data.repository.MatchRepository
import com.nerdsalad.superbingo.data.repository.PlayerRepository
import com.nerdsalad.superbingo.domain.model.Player
import com.nerdsalad.superbingo.domain.model.PrizeCategory
import com.nerdsalad.superbingo.navigation.Screen
import com.nerdsalad.superbingo.util.Constants
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

data class NewMatchUiState(
    val potAmount: String = "",
    val allPlayers: List<Player> = emptyList(),
    val selectedPlayerIds: Set<Long> = emptySet(),
    val prizeCategories: List<PrizeCategory> = emptyList(),
    val isLoading: Boolean = false,
    val potAmountError: String? = null,
    val isStartEnabled: Boolean = false,
    val canUndo: Boolean = false
)

@HiltViewModel
class NewMatchViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewMatchUiState(
        prizeCategories = Constants.DEFAULT_CATEGORY_NAMES.map { name ->
            PrizeCategory(name = name, amount = 0.0)
        }
    ))
    val state: StateFlow<NewMatchUiState> = _state.asStateFlow()

    private val undoStack = java.util.Stack<List<PrizeCategory>>()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadPlayers()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            playerRepository.getAllPlayers().collect { players ->
                _state.update { it.copy(allPlayers = players) }
            }
        }
    }

    fun updatePotAmount(amount: String) {
        _state.update { it.copy(potAmount = amount) }
        validate()
    }

    fun togglePlayer(playerId: Long) {
        _state.update { 
            val newSelected = if (it.selectedPlayerIds.contains(playerId)) {
                it.selectedPlayerIds - playerId
            } else {
                it.selectedPlayerIds + playerId
            }
            it.copy(selectedPlayerIds = newSelected)
        }
        validate()
    }

    fun addPrizeCategory() {
        _state.update { 
            it.copy(prizeCategories = it.prizeCategories + PrizeCategory(name = "", amount = 0.0))
        }
    }

    fun updatePrizeCategory(index: Int, name: String, amount: String) {
        _state.update { state ->
            val updated = state.prizeCategories.toMutableList()
            if (index in updated.indices) {
                val amountDouble = amount.toDoubleOrNull() ?: 0.0
                updated[index] = updated[index].copy(name = name, amount = amountDouble)
            }
            state.copy(prizeCategories = updated)
        }
    }

    fun removePrizeCategory(index: Int) {
        _state.update { state ->
            val updated = state.prizeCategories.toMutableList()
            if (index in updated.indices) {
                undoStack.push(state.prizeCategories)
                updated.removeAt(index)
            }
            state.copy(prizeCategories = updated, canUndo = true)
        }
    }

    fun undoRemovePrizeCategory() {
        if (undoStack.isNotEmpty()) {
            val previousList = undoStack.pop()
            _state.update { it.copy(prizeCategories = previousList, canUndo = undoStack.isNotEmpty()) }
        }
    }

    private fun validate() {
        _state.update { 
            val pot = it.potAmount.toDoubleOrNull() ?: 0.0
            it.copy(isStartEnabled = pot > 0 && it.selectedPlayerIds.isNotEmpty())
        }
    }

    fun startMatch() {
        viewModelScope.launch {
            val pot = _state.value.potAmount.toDoubleOrNull() ?: 0.0
            val playerIds = _state.value.selectedPlayerIds.toList()
            val categories = _state.value.prizeCategories.filter { it.name.isNotBlank() }

            val matchId = withContext(Dispatchers.IO) {
                matchRepository.createMatch(pot, playerIds, categories)
            }
            
            _events.send(UiEvent.NavigateTo(Screen.LiveMatch.createRoute(matchId)))
        }
    }
}
