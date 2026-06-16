package com.nerdsaladstudios.superbingo.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object PlayerManagement : Screen("player_management")
    object NewMatch : Screen("new_match")
    
    object LiveMatch : Screen("live_match/{matchId}") {
        fun createRoute(matchId: Long) = "live_match/$matchId"
    }
    
    object MatchSummary : Screen("match_summary/{matchId}") {
        fun createRoute(matchId: Long) = "match_summary/$matchId"
    }
    
    object MatchHistory : Screen("match_history")
    
    object MatchDetail : Screen("match_detail/{matchId}") {
        fun createRoute(matchId: Long) = "match_detail/$matchId"
    }
    
    object Statistics : Screen("statistics")
    object Settings : Screen("settings")
}
