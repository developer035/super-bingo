package com.nerdsaladstudios.superbingo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nerdsaladstudios.superbingo.ui.screens.history.MatchDetailScreen
import com.nerdsaladstudios.superbingo.ui.screens.history.MatchHistoryScreen
import com.nerdsaladstudios.superbingo.ui.screens.home.HomeScreen
import com.nerdsaladstudios.superbingo.ui.screens.livematch.LiveMatchScreen
import com.nerdsaladstudios.superbingo.ui.screens.newmatch.NewMatchScreen
import com.nerdsaladstudios.superbingo.ui.screens.player.PlayerManagementScreen
import com.nerdsaladstudios.superbingo.ui.screens.settings.SettingsScreen
import com.nerdsaladstudios.superbingo.ui.screens.statistics.StatisticsScreen
import com.nerdsaladstudios.superbingo.ui.screens.summary.MatchSummaryScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.PlayerManagement.route) {
            PlayerManagementScreen(navController = navController)
        }
        
        composable(Screen.NewMatch.route) {
            NewMatchScreen(navController = navController)
        }
        
        composable(
            route = Screen.LiveMatch.route,
            arguments = listOf(navArgument("matchId") { type = NavType.LongType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getLong("matchId") ?: -1L
            LiveMatchScreen(navController = navController, matchId = matchId)
        }
        
        composable(
            route = Screen.MatchSummary.route,
            arguments = listOf(navArgument("matchId") { type = NavType.LongType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getLong("matchId") ?: -1L
            MatchSummaryScreen(navController = navController, matchId = matchId)
        }
        
        composable(Screen.MatchHistory.route) {
            MatchHistoryScreen(navController = navController)
        }
        
        composable(
            route = Screen.MatchDetail.route,
            arguments = listOf(navArgument("matchId") { type = NavType.LongType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getLong("matchId") ?: -1L
            MatchDetailScreen(navController = navController, matchId = matchId)
        }
        
        composable(Screen.Statistics.route) {
            StatisticsScreen(navController = navController)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}
