# Implement Banner Ad in HomeScreen

This plan outlines the steps to implement a banner ad at the bottom of the `HomeScreen` using the GMA Next-Gen SDK.

## Proposed Changes

### [Super Bingo App]

#### [Extensions.kt](file:///K:/InternProjects/SuperBingo/app/src/main/java/com/nerdsaladstudios/superbingo/util/Extensions.kt)

- Add a helper function to find the `Activity` from a `Context`. This is required by `BannerAd.getView(activity)`.

```kotlin
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
```

#### [HomeScreen.kt](file:///K:/InternProjects/SuperBingo/app/src/main/java/com/nerdsaladstudios/superbingo/ui/screens/home/HomeScreen.kt)

- Initialize `bannerAdState` using `remember { mutableStateOf<BannerAd?>(null) }`.
- Use `LaunchedEffect` to load the banner ad when the screen is active.
- Use `DisposableEffect` to destroy the banner ad when the screen is disposed.
- Update the `bottomBar` of the `Scaffold` to display the `BannerAd` using `AndroidView`.

```kotlin
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context.findActivity()
    var bannerAdState by remember { mutableStateOf<BannerAd?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val isPreviewMode = LocalInspectionMode.current

    LaunchedEffect(Unit) {
        if (!isPreviewMode && activity != null) {
            val adSize = AdSize.getLargeAnchoredAdaptiveBannerAdSize(activity, 360)
            val adRequest = BannerAdRequest.Builder("ca-app-pub-3940256099942544/9214589741", adSize).build()
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

    Scaffold(
        // ...
        bottomBar = {
            bannerAdState?.let { bannerAd ->
                if (activity != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                        contentAlignment = Alignment.Center
                    ) {
                        AndroidView(
                            factory = { bannerAd.getView(activity) },
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }
            }
        }
    ) {
        // ...
    }
}
```

## Verification Plan

### Automated Tests
- I will run `gradle_build` to ensure the project still compiles correctly.

### Manual Verification
- I will deploy the app to the device and verify that the banner ad is displayed at the bottom of the `HomeScreen`.
- I will use `take_screenshot` and `ui_state` to confirm the presence of the ad.
