package com.nerdsalad.superbingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.nerdsalad.superbingo.navigation.AppNavigation
import com.nerdsalad.superbingo.ui.theme.SuperBingoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperBingoTheme {
                val navController = rememberNavController()
                Scaffold() {paddingValues ->
                    AppNavigation(navController = navController,paddingValues)
                }
            }
        }
    }
}
