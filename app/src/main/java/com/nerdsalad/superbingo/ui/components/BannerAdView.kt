package com.nerdsalad.superbingo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadResult
import com.nerdsalad.superbingo.util.Constants
import com.nerdsalad.superbingo.util.findActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BannerAdView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context.findActivity()
    var bannerAdState by remember { mutableStateOf<BannerAd?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val isPreviewMode = LocalInspectionMode.current

    LaunchedEffect(Unit) {
        if (!isPreviewMode && activity != null) {
            // Wait for initialization if necessary
            while (!MobileAds.isInitialized) {
                delay(100)
            }

            val adSize = AdSize.getLargeAnchoredAdaptiveBannerAdSize(activity, 360)
            val adRequest = BannerAdRequest.Builder(Constants.BANNER_AD_UNIT_ID, adSize).build()
            coroutineScope.launch {
                when (val result = BannerAd.load(adRequest)) {
                    is AdLoadResult.Success -> {
                        bannerAdState = result.ad
                    }
                    is AdLoadResult.Failure -> {
                        // Handle failure if needed
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            bannerAdState?.destroy()
        }
    }

    bannerAdState?.let { bannerAd ->
        if (activity != null) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { ctx ->
                        AdView(ctx).apply {
                            registerBannerAd(bannerAd, activity)
                        }
                    },
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
    }
}
