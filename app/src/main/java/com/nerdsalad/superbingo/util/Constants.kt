package com.nerdsalad.superbingo.util

import com.nerdsalad.superbingo.BuildConfig

object Constants {
    const val DEFAULT_PRIZE_CATEGORIES = "Laddu,Corner,BP,First Line,Second Line,Third Line,House"
    val DEFAULT_CATEGORY_NAMES = listOf("Laddu", "Corner", "BP", "First Line", "Second Line", "Third Line", "House")
    const val MAX_TAMBOLA_NUMBER = 90
    const val MIN_TAMBOLA_NUMBER = 1
    const val RECENT_NUMBERS_COUNT = 8
    const val RESTART_COUNTDOWN_SECONDS = 2

    val BANNER_AD_UNIT_ID = BuildConfig.BANNER_AD_UNIT_ID
    val INTERSTITIAL_AD_UNIT_ID = BuildConfig.INTERSTITIAL_AD_UNIT_ID
    val APP_ID = BuildConfig.ADMOB_APP_ID
}
