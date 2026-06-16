package com.nerdsaladstudios.superbingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.nerdsaladstudios.superbingo.navigation.AppNavigation
import com.nerdsaladstudios.superbingo.ui.theme.SuperBingoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperBingoTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}
