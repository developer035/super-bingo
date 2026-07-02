package com.nerdsalad.superbingo.util

sealed class UiEvent {
    data class NavigateTo(val route: String) : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
    object NavigateBack : UiEvent()
}
