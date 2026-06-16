package com.nerdsaladstudios.superbingo.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nerdsaladstudios.superbingo.data.repository.MatchRepository
import com.nerdsaladstudios.superbingo.domain.model.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchHistoryUiState(
    val matches: List<Match> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class MatchHistoryViewModel @Inject constructor(
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MatchHistoryUiState())
    val state: StateFlow<MatchHistoryUiState> = _state.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            matchRepository.getAllMatches().collect { matches ->
                _state.update { it.copy(matches = matches, isLoading = false) }
            }
        }
    }
}
