package com.nerdsalad.superbingo

import android.app.Application
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.PreloadConfiguration
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdPreloader
import com.nerdsalad.superbingo.util.Constants
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class SuperBingoApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            MobileAds.initialize(
                this@SuperBingoApplication,
                InitializationConfig.Builder(Constants.APP_ID).build()
            ) {
                // Initialize Interstitial preloading after SDK is ready
                val adRequest = AdRequest.Builder(Constants.INTERSTITIAL_AD_UNIT_ID).build()
                val preloadConfig = PreloadConfiguration(adRequest)
                InterstitialAdPreloader.start(Constants.INTERSTITIAL_AD_UNIT_ID, preloadConfig)
            }
        }
    }
}
