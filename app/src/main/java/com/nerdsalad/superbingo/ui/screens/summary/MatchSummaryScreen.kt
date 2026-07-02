package com.nerdsalad.superbingo.ui.screens.summary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdPreloader
import com.nerdsalad.superbingo.navigation.Screen
import com.nerdsalad.superbingo.ui.components.BannerAdView
import com.nerdsalad.superbingo.ui.components.BingoPrimaryButton
import com.nerdsalad.superbingo.ui.components.WinnerCard
import com.nerdsalad.superbingo.ui.theme.DeepNavy
import com.nerdsalad.superbingo.ui.theme.SaffronOrange
import com.nerdsalad.superbingo.ui.theme.TextMuted
import com.nerdsalad.superbingo.util.Constants
import com.nerdsalad.superbingo.util.findActivity
import com.nerdsalad.superbingo.util.toFormattedDate
import com.nerdsalad.superbingo.util.toFormattedTime
import com.nerdsalad.superbingo.util.toRupees

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchSummaryScreen(
    navController: NavController,
    matchId: Long,
    viewModel: MatchSummaryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context.findActivity()

    LaunchedEffect(matchId) {
        viewModel.loadMatchDetail(matchId)
    }

    LaunchedEffect(state.matchDetail) {
        if (state.matchDetail != null && activity != null) {
            val interstitialAd = InterstitialAdPreloader.pollAd(Constants.INTERSTITIAL_AD_UNIT_ID)
            interstitialAd?.show(activity)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Game Summary 🎉",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            BannerAdView()
        }
    ) { padding ->
        state.matchDetail?.let { detail ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "GAME OVER!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = SaffronOrange
                        )
                        Text(
                            text = "${detail.match.createdAt.toFormattedDate()} at ${detail.match.createdAt.toFormattedTime()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            SummaryStatItem(label = "Total Pot", value = detail.match.totalPotAmount.toRupees())
                            SummaryStatItem(label = "Players", value = detail.match.playerIds.size.toString())
                            SummaryStatItem(label = "Numbers", value = "${detail.calledNumbers.size}")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "🏆 Winners",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.fillMaxWidth(),
                    color = DeepNavy
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (detail.winners.isEmpty()) {
                    Text(
                        text = "No winners recorded for this game.",
                        modifier = Modifier.padding(vertical = 32.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                } else {
                    detail.winners.forEach { winner ->
                        WinnerCard(winner = winner)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                BingoPrimaryButton(
                    text = "Back to Home",
                    onClick = { 
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { navController.navigate(Screen.MatchHistory.route) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("View Game History", fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SummaryStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = DeepNavy
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
    }
}
