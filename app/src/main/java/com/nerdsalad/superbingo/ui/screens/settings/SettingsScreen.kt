package com.nerdsalad.superbingo.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nerdsalad.superbingo.ui.components.BannerAdView
import com.nerdsalad.superbingo.ui.theme.SuperBingoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            BannerAdView()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(
                title = "Premium",
                badgeText = "Coming Soon"
            ) {
                SettingsItem(
                    title = "Cloud Backup",
                    icon = Icons.Outlined.Cloud,
                    showLock = true,
                    badge = "Premium",
                    isPremium = true,
                    onClick = { /* TODO: Implement Premium */ }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                SettingsItem(
                    title = "Voice Announcements",
                    icon = Icons.Outlined.VolumeUp,
                    badge = "Coming Soon",
                    isPremium = false,
                    onClick = { /* TODO: Implement Voice Announcements */ }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                SettingsItem(
                    title = "Remove Ads",
                    icon = Icons.Outlined.Block,
                    showLock = true,
                    badge = "Premium",
                    isPremium = true,
                    onClick = { /* TODO: Implement Remove Ads */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = "About") {
                SettingsItem(
                    title = "App Version",
                    icon = Icons.Outlined.Info,
                    trailingText = "1.0.0",
                    showChevron = false
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                SettingsItem(
                    title = "Terms and Conditions",
                    icon = Icons.Outlined.Gavel,
                    onClick = {
                        uriHandler.openUri("https://sites.google.com/view/superbingo-privacypolicy/terms-and-conditions")
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                SettingsItem(
                    title = "Privacy Policy",
                    icon = Icons.Outlined.Description,
                    onClick = {
                        uriHandler.openUri("https://sites.google.com/view/superbingo-privacypolicy/privacy-policy")
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SuperBingoTheme {
        SettingsScreen(navController = NavController(LocalContext.current))
    }
}
