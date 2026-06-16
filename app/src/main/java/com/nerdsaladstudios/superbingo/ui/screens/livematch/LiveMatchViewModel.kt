package com.nerdsaladstudios.superbingo.ui.screens.livematch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nerdsaladstudios.superbingo.data.repository.MatchRepository
import com.nerdsaladstudios.superbingo.domain.model.Player
import com.nerdsaladstudios.superbingo.domain.model.PrizeCategory
import com.nerdsaladstudios.superbingo.domain.model.Winner
import com.nerdsaladstudios.superbingo.navigation.Screen
import com.nerdsaladstudios.superbingo.util.Constants
import com.nerdsaladstudios.superbingo.util.GameEngine
import com.nerdsaladstudios.superbingo.util.NumberAnnouncer
import com.nerdsaladstudios.superbingo.util.SoundEffectPlayer
import com.nerdsaladstudios.superbingo.util.UiEvent
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

data class LiveMatchUiState(
    val matchId: Long = -1,
    val currentNumber: Int? = null,
    val calledNumbers: List<Int> = emptyList(),
    val recentNumbers: List<Int> = emptyList(),
    val remainingCount: Int = 90,
    val isGameComplete: Boolean = false,
    val prizeCategories: List<PrizeCategory> = emptyList(),
    val winners: List<Winner> = emptyList(),
    val players: List<Player> = emptyList(),
    val showRecordWinnerDialog: Boolean = false,
    val selectedCategoryForWinner: PrizeCategory? = null,
    val showRestartDialog: Boolean = false,
    val showExitConfirm: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class LiveMatchViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    private val numberAnnouncer: NumberAnnouncer,
    private val soundEffectPlayer: SoundEffectPlayer
) : ViewModel() {

    private val gameEngine = GameEngine()
    private val _state = MutableStateFlow(LiveMatchUiState())
    val state: StateFlow<LiveMatchUiState> = _state.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun initMatch(matchId: Long) {
        if (_state.value.matchId == matchId) return
        
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, matchId = matchId) }
            val detail = matchRepository.getMatchDetail(matchId)
            if (detail != null) {
                gameEngine.restoreState(detail.calledNumbers.map { it.number })
                _state.update { it.copy(
                    currentNumber = gameEngine.currentNumber,
                    calledNumbers = gameEngine.calledNumbers,
                    recentNumbers = gameEngine.calledNumbers.reversed().take(Constants.RECENT_NUMBERS_COUNT),
                    remainingCount = gameEngine.remainingCount,
                    isGameComplete = gameEngine.isGameComplete,
                    prizeCategories = detail.prizeCategories,
                    winners = detail.winners,
                    players = detail.players,
                    isLoading = false
                ) }
            }
        }
    }

    fun callNextNumber() {
        if (gameEngine.isGameComplete) return

        val number = gameEngine.callNextNumber() ?: return
        
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                matchRepository.addCalledNumber(
                    _state.value.matchId, 
                    number, 
                    gameEngine.calledNumbers.size
                )
            }
            
            _state.update { it.copy(
                currentNumber = number,
                calledNumbers = gameEngine.calledNumbers,
                recentNumbers = gameEngine.calledNumbers.reversed().take(Constants.RECENT_NUMBERS_COUNT),
                remainingCount = gameEngine.remainingCount,
                isGameComplete = gameEngine.isGameComplete
            ) }
            
            numberAnnouncer.announce(number)
            soundEffectPlayer.playNumberCalled()
            
            if (gameEngine.isGameComplete) {
                numberAnnouncer.announceGameComplete()
                soundEffectPlayer.playGameComplete()
            }
        }
    }

    fun undoLastNumber() {
        viewModelScope.launch {
            val undoneNumber = withContext(Dispatchers.IO) {
                matchRepository.removeLastCalledNumber(_state.value.matchId)
            }
            
            if (undoneNumber != null) {
                gameEngine.undoLastNumber()
                _state.update { it.copy(
                    currentNumber = gameEngine.currentNumber,
                    calledNumbers = gameEngine.calledNumbers,
                    recentNumbers = gameEngine.calledNumbers.reversed().take(Constants.RECENT_NUMBERS_COUNT),
                    remainingCount = gameEngine.remainingCount,
                    isGameComplete = gameEngine.isGameComplete
                ) }
                _events.send(UiEvent.ShowSnackbar("Undone: $undoneNumber"))
            }
        }
    }

    fun onRestartClick() {
        _state.update { it.copy(showRestartDialog = true) }
    }

    fun restartMatch() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                matchRepository.resetMatch(_state.value.matchId)
            }
            gameEngine.reset()
            _state.update { it.copy(
                currentNumber = null,
                calledNumbers = emptyList(),
                recentNumbers = emptyList(),
                remainingCount = 90,
                isGameComplete = false,
                winners = emptyList(),
                showRestartDialog = false
            ) }
        }
    }

    fun onExitClick() {
        _state.update { it.copy(showExitConfirm = true) }
    }

    fun endMatch() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                matchRepository.completeMatch(_state.value.matchId)
            }
            _events.send(UiEvent.NavigateTo(Screen.MatchSummary.createRoute(_state.value.matchId)))
        }
    }

    fun showRecordWinnerFor(category: PrizeCategory) {
        _state.update { it.copy(showRecordWinnerDialog = true, selectedCategoryForWinner = category) }
    }

    fun dismissRecordWinnerDialog() {
        _state.update { it.copy(showRecordWinnerDialog = false, selectedCategoryForWinner = null) }
    }

    fun recordWinner(playerId: Long) {
        val category = _state.value.selectedCategoryForWinner ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                matchRepository.recordWinner(
                    _state.value.matchId,
                    category.id,
                    playerId,
                    category.amount
                )
            }
            val updatedWinners = matchRepository.getWinnersForMatch(_state.value.matchId)
            _state.update { it.copy(
                winners = updatedWinners,
                showRecordWinnerDialog = false,
                selectedCategoryForWinner = null
            ) }
            soundEffectPlayer.playWinnerRecorded()
        }
    }
    
    fun onDismissRestart() {
        _state.update { it.copy(showRestartDialog = false) }
    }
    
    fun onDismissExit() {
        _state.update { it.copy(showExitConfirm = false) }
    }
}
