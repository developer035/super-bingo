package com.nerdsalad.superbingo.ui.screens.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nerdsalad.superbingo.data.repository.MatchRepository
import com.nerdsalad.superbingo.domain.model.MatchDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchSummaryUiState(
    val matchDetail: MatchDetail? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class MatchSummaryViewModel @Inject constructor(
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MatchSummaryUiState())
    val state: StateFlow<MatchSummaryUiState> = _state.asStateFlow()

    fun loadMatchDetail(matchId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val detail = matchRepository.getMatchDetail(matchId)
            _state.update { it.copy(matchDetail = detail, isLoading = false) }
        }
    }
}
