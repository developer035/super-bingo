# SUPER BINGO — TAMBOLA/HOUSIE COMPANION APP
## Complete AI Implementation Blueprint for Android Studio Agent

> **AGENT INSTRUCTIONS**: You are building a production-ready Android app called **Super Bingo**.
> Read every section completely before writing a single line of code.
> Follow phases in strict order. Do not skip. Do not improvise architecture.
> Every class name, package name, and file path in this document is AUTHORITATIVE. Use them exactly.

---

---

# SECTION 1 — PRODUCT DEFINITION

## 1.1 Purpose

Super Bingo is a **family-friendly Tambola/Housie companion app** for a single Android phone.

One person (the **host**) runs the app. The phone may be connected to a Bluetooth speaker.
The host presses a button. The app calls the next number. Players mark their physical paper tickets.
When a player wins a prize, the host manually records the winner. The app does NOT verify tickets.

This app is NOT gambling software. It is a household game companion.

---

## 1.2 User Personas

**Primary — The Host**
- Adult, 25–60 years old
- Runs the game for the family
- Needs large buttons, clear numbers, undo support
- May be distracted by children/family while hosting

**Secondary — Spectators / Players (indirect)**
- Players do NOT use the phone
- Players shout out prize claims verbally
- Host records winners manually

---

## 1.3 Core User Flows

### Flow 1 — First Time Setup
App Install → Add Players → Start New Match → Play Game

### Flow 2 — Game Session
Home → New Match → Select Players → Set Pot & Prizes → Live Match Screen
→ Call Numbers → Record Winners → End Game → View Summary → (auto-saved to History)

### Flow 3 — Between Sessions
Home → Match History → View Past Match Details

### Flow 4 — Statistics
Home → Statistics → View Per-Player Stats

---

## 1.4 MVP Scope (Build This)

- Player management (add, edit, delete)
- New match setup (pot, players, prize categories)
- Live game screen (call numbers, board, history, undo)
- Winner recording per prize category
- Match summary screen
- Match history (persistent, browsable)
- Per-player statistics
- Settings screen (placeholder)

---

## 1.5 Future Scope (DO NOT BUILD NOW — Only design extension points)

- Text-to-speech number announcement
- Sound effects and jingles
- Firebase cloud sync
- Google Sign-In
- Multiplayer (remote players join via code)
- Cloud backup of match history

---

---

# SECTION 2 — ARCHITECTURE DECISIONS

## 2.1 Why MVVM

- Clean separation of UI logic from business logic
- ViewModels survive configuration changes (screen rotation)
- Pairs perfectly with Jetpack Compose and StateFlow
- Standard on Android, well-understood, easy to maintain solo

## 2.2 Why Room

- Permanent persistence of players, matches, winners, stats
- Type-safe SQL queries via DAOs
- No network dependency
- Easy migrations for future updates

## 2.3 Why Hilt

- Dependency injection with minimal boilerplate
- Pairs with ViewModel via `@HiltViewModel`
- No manual factory classes needed
- Single annotation setup

## 2.4 Why StateFlow

- Reactive, Compose-friendly
- Replaces LiveData (modern approach)
- Works naturally with coroutines
- Immutable UI state pattern prevents bugs

## 2.5 Why Jetpack Compose

- Single toolkit, no XML
- Material 3 out of the box
- Animations built-in
- Faster UI development for solo developer

## 2.6 Why Single-Module

- Solo developer
- Faster build times
- Simpler dependency management
- No multi-module overhead

## 2.7 Why Single Activity

- Navigation Compose requires it
- Simpler lifecycle management
- Back stack handled by NavController

---

## 2.8 Architecture Layer Summary

```
UI Layer       → Compose Screens + ViewModels (ui/)
Domain Layer   → Pure Kotlin models + game logic (domain/)
Data Layer     → Room DAOs + Repositories (data/)
DI Layer       → Hilt modules (di/)
```

---

---

# SECTION 3 — PROJECT STRUCTURE

```
com.superbingo/
├── MainActivity.kt                     ← Single Activity, sets up NavHost
│
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt              ← Room database singleton
│   │   ├── dao/
│   │   │   ├── PlayerDao.kt
│   │   │   ├── MatchDao.kt
│   │   │   ├── PrizeCategoryDao.kt
│   │   │   ├── WinnerDao.kt
│   │   │   └── CalledNumberDao.kt
│   │   ├── entity/
│   │   │   ├── PlayerEntity.kt
│   │   │   ├── MatchEntity.kt
│   │   │   ├── PrizeCategoryEntity.kt
│   │   │   ├── WinnerEntity.kt
│   │   │   └── CalledNumberEntity.kt
│   │   └── converter/
│   │       └── Converters.kt           ← TypeConverters for Room
│   │
│   └── repository/
│       ├── PlayerRepository.kt
│       ├── MatchRepository.kt
│       └── StatisticsRepository.kt
│
├── domain/
│   └── model/
│       ├── Player.kt                   ← Pure Kotlin data class
│       ├── Match.kt
│       ├── PrizeCategory.kt
│       ├── Winner.kt
│       ├── CalledNumber.kt
│       └── MatchDetail.kt             ← Aggregated match view model
│
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   │
│   ├── components/
│   │   ├── NumberBoard.kt             ← 9x10 grid of numbers 1-90
│   │   ├── NumberCell.kt              ← Single number cell with color state
│   │   ├── RecentNumbersRow.kt        ← Horizontal scrollable recent calls
│   │   ├── PrizeCategoryRow.kt        ← Single prize row in setup
│   │   ├── PlayerCheckboxRow.kt       ← Player selection row
│   │   ├── WinnerCard.kt              ← Winner display card
│   │   ├── StatCard.kt                ← Statistics display card
│   │   ├── MatchHistoryCard.kt        ← History list item card
│   │   ├── ConfirmationDialog.kt      ← Generic confirm dialog
│   │   ├── CountdownConfirmDialog.kt  ← Restart dialog with countdown
│   │   └── EmptyStateView.kt          ← Empty list placeholder
│   │
│   └── screens/
│       ├── home/
│       │   ├── HomeScreen.kt
│       │   └── HomeViewModel.kt
│       │
│       ├── player/
│       │   ├── PlayerManagementScreen.kt
│       │   └── PlayerManagementViewModel.kt
│       │
│       ├── newmatch/
│       │   ├── NewMatchScreen.kt
│       │   └── NewMatchViewModel.kt
│       │
│       ├── livematch/
│       │   ├── LiveMatchScreen.kt
│       │   └── LiveMatchViewModel.kt
│       │
│       ├── winner/
│       │   ├── RecordWinnerDialog.kt
│       │   └── (owned by LiveMatchViewModel)
│       │
│       ├── summary/
│       │   ├── MatchSummaryScreen.kt
│       │   └── MatchSummaryViewModel.kt
│       │
│       ├── history/
│       │   ├── MatchHistoryScreen.kt
│       │   ├── MatchHistoryViewModel.kt
│       │   ├── MatchDetailScreen.kt
│       │   └── MatchDetailViewModel.kt
│       │
│       ├── statistics/
│       │   ├── StatisticsScreen.kt
│       │   └── StatisticsViewModel.kt
│       │
│       └── settings/
│           ├── SettingsScreen.kt
│           └── SettingsViewModel.kt
│
├── navigation/
│   ├── AppNavigation.kt               ← NavHost + all routes
│   └── Screen.kt                      ← Sealed class of all route definitions
│
├── di/
│   ├── AppModule.kt                   ← Hilt module: Database + Repos
│   └── DatabaseModule.kt             ← Hilt module: Room DB provision
│
├── util/
│   ├── GameEngine.kt                  ← Number generation logic
│   ├── NumberAnnouncer.kt             ← Future TTS abstraction (interface only)
│   ├── SoundEffectPlayer.kt           ← Future sound abstraction (interface only)
│   ├── Extensions.kt                  ← Kotlin extension functions
│   └── Constants.kt                   ← App-wide constants
│
└── SuperBingoApplication.kt           ← @HiltAndroidApp
```

---

---

# SECTION 4 — DATABASE DESIGN

## 4.1 Entity: PlayerEntity

**Table name**: `players`

| Column       | Type    | Constraints             |
|--------------|---------|-------------------------|
| id           | Long    | PRIMARY KEY AUTOINCREMENT |
| name         | String  | NOT NULL, UNIQUE         |
| createdAt    | Long    | NOT NULL (epoch millis)  |

**Purpose**: Persistent list of all players ever added.

---

## 4.2 Entity: MatchEntity

**Table name**: `matches`

| Column           | Type    | Constraints                   |
|------------------|---------|-------------------------------|
| id               | Long    | PRIMARY KEY AUTOINCREMENT     |
| createdAt        | Long    | NOT NULL (epoch millis)       |
| totalPotAmount   | Double  | NOT NULL                      |
| playerIds        | String  | NOT NULL (comma-separated IDs)|
| isCompleted      | Boolean | DEFAULT false                 |
| completedAt      | Long    | NULLABLE                      |

**Note**: `playerIds` is stored as `"1,2,3"` and decoded with a TypeConverter.
Index on `createdAt DESC` for history ordering.

---

## 4.3 Entity: PrizeCategoryEntity

**Table name**: `prize_categories`

| Column      | Type    | Constraints               |
|-------------|---------|---------------------------|
| id          | Long    | PRIMARY KEY AUTOINCREMENT |
| matchId     | Long    | NOT NULL, FK → matches.id |
| name        | String  | NOT NULL                  |
| amount      | Double  | NOT NULL                  |
| sortOrder   | Int     | NOT NULL DEFAULT 0        |

**Index**: `matchId`

---

## 4.4 Entity: WinnerEntity

**Table name**: `winners`

| Column          | Type    | Constraints                       |
|-----------------|---------|-----------------------------------|
| id              | Long    | PRIMARY KEY AUTOINCREMENT         |
| matchId         | Long    | NOT NULL, FK → matches.id         |
| prizeCategoryId | Long    | NOT NULL, FK → prize_categories.id|
| playerId        | Long    | NOT NULL, FK → players.id         |
| prizeAmount     | Double  | NOT NULL                          |
| recordedAt      | Long    | NOT NULL (epoch millis)           |

**Constraint**: `UNIQUE(matchId, prizeCategoryId)` — one winner per prize per match (enforced in repo)
**Index**: `matchId`, `playerId`

---

## 4.5 Entity: CalledNumberEntity

**Table name**: `called_numbers`

| Column     | Type  | Constraints               |
|------------|-------|---------------------------|
| id         | Long  | PRIMARY KEY AUTOINCREMENT |
| matchId    | Long  | NOT NULL, FK → matches.id |
| number     | Int   | NOT NULL (1–90)           |
| calledAt   | Long  | NOT NULL (epoch millis)   |
| callOrder  | Int   | NOT NULL                  |

**Index**: `matchId`
**Constraint**: `UNIQUE(matchId, number)` — no duplicates per match

---

## 4.6 ERD Description

```
players (1) ──< (M) winners
matches (1) ──< (M) prize_categories
matches (1) ──< (M) winners
matches (1) ──< (M) called_numbers
prize_categories (1) ──< (M) winners
players (M) >──< (M) matches  [via playerIds column in matches]
```

---

## 4.7 TypeConverters Required

File: `data/local/converter/Converters.kt`

```kotlin
// Convert List<Long> <-> String (comma-separated)
@TypeConverter fun fromLongList(list: List<Long>): String
@TypeConverter fun toLongList(value: String): List<Long>
```

---

---

# SECTION 5 — REPOSITORY DESIGN

## 5.1 PlayerRepository

**File**: `data/repository/PlayerRepository.kt`

**Responsibilities**:
- CRUD operations for players
- Provide reactive list of all players (Flow)

**Public Methods**:
```
getAllPlayers(): Flow<List<Player>>
getPlayerById(id: Long): Player?
insertPlayer(name: String): Long
updatePlayer(id: Long, name: String)
deletePlayer(id: Long)
getPlayersByIds(ids: List<Long>): List<Player>
```

**Data Source**: `PlayerDao`

**Notes**:
- `getAllPlayers()` returns `Flow` (reactive, updates UI automatically)
- Name validation (non-empty, no duplicates) happens here
- Map `PlayerEntity` → `Player` domain model here

---

## 5.2 MatchRepository

**File**: `data/repository/MatchRepository.kt`

**Responsibilities**:
- Create and manage matches
- Manage prize categories per match
- Manage called numbers per match
- Manage winners per match
- Provide match history

**Public Methods**:
```
createMatch(potAmount: Double, playerIds: List<Long>, prizeCategories: List<PrizeCategory>): Long
getAllMatches(): Flow<List<Match>>
getMatchById(id: Long): Match?
getMatchDetail(id: Long): MatchDetail  ← aggregates match + players + categories + winners + numbers
completeMatch(id: Long)
addCalledNumber(matchId: Long, number: Int, callOrder: Int)
removeLastCalledNumber(matchId: Long): Int?  ← returns removed number
getCalledNumbers(matchId: Long): List<CalledNumber>
recordWinner(matchId: Long, prizeCategoryId: Long, playerId: Long, prizeAmount: Double)
getWinnersForMatch(matchId: Long): List<Winner>
getPrizeCategoriesForMatch(matchId: Long): List<PrizeCategory>
deleteMatch(id: Long)
```

**Data Sources**: `MatchDao`, `PrizeCategoryDao`, `WinnerDao`, `CalledNumberDao`

---

## 5.3 StatisticsRepository

**File**: `data/repository/StatisticsRepository.kt`

**Responsibilities**:
- Compute per-player statistics from stored match/winner data
- No separate statistics table — always computed from source data

**Public Methods**:
```
getPlayerStatistics(playerId: Long): PlayerStatistics
getAllPlayerStatistics(): Flow<List<PlayerStatistics>>
```

**Data Sources**: `MatchDao`, `WinnerDao`, `PrizeCategoryDao`, `PlayerDao`

**Notes**:
- Statistics are always derived, never stored separately
- This avoids sync issues

---

---

# SECTION 6 — NAVIGATION DESIGN

## 6.1 Route Definitions

File: `navigation/Screen.kt`

```kotlin
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
```

---

## 6.2 Navigation Graph

File: `navigation/AppNavigation.kt`

```
NavHost(startDestination = Screen.Home.route) {
    composable(Screen.Home.route) { HomeScreen(...) }
    composable(Screen.PlayerManagement.route) { PlayerManagementScreen(...) }
    composable(Screen.NewMatch.route) { NewMatchScreen(...) }
    composable(Screen.LiveMatch.route) { LiveMatchScreen(..., matchId) }
    composable(Screen.MatchSummary.route) { MatchSummaryScreen(..., matchId) }
    composable(Screen.MatchHistory.route) { MatchHistoryScreen(...) }
    composable(Screen.MatchDetail.route) { MatchDetailScreen(..., matchId) }
    composable(Screen.Statistics.route) { StatisticsScreen(...) }
    composable(Screen.Settings.route) { SettingsScreen(...) }
}
```

---

## 6.3 Screen Navigation Table

| Screen              | Entry From           | Exits To                          | Parameters        |
|---------------------|----------------------|-----------------------------------|-------------------|
| Home                | App Launch           | All screens                       | None              |
| PlayerManagement    | Home                 | Home (back)                       | None              |
| NewMatch            | Home                 | LiveMatch (on create)             | None              |
| LiveMatch           | NewMatch             | MatchSummary (end game)           | matchId: Long     |
| MatchSummary        | LiveMatch            | Home (done), History (view)       | matchId: Long     |
| MatchHistory        | Home                 | MatchDetail                       | None              |
| MatchDetail         | MatchHistory         | MatchHistory (back)               | matchId: Long     |
| Statistics          | Home                 | Home (back)                       | None              |
| Settings            | Home                 | Home (back)                       | None              |

---

## 6.4 Back Stack Rules

- `LiveMatch` → Back press → Show "Are you sure you want to exit the match?" dialog
- `MatchSummary` → Back press → Go to Home (NOT LiveMatch — match is over)
- `NewMatch` → Back press → Go to Home (discard setup)

**IMPORTANT**: When navigating from `LiveMatch` to `MatchSummary`, use:
```kotlin
navController.navigate(Screen.MatchSummary.createRoute(matchId)) {
    popUpTo(Screen.Home.route) { inclusive = false }
}
```

---

---

# SECTION 7 — UI SCREEN SPECIFICATIONS

## 7.1 Home Screen

**File**: `ui/screens/home/HomeScreen.kt`

**Layout**:
- App name "Super Bingo 🎱" at top (large, bold, colorful)
- Tagline "Tambola · Housie · Family Fun" (subtitle)
- 5 large clickable cards arranged in a 2-column grid + 1 full-width at bottom:
  - 🎮 Start New Match (full-width, primary color, prominent)
  - 📋 Match History
  - 👥 Players
  - 📊 Statistics
  - ⚙️ Settings

**Empty States**: Not applicable (home always shows cards)

**Actions**:
- Tap any card → navigate to corresponding screen

**ViewModel**: `HomeViewModel` (minimal, just nav triggers)

---

## 7.2 Player Management Screen

**File**: `ui/screens/player/PlayerManagementScreen.kt`

**Layout**:
- Top bar: "Players" + Back button
- FAB (bottom right): "+" Add Player
- LazyColumn: list of player cards
  - Each card: Player name | Edit icon | Delete icon

**Add/Edit Player**:
- Show `AlertDialog` with TextField for player name
- Validate: non-empty, unique name
- Save on confirm

**Delete Player**:
- Show `ConfirmationDialog`: "Delete [Name]? This won't affect past match history."
- On confirm: delete

**Empty State**:
- Show `EmptyStateView`: "No players yet. Tap + to add your first player!"

**Error States**:
- Duplicate name: show inline error in TextField "Player name already exists"
- Empty name: "Name cannot be empty"

---

## 7.3 New Match Screen

**File**: `ui/screens/newmatch/NewMatchScreen.kt`

**Layout** (scrollable Column):

**Section 1 — Pot Amount**
- Label: "Total Pot Amount (₹)"
- OutlinedTextField, numeric keyboard, prefix "₹"
- Validation: must be > 0

**Section 2 — Select Players**
- Label: "Select Players" + auto-count badge "(3 selected)"
- LazyColumn of `PlayerCheckboxRow` for each stored player
- Each row: Checkbox + Player Name
- If no players exist: "No players found. Please add players first." + button to go to Player Management

**Section 3 — Prize Categories**
- Label: "Prize Categories"
- Default categories pre-populated:
  - Laddu, Corner, BP, First Line, Second Line, Third Line, House
  - Each with a default amount of ₹0 (host fills in)
- Each category row: category name | amount field | delete icon
- "Add Category" button at bottom of section
- Tap category name to rename it

**Bottom Button**:
- "Start Match" button (full-width)
- Disabled if: no players selected OR pot amount is empty/zero
- On tap: create match in DB → navigate to LiveMatch with matchId

**Empty/Error States**:
- No players: show message + nav button
- Validation errors: show snackbar

---

## 7.4 Live Match Screen

**File**: `ui/screens/livematch/LiveMatchScreen.kt`

**Layout** (vertical, scrollable if needed):

**Top Section**:
- Row: Match info (date, pot amount) | Undo button (icon) | Menu (⋮ for Restart/End)

**Current Number Display**:
- Massive number display (center, 120sp+, Red/Primary color)
- Label below: "Current Number"
- If no number called yet: Show "Press button to start!" placeholder

**Call Next Number Button**:
- Large, full-width, Primary color
- Label: "CALL NEXT NUMBER 🎱"
- Disabled when all 90 numbers called

**Recently Called Numbers**:
- Label: "Recently Called"
- Horizontal `LazyRow`, newest on left
- Shows last 8–10 numbers as chips/badges
- Color: Slightly muted (not current)

**Number Board**:
- 9×10 grid (or 10×9) of numbers 1–90
- Each cell colored based on state:
  - Not Called: Light Gray / Surface
  - Previously Called: Green
  - Current: Red (pulsing animation)

**Prize Claims Section**:
- Label: "Prize Claims"
- For each prize category: row with name + amount + "Claim" button
- If claimed: show winner name instead + checkmark
- Unclaimed categories show "Claim" button
- Tap "Claim" → opens `RecordWinnerDialog`

**End Game Button** (bottom):
- "End Match" button (outlined, secondary)
- Tap → ConfirmationDialog → navigate to MatchSummary

**Undo**:
- Undo button removes last called number
- Show snackbar: "Undone: [number]"

**Restart**:
- In overflow menu → "Restart Match"
- Opens `CountdownConfirmDialog`

---

## 7.5 Record Winner Dialog

**File**: `ui/screens/winner/RecordWinnerDialog.kt`

**Type**: `AlertDialog` (shown over LiveMatch)

**Layout**:
- Title: "Record Winner — [Category Name]"
- Prize amount label: "Prize: ₹[amount]"
- Dropdown: Select Winner
  - Shows only players selected for current match
  - Default: "Select a player"
- Cancel button
- Save button (disabled until player selected)

**Behavior**:
- On Save: record winner → update UI state → close dialog

---

## 7.6 Match Summary Screen

**File**: `ui/screens/summary/MatchSummaryScreen.kt`

**Layout**:
- Title: "Match Summary 🎉"
- Card: Date | Time | Total Pot | Players Count
- Section "Winners":
  - For each prize category:
    - Category name | Amount
    - Winner name (or "No winner recorded" in muted text)
- Section "Numbers Called": "X / 90 numbers called"
- Button: "Back to Home" (full-width)
- Button: "View in History" (outlined)

**Empty/Edge States**:
- Match with no winners: show all categories with "—"

---

## 7.7 Match History Screen

**File**: `ui/screens/history/MatchHistoryScreen.kt`

**Layout**:
- Top bar: "Match History"
- LazyColumn of `MatchHistoryCard`:
  - Date + Time
  - Players count
  - Pot amount
  - "X winners" badge
  - Tap → navigate to MatchDetail

**Empty State**:
- "No matches yet. Start your first game!"

---

## 7.8 Match Detail Screen

**File**: `ui/screens/history/MatchDetailScreen.kt`

**Layout**:
- Same as MatchSummary but read-only
- Shows: date, players, pot, all prize categories with winners, called numbers (expandable)

---

## 7.9 Statistics Screen

**File**: `ui/screens/statistics/StatisticsScreen.kt`

**Layout**:
- Top bar: "Statistics"
- LazyColumn of player stat cards
- Each `StatCard` shows:
  - Player Name (large)
  - 🎮 Games Played
  - 🏆 Total Wins
  - 💰 Total Prize Money Won
  - 🏁 Houses Won
  - 📏 Lines Won (First + Second + Third combined)
  - 🔲 Corners Won
  - 📅 Last Win Date (or "Never won yet")

**Empty State**:
- "Play some matches to see statistics here!"

---

## 7.10 Settings Screen

**File**: `ui/screens/settings/SettingsScreen.kt`

**Layout** (placeholder for now):
- Top bar: "Settings"
- Coming Soon items (grayed out):
  - Voice Announcements (toggle, disabled)
  - Sound Effects (toggle, disabled)
  - Cloud Backup (disabled)
- App version at bottom

---

---

# SECTION 8 — STATE MANAGEMENT

## 8.1 State Design Philosophy

- Every screen has exactly ONE `UiState` data class
- ViewModel exposes `StateFlow<UiState>`
- UI collects via `collectAsStateWithLifecycle()`
- One-time events (navigation, snackbars) use `Channel<UiEvent>` → collected as `Flow` in UI

---

## 8.2 UiState Classes

### HomeUiState
```kotlin
data class HomeUiState(
    val isLoading: Boolean = false
)
```

### PlayerManagementUiState
```kotlin
data class PlayerManagementUiState(
    val players: List<Player> = emptyList(),
    val isLoading: Boolean = false,
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val editingPlayer: Player? = null,
    val nameError: String? = null,
    val showDeleteConfirm: Boolean = false,
    val deletingPlayer: Player? = null
)
```

### NewMatchUiState
```kotlin
data class NewMatchUiState(
    val potAmount: String = "",
    val allPlayers: List<Player> = emptyList(),
    val selectedPlayerIds: Set<Long> = emptySet(),
    val prizeCategories: List<PrizeCategory> = defaultCategories(),
    val isLoading: Boolean = false,
    val potAmountError: String? = null,
    val isStartEnabled: Boolean = false
)
```

### LiveMatchUiState
```kotlin
data class LiveMatchUiState(
    val matchId: Long = -1,
    val currentNumber: Int? = null,
    val calledNumbers: List<Int> = emptyList(),
    val recentNumbers: List<Int> = emptyList(),        // last 8, newest first
    val remainingCount: Int = 90,
    val isGameComplete: Boolean = false,
    val prizeCategories: List<PrizeCategory> = emptyList(),
    val winners: List<Winner> = emptyList(),
    val players: List<Player> = emptyList(),
    val showRecordWinnerDialog: Boolean = false,
    val selectedCategoryForWinner: PrizeCategory? = null,
    val showRestartDialog: Boolean = false,
    val showExitConfirm: Boolean = false,
    val isLoading: Boolean = false
)
```

### MatchSummaryUiState
```kotlin
data class MatchSummaryUiState(
    val matchDetail: MatchDetail? = null,
    val isLoading: Boolean = false
)
```

### MatchHistoryUiState
```kotlin
data class MatchHistoryUiState(
    val matches: List<Match> = emptyList(),
    val isLoading: Boolean = false
)
```

### StatisticsUiState
```kotlin
data class StatisticsUiState(
    val playerStats: List<PlayerStatistics> = emptyList(),
    val isLoading: Boolean = false
)
```

---

## 8.3 UiEvent (One-Time Events)

```kotlin
sealed class UiEvent {
    data class NavigateTo(val route: String) : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
    object NavigateBack : UiEvent()
}
```

In ViewModel:
```kotlin
private val _events = Channel<UiEvent>(Channel.BUFFERED)
val events = _events.receiveAsFlow()
```

In Composable:
```kotlin
LaunchedEffect(Unit) {
    viewModel.events.collect { event ->
        when (event) {
            is UiEvent.NavigateTo -> navController.navigate(event.route)
            is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            is UiEvent.NavigateBack -> navController.popBackStack()
        }
    }
}
```

---

## 8.4 Loading States

- All screens show `CircularProgressIndicator` when `isLoading = true`
- Loading triggered at ViewModel init for data fetching
- Operations (save/delete) show loading on the button itself (disable + show spinner)

---

---

# SECTION 9 — GAME ENGINE LOGIC

## 9.1 GameEngine

**File**: `util/GameEngine.kt`

This is a **pure Kotlin class** (no Android dependencies). Fully unit testable.

```kotlin
class GameEngine {
    // State
    private val _calledNumbers = mutableListOf<Int>()
    private val _availableNumbers = (1..90).toMutableList()

    // Public state accessors
    val calledNumbers: List<Int> get() = _calledNumbers.toList()
    val currentNumber: Int? get() = _calledNumbers.lastOrNull()
    val remainingCount: Int get() = _availableNumbers.size
    val isGameComplete: Boolean get() = _availableNumbers.isEmpty()

    // Core operations
    fun callNextNumber(): Int? { ... }
    fun undoLastNumber(): Int? { ... }
    fun restoreState(called: List<Int>) { ... }  // for loading saved match
    fun reset() { ... }
}
```

### callNextNumber() Logic:
1. If `_availableNumbers` is empty → return null
2. Pick random index from `_availableNumbers`
3. Remove from `_availableNumbers`
4. Add to end of `_calledNumbers`
5. Return the number

### undoLastNumber() Logic:
1. If `_calledNumbers` is empty → return null
2. Remove last from `_calledNumbers`
3. Add it back to `_availableNumbers`
4. Sort `_availableNumbers` (keep it ordered, doesn't matter functionally)
5. Return the removed number

### restoreState() Logic:
1. Called when LiveMatch screen loads existing match
2. `_calledNumbers = called.toMutableList()`
3. `_availableNumbers = (1..90).toMutableList().apply { removeAll(called.toSet()) }`

### reset() Logic:
1. `_calledNumbers.clear()`
2. `_availableNumbers = (1..90).toMutableList()`

---

## 9.2 Number Board State

Number board is computed in the ViewModel from `LiveMatchUiState`:

```kotlin
fun getNumberColor(number: Int, state: LiveMatchUiState): NumberState {
    return when {
        number == state.currentNumber -> NumberState.CURRENT
        state.calledNumbers.contains(number) -> NumberState.CALLED
        else -> NumberState.UNCALLED
    }
}

enum class NumberState { UNCALLED, CALLED, CURRENT }
```

**Colors**:
- `UNCALLED`: `MaterialTheme.colorScheme.surfaceVariant`
- `CALLED`: `Color(0xFF4CAF50)` (Green)
- `CURRENT`: `Color(0xFFE53935)` (Red) + pulsing animation

---

## 9.3 Recent Numbers

Recent numbers = `calledNumbers.reversed().take(8)`
Newest first, oldest last.

---

## 9.4 Game Completion

- When `GameEngine.isGameComplete == true`:
  - Disable "Call Next Number" button
  - Show "All 90 numbers called! 🎉" banner
  - "End Match" button becomes primary (more prominent)

---

## 9.5 Undo Edge Cases

- Cannot undo if no numbers called (button disabled)
- After undo: board updates immediately
- After undo: if a winner was claimed for the current number → do NOT auto-remove winner (host handles manually)
- Show snackbar: "↩ [number] returned to pool"

---

## 9.6 Restart Match

- Triggers `GameEngine.reset()`
- Deletes all `CalledNumberEntity` for matchId from DB
- Deletes all `WinnerEntity` for matchId from DB
- Reloads LiveMatch screen state

---

---

# SECTION 10 — PERSISTENCE RULES

## 10.1 Permanently Persisted (survives app restart, device restart)

| Data                    | Storage  | Notes                              |
|-------------------------|----------|------------------------------------|
| All players             | Room     | Never auto-deleted                 |
| All past matches        | Room     | Full history                       |
| All called numbers      | Room     | Per match                          |
| All winners             | Room     | Per match                          |
| All prize categories    | Room     | Per match                          |

## 10.2 Session-Only (lost on app restart)

| Data                         | Notes                                      |
|------------------------------|--------------------------------------------|
| New match form selections    | Re-select on each new game                 |
| Current scroll position      | Not restored                               |
| Dialog open/close state      | Reset on restart                           |

## 10.3 Active Match Recovery

If app is killed during a live match (matchId exists, `isCompleted = false`):
- When user goes to Home → do NOT auto-resume (too complex for MVP)
- Show nothing about in-progress match on Home screen
- User can find it in Match History and view it as-is (incomplete)
- **Future enhancement**: detect incomplete match and offer to resume

---

---

# SECTION 11 — STATISTICS LOGIC

## 11.1 PlayerStatistics Model

```kotlin
data class PlayerStatistics(
    val player: Player,
    val gamesPlayed: Int,
    val totalWins: Int,
    val totalPrizeMoney: Double,
    val cornersWon: Int,
    val linesWon: Int,           // First + Second + Third combined
    val housesWon: Int,
    val laddusWon: Int,
    val bpWon: Int,
    val lastWinDate: Long?       // epoch millis, null if never won
)
```

## 11.2 Calculation Formulas

**Games Played**:
```
Count of matches where player.id is in match.playerIds AND match.isCompleted = true
```

**Total Wins**:
```
Count of WinnerEntity where playerId = player.id
```

**Total Prize Money**:
```
SUM(WinnerEntity.prizeAmount) where playerId = player.id
```

**Houses Won**:
```
Count of WinnerEntity where playerId = player.id
AND prizeCategoryId → PrizeCategoryEntity.name LIKE "%house%"  (case-insensitive)
```

**Lines Won**:
```
Count of WinnerEntity where playerId = player.id
AND prizeCategoryId → PrizeCategoryEntity.name LIKE "%line%"
```

**Corners Won**:
```
Count of WinnerEntity where playerId = player.id
AND prizeCategoryId → PrizeCategoryEntity.name LIKE "%corner%"
```

**Last Win Date**:
```
MAX(WinnerEntity.recordedAt) where playerId = player.id
```

**Edge Cases**:
- Player has never played → all zeros, lastWinDate = null
- Player played but never won → gamesPlayed > 0, wins = 0
- Deleted player: stats shown only for active players (those still in `players` table)

---

---

# SECTION 12 — FUTURE EXTENSION POINTS

## 12.1 NumberAnnouncer Interface

**File**: `util/NumberAnnouncer.kt`

```kotlin
interface NumberAnnouncer {
    fun announce(number: Int)
    fun announceGameComplete()
    fun shutdown()
}

// Future implementation (DO NOT BUILD NOW):
// class TtsNumberAnnouncer(context: Context) : NumberAnnouncer { ... }

// Default no-op implementation (USE THIS NOW):
class NoOpNumberAnnouncer : NumberAnnouncer {
    override fun announce(number: Int) { /* no-op */ }
    override fun announceGameComplete() { /* no-op */ }
    override fun shutdown() { /* no-op */ }
}
```

Inject `NumberAnnouncer` via Hilt into `LiveMatchViewModel`. Future dev just swaps implementation.

---

## 12.2 SoundEffectPlayer Interface

**File**: `util/SoundEffectPlayer.kt`

```kotlin
interface SoundEffectPlayer {
    fun playNumberCalled()
    fun playWinnerRecorded()
    fun playGameComplete()
}

class NoOpSoundEffectPlayer : SoundEffectPlayer {
    override fun playNumberCalled() {}
    override fun playWinnerRecorded() {}
    override fun playGameComplete() {}
}
```

---

## 12.3 CloudSyncService Interface

**File**: `util/CloudSyncService.kt`

```kotlin
interface CloudSyncService {
    suspend fun syncMatches(): Result<Unit>
    suspend fun backupPlayers(): Result<Unit>
    fun isSignedIn(): Boolean
}

class NoOpCloudSyncService : CloudSyncService {
    override suspend fun syncMatches() = Result.success(Unit)
    override suspend fun backupPlayers() = Result.success(Unit)
    override fun isSignedIn() = false
}
```

---

## 12.4 RemotePlayerService Interface (Multiplayer future)

**File**: `util/RemotePlayerService.kt`

```kotlin
interface RemotePlayerService {
    suspend fun createRoom(): String           // returns room code
    suspend fun joinRoom(code: String): Boolean
    suspend fun broadcastNumber(number: Int)
    fun observeRemotePlayers(): Flow<List<String>>
}
```

DO NOT implement. Interface only.

---

---

# SECTION 13 — DEVELOPMENT PHASES

---

## PHASE 1 — Project Setup

**Objective**: Create a working, buildable Android project with correct dependencies.

**Files to Create**:
- `build.gradle.kts` (app level)
- `build.gradle.kts` (project level)
- `libs.versions.toml`
- `SuperBingoApplication.kt`
- `MainActivity.kt`
- `ui/theme/Color.kt`
- `ui/theme/Theme.kt`
- `ui/theme/Type.kt`

**Dependency List** (add to `libs.versions.toml`):

```toml
[versions]
kotlin = "2.0.0"
agp = "8.5.0"
compose-bom = "2024.06.00"
hilt = "2.51.1"
room = "2.6.1"
navigation-compose = "2.7.7"
lifecycle = "2.8.3"
coroutines = "1.8.1"
material3 = "1.2.1"

[libraries]
# Compose BOM
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }
compose-material-icons = { group = "androidx.compose.material", name = "material-icons-extended" }
activity-compose = { group = "androidx.activity", name = "activity-compose", version = "1.9.0" }

# Navigation
navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation-compose" }

# Lifecycle
lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycle" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.2.0" }

# Room
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

# Coroutines
coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }

# Testing
junit = { group = "junit", name = "junit", version = "4.13.2" }
coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutines" }
room-testing = { group = "androidx.room", name = "room-testing", version.ref = "room" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version = "2.0.0-1.0.22" }
```

**Acceptance Criteria**:
- Project builds without errors
- App launches to blank screen
- Hilt installed and working (`@HiltAndroidApp` on Application class)
- Material 3 theme applied

**Definition of Done**: `./gradlew assembleDebug` passes.

---

## PHASE 2 — Database Layer

**Objective**: All Room entities, DAOs, and database class created and compiling.

**Files to Create**:
- `data/local/entity/PlayerEntity.kt`
- `data/local/entity/MatchEntity.kt`
- `data/local/entity/PrizeCategoryEntity.kt`
- `data/local/entity/WinnerEntity.kt`
- `data/local/entity/CalledNumberEntity.kt`
- `data/local/converter/Converters.kt`
- `data/local/dao/PlayerDao.kt`
- `data/local/dao/MatchDao.kt`
- `data/local/dao/PrizeCategoryDao.kt`
- `data/local/dao/WinnerDao.kt`
- `data/local/dao/CalledNumberDao.kt`
- `data/local/AppDatabase.kt`
- `domain/model/Player.kt`
- `domain/model/Match.kt`
- `domain/model/PrizeCategory.kt`
- `domain/model/Winner.kt`
- `domain/model/CalledNumber.kt`
- `domain/model/MatchDetail.kt`
- `domain/model/PlayerStatistics.kt`
- `di/DatabaseModule.kt`

**Acceptance Criteria**:
- All entities defined with correct annotations
- All DAOs compile
- `AppDatabase` includes all entities and DAOs
- `DatabaseModule` provides `AppDatabase` and all DAOs via Hilt
- Project still builds

**Definition of Done**: `./gradlew assembleDebug` passes. No compilation errors.

---

## PHASE 3 — Repositories

**Objective**: All repositories created with full method implementations.

**Files to Create**:
- `data/repository/PlayerRepository.kt`
- `data/repository/MatchRepository.kt`
- `data/repository/StatisticsRepository.kt`
- `di/AppModule.kt`
- `util/GameEngine.kt`
- `util/NumberAnnouncer.kt`
- `util/SoundEffectPlayer.kt`
- `util/CloudSyncService.kt`
- `util/Extensions.kt`
- `util/Constants.kt`

**Acceptance Criteria**:
- All repository methods implemented
- Repositories injected via Hilt (`@Singleton`, `@Inject constructor`)
- `GameEngine` fully implemented with unit test written
- Extension points (interfaces + no-op impls) exist

**Definition of Done**: Unit tests for `GameEngine` pass. Project builds.

---

## PHASE 4 — Navigation

**Objective**: Full navigation graph wired up. All screens navigate correctly (even if screens are empty stubs).

**Files to Create**:
- `navigation/Screen.kt`
- `navigation/AppNavigation.kt`
- Stub composable functions for all 9 screens

**Acceptance Criteria**:
- App launches and shows Home screen stub
- All nav routes defined
- NavController wired in MainActivity
- Back navigation works

**Definition of Done**: Can navigate between all stub screens without crashing.

---

## PHASE 5 — Player Management

**Objective**: Fully working player CRUD.

**Files to Create**:
- `ui/screens/player/PlayerManagementScreen.kt`
- `ui/screens/player/PlayerManagementViewModel.kt`
- `ui/components/PlayerCheckboxRow.kt`
- `ui/components/ConfirmationDialog.kt`
- `ui/components/EmptyStateView.kt`

**Acceptance Criteria**:
- Add player (with validation)
- Edit player name
- Delete player (with confirmation)
- Empty state shows when no players
- Players persist after app restart
- Duplicate name prevented

**Definition of Done**: Full manual test of add/edit/delete flow passes.

---

## PHASE 6 — Match Setup (New Match)

**Objective**: New match can be configured and started.

**Files to Create**:
- `ui/screens/newmatch/NewMatchScreen.kt`
- `ui/screens/newmatch/NewMatchViewModel.kt`
- `ui/components/PrizeCategoryRow.kt`

**Acceptance Criteria**:
- Pot amount entered with ₹ prefix
- Players listed with checkboxes
- Default prize categories shown
- Add/edit/delete categories works
- Player count shown automatically
- "Start Match" creates match in DB
- Navigates to LiveMatch with matchId
- Validation prevents starting with no players or empty pot

**Definition of Done**: Match created in DB. Can verify in DB inspector.

---

## PHASE 7 — Game Engine + Live Match

**Objective**: Fully working game screen — the core feature of the app.

**Files to Create**:
- `ui/screens/livematch/LiveMatchScreen.kt`
- `ui/screens/livematch/LiveMatchViewModel.kt`
- `ui/components/NumberBoard.kt`
- `ui/components/NumberCell.kt`
- `ui/components/RecentNumbersRow.kt`
- `ui/components/CountdownConfirmDialog.kt`

**Acceptance Criteria**:
- Current number shown large and bold
- "Call Next Number" generates random number
- Number board updates (gray/green/red states)
- Recently called numbers show (newest first)
- Called numbers persist to DB
- Undo removes last number (board + DB)
- Restart works with countdown dialog
- After 90 numbers: button disabled
- Back press shows exit confirmation

**Definition of Done**: Full game of 10 numbers played, undone, replayed without bugs.

---

## PHASE 8 — Winner Management

**Objective**: Winners recorded per prize category.

**Files to Create**:
- `ui/screens/winner/RecordWinnerDialog.kt`

**Acceptance Criteria**:
- Each prize category has a "Claim" button
- Clicking "Claim" opens dialog with player dropdown
- Only match players shown in dropdown
- Winner saved to DB
- Category shows winner name after claim
- Duplicate category winner prevention (UI blocks second claim)

**Definition of Done**: Record winner, restart app, winner still shown.

---

## PHASE 9 — Match Summary + History

**Objective**: Users can view completed match summaries and browse history.

**Files to Create**:
- `ui/screens/summary/MatchSummaryScreen.kt`
- `ui/screens/summary/MatchSummaryViewModel.kt`
- `ui/screens/history/MatchHistoryScreen.kt`
- `ui/screens/history/MatchHistoryViewModel.kt`
- `ui/screens/history/MatchDetailScreen.kt`
- `ui/screens/history/MatchDetailViewModel.kt`
- `ui/components/MatchHistoryCard.kt`
- `ui/components/WinnerCard.kt`

**Acceptance Criteria**:
- Ending match navigates to summary
- Summary shows all data correctly
- History shows all past matches
- Tapping history item shows full detail
- Empty state on History screen

**Definition of Done**: Play 2 matches, view both in history with correct data.

---

## PHASE 10 — Statistics

**Objective**: Per-player statistics computed and displayed.

**Files to Create**:
- `ui/screens/statistics/StatisticsScreen.kt`
- `ui/screens/statistics/StatisticsViewModel.kt`
- `ui/components/StatCard.kt`

**Acceptance Criteria**:
- Each player's stats shown
- Games played, wins, money, category breakdown accurate
- Last win date shown
- Empty state before any matches played

**Definition of Done**: Play 3 matches with different winners, verify stats are accurate.

---

## PHASE 11 — Polish

**Objective**: Final UI polish, animations, edge cases, Play Store readiness.

**Tasks**:
- Home screen final design (cards, colors, icons)
- Animations: number call animation (bounce/pulse on current number)
- Number board cell animations (color transitions)
- Settings screen placeholder built
- All empty states styled
- All error states tested
- App icon designed (use Android Studio's Image Asset Studio)
- App name configured in strings.xml
- Minimum SDK / target SDK verified
- ProGuard rules reviewed
- Privacy policy placeholder URL added to Play Store listing

**Acceptance Criteria**:
- App looks polished and family-friendly
- No crashes in any flow
- Back navigation works everywhere
- App icon shows on launcher

**Definition of Done**: Full end-to-end play test of one complete match, no issues.

---

---

# SECTION 14 — ANDROID STUDIO AGENT PROMPT

> **COPY THIS ENTIRE BLOCK AND GIVE IT TO THE ANDROID STUDIO AI AGENT AT THE START OF EACH PHASE.**

---

## MASTER PROMPT (Give this first, then the phase prompt)

```
You are building a production-ready Android app called Super Bingo.

CRITICAL RULES — READ BEFORE WRITING ANY CODE:

1. Package name: com.superbingo
2. Use Kotlin, Jetpack Compose, Material 3, Hilt, Room, Navigation Compose, MVVM, StateFlow.
3. Use SINGLE MODULE architecture. NO multi-module.
4. NEVER use LiveData. ALWAYS use StateFlow.
5. NEVER use XML layouts. ALWAYS use Jetpack Compose.
6. NEVER use findNavController(). ALWAYS use NavController passed as parameter.
7. NEVER hardcode strings in Composables. Use string resources or constants.
8. NEVER write ViewModels without @HiltViewModel annotation.
9. ALL database operations happen on Dispatchers.IO (inside viewModelScope.launch { withContext(Dispatchers.IO) { } }).
10. EVERY Composable screen takes navController: NavController as a parameter.
11. Minimum SDK = 24. Target SDK = 34.
12. Use the EXACT file paths and class names defined in the blueprint.
13. DO NOT add features not listed in this blueprint.
14. DO NOT create abstract base classes unless explicitly listed.
15. DO NOT create UseCase classes. Repositories call DAOs directly.
16. Inject repositories into ViewModels with @Inject constructor.

The project structure is:
app/src/main/java/com/superbingo/
├── data/local/entity/
├── data/local/dao/
├── data/local/converter/
├── data/repository/
├── domain/model/
├── ui/theme/
├── ui/components/
├── ui/screens/home/
├── ui/screens/player/
├── ui/screens/newmatch/
├── ui/screens/livematch/
├── ui/screens/winner/
├── ui/screens/summary/
├── ui/screens/history/
├── ui/screens/statistics/
├── ui/screens/settings/
├── navigation/
├── di/
└── util/
```

---

## PHASE PROMPTS

### Phase 1 Prompt — Project Setup

```
Build Phase 1 of Super Bingo Android app.

TASK: Set up the complete project with all dependencies.

FILE 1: libs.versions.toml
Add these exact dependencies:
- Compose BOM 2024.06.00
- Hilt 2.51.1
- Room 2.6.1
- Navigation Compose 2.7.7
- Lifecycle 2.8.3
- Coroutines 1.8.1
- KSP plugin

FILE 2: app/build.gradle.kts
- applicationId = "com.superbingo"
- minSdk = 24, targetSdk = 34, compileSdk = 34
- Apply plugins: android-application, kotlin-android, kotlin-compose, hilt, ksp
- Add all dependencies from libs.versions.toml

FILE 3: SuperBingoApplication.kt
- Package: com.superbingo
- @HiltAndroidApp class

FILE 4: MainActivity.kt
- @AndroidEntryPoint
- Single activity
- setContent { SuperBingoTheme { /* NavHost goes here in Phase 4 */ } }

FILE 5: ui/theme/Color.kt
- Define SuperBingo brand colors:
  - Primary: Vibrant Purple #7C3AED
  - Secondary: Amber #F59E0B
  - Tertiary: Teal #0D9488
  - Background: White
  - Surface: #F8F7FF
  - Error: #EF4444

FILE 6: ui/theme/Type.kt
- Use Poppins font family if available, else default
- Define titleLarge (32sp, bold), titleMedium (24sp), bodyLarge (18sp), bodyMedium (14sp)

FILE 7: ui/theme/Theme.kt
- SuperBingoTheme composable
- Light theme only for MVP
- Apply Material 3 color scheme from Color.kt

ACCEPTANCE CRITERIA:
- ./gradlew assembleDebug passes with zero errors
- App launches on emulator/device
```

---

### Phase 2 Prompt — Database Layer

```
Build Phase 2 of Super Bingo. Project setup from Phase 1 is complete.

TASK: Create the complete Room database layer.

CREATE THESE FILES IN ORDER:

1. domain/model/Player.kt
data class Player(val id: Long = 0, val name: String, val createdAt: Long = System.currentTimeMillis())

2. domain/model/PrizeCategory.kt
data class PrizeCategory(val id: Long = 0, val matchId: Long = 0, val name: String, val amount: Double, val sortOrder: Int = 0)

3. domain/model/Winner.kt
data class Winner(val id: Long = 0, val matchId: Long, val prizeCategoryId: Long, val playerId: Long, val prizeAmount: Double, val recordedAt: Long = System.currentTimeMillis(), val playerName: String = "", val categoryName: String = "")

4. domain/model/CalledNumber.kt
data class CalledNumber(val id: Long = 0, val matchId: Long, val number: Int, val calledAt: Long = System.currentTimeMillis(), val callOrder: Int)

5. domain/model/Match.kt
data class Match(val id: Long = 0, val createdAt: Long = System.currentTimeMillis(), val totalPotAmount: Double, val playerIds: List<Long>, val isCompleted: Boolean = false, val completedAt: Long? = null)

6. domain/model/MatchDetail.kt
data class MatchDetail(val match: Match, val players: List<Player>, val prizeCategories: List<PrizeCategory>, val winners: List<Winner>, val calledNumbers: List<CalledNumber>)

7. domain/model/PlayerStatistics.kt
data class PlayerStatistics(val player: Player, val gamesPlayed: Int = 0, val totalWins: Int = 0, val totalPrizeMoney: Double = 0.0, val cornersWon: Int = 0, val linesWon: Int = 0, val housesWon: Int = 0, val laddusWon: Int = 0, val bpWon: Int = 0, val lastWinDate: Long? = null)

8. data/local/entity/PlayerEntity.kt
@Entity(tableName = "players", indices = [Index(value = ["name"], unique = true)])
data class PlayerEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String, val createdAt: Long)
Add extension function: fun PlayerEntity.toDomain() = Player(id, name, createdAt)
Add extension function: fun Player.toEntity() = PlayerEntity(id, name, createdAt)

9. data/local/entity/MatchEntity.kt
@Entity(tableName = "matches", indices = [Index("createdAt")])
data class MatchEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val createdAt: Long, val totalPotAmount: Double, val playerIds: String, val isCompleted: Boolean = false, val completedAt: Long? = null)

10. data/local/entity/PrizeCategoryEntity.kt
@Entity(tableName = "prize_categories", indices = [Index("matchId")])
data class PrizeCategoryEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val matchId: Long, val name: String, val amount: Double, val sortOrder: Int)

11. data/local/entity/WinnerEntity.kt
@Entity(tableName = "winners", indices = [Index("matchId"), Index("playerId")])
data class WinnerEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val matchId: Long, val prizeCategoryId: Long, val playerId: Long, val prizeAmount: Double, val recordedAt: Long)

12. data/local/entity/CalledNumberEntity.kt
@Entity(tableName = "called_numbers", indices = [Index("matchId")], uniqueConstraints = [UniqueConstraint(columns = ["matchId", "number"])])
data class CalledNumberEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val matchId: Long, val number: Int, val calledAt: Long, val callOrder: Int)

13. data/local/converter/Converters.kt
@ProvidedTypeConverter class Converters {
  @TypeConverter fun fromLongList(list: List<Long>): String = list.joinToString(",")
  @TypeConverter fun toLongList(value: String): List<Long> = if (value.isBlank()) emptyList() else value.split(",").mapNotNull { it.toLongOrNull() }
}

14. data/local/dao/PlayerDao.kt
@Dao interface PlayerDao {
  @Query("SELECT * FROM players ORDER BY name ASC") fun getAllPlayers(): Flow<List<PlayerEntity>>
  @Query("SELECT * FROM players WHERE id = :id") suspend fun getById(id: Long): PlayerEntity?
  @Query("SELECT * FROM players WHERE id IN (:ids)") suspend fun getByIds(ids: List<Long>): List<PlayerEntity>
  @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(player: PlayerEntity): Long
  @Update suspend fun update(player: PlayerEntity)
  @Delete suspend fun delete(player: PlayerEntity)
}

15. data/local/dao/MatchDao.kt
@Dao interface MatchDao {
  @Query("SELECT * FROM matches ORDER BY createdAt DESC") fun getAllMatches(): Flow<List<MatchEntity>>
  @Query("SELECT * FROM matches WHERE id = :id") suspend fun getById(id: Long): MatchEntity?
  @Insert suspend fun insert(match: MatchEntity): Long
  @Update suspend fun update(match: MatchEntity)
  @Delete suspend fun delete(match: MatchEntity)
  @Query("SELECT * FROM matches WHERE strpos(',' || playerIds || ',', ',' || :playerId || ',') > 0 AND isCompleted = 1") suspend fun getCompletedMatchesForPlayer(playerId: Long): List<MatchEntity>
}

16. data/local/dao/PrizeCategoryDao.kt
@Dao interface PrizeCategoryDao {
  @Query("SELECT * FROM prize_categories WHERE matchId = :matchId ORDER BY sortOrder ASC") suspend fun getForMatch(matchId: Long): List<PrizeCategoryEntity>
  @Insert suspend fun insertAll(categories: List<PrizeCategoryEntity>)
  @Insert suspend fun insert(category: PrizeCategoryEntity): Long
  @Delete suspend fun delete(category: PrizeCategoryEntity)
  @Query("DELETE FROM prize_categories WHERE matchId = :matchId") suspend fun deleteForMatch(matchId: Long)
}

17. data/local/dao/WinnerDao.kt
@Dao interface WinnerDao {
  @Query("SELECT * FROM winners WHERE matchId = :matchId") suspend fun getForMatch(matchId: Long): List<WinnerEntity>
  @Query("SELECT * FROM winners WHERE playerId = :playerId") suspend fun getForPlayer(playerId: Long): List<WinnerEntity>
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(winner: WinnerEntity): Long
  @Query("DELETE FROM winners WHERE matchId = :matchId") suspend fun deleteForMatch(matchId: Long)
}

18. data/local/dao/CalledNumberDao.kt
@Dao interface CalledNumberDao {
  @Query("SELECT * FROM called_numbers WHERE matchId = :matchId ORDER BY callOrder ASC") suspend fun getForMatch(matchId: Long): List<CalledNumberEntity>
  @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insert(number: CalledNumberEntity): Long
  @Query("DELETE FROM called_numbers WHERE matchId = :matchId AND callOrder = (SELECT MAX(callOrder) FROM called_numbers WHERE matchId = :matchId)") suspend fun deleteLastForMatch(matchId: Long)
  @Query("SELECT number FROM called_numbers WHERE matchId = :matchId ORDER BY callOrder DESC LIMIT 1") suspend fun getLastNumber(matchId: Long): Int?
  @Query("DELETE FROM called_numbers WHERE matchId = :matchId") suspend fun deleteAllForMatch(matchId: Long)
}

19. data/local/AppDatabase.kt
@Database(entities = [PlayerEntity::class, MatchEntity::class, PrizeCategoryEntity::class, WinnerEntity::class, CalledNumberEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun playerDao(): PlayerDao
  abstract fun matchDao(): MatchDao
  abstract fun prizeCategoryDao(): PrizeCategoryDao
  abstract fun winnerDao(): WinnerDao
  abstract fun calledNumberDao(): CalledNumberDao
}

20. di/DatabaseModule.kt
@Module @InstallIn(SingletonComponent::class) object DatabaseModule {
  @Provides @Singleton fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, "superbingo_db")
      .addTypeConverter(Converters())
      .fallbackToDestructiveMigration()
      .build()
  @Provides fun providePlayerDao(db: AppDatabase) = db.playerDao()
  @Provides fun provideMatchDao(db: AppDatabase) = db.matchDao()
  @Provides fun providePrizeCategoryDao(db: AppDatabase) = db.prizeCategoryDao()
  @Provides fun provideWinnerDao(db: AppDatabase) = db.winnerDao()
  @Provides fun provideCalledNumberDao(db: AppDatabase) = db.calledNumberDao()
}

ACCEPTANCE CRITERIA:
- All files compile
- ./gradlew assembleDebug passes
- No Room schema errors
```

---

### Phase 3 Prompt — Repositories + Utilities

```
Build Phase 3 of Super Bingo. Database layer from Phase 2 is complete.

TASK: Create repositories and utility classes.

FILE 1: util/Constants.kt
object Constants {
  const val DEFAULT_PRIZE_CATEGORIES = "Laddu,Corner,BP,First Line,Second Line,Third Line,House"
  val DEFAULT_CATEGORY_NAMES = listOf("Laddu", "Corner", "BP", "First Line", "Second Line", "Third Line", "House")
  const val MAX_TAMBOLA_NUMBER = 90
  const val MIN_TAMBOLA_NUMBER = 1
  const val RECENT_NUMBERS_COUNT = 8
  const val RESTART_COUNTDOWN_SECONDS = 2
}

FILE 2: util/GameEngine.kt
Pure Kotlin class. NO Android imports.
class GameEngine {
  private val _calledNumbers = mutableListOf<Int>()
  private val _availableNumbers = (1..90).toMutableList().also { it.shuffle() }

  val calledNumbers: List<Int> get() = _calledNumbers.toList()
  val currentNumber: Int? get() = _calledNumbers.lastOrNull()
  val previousNumber: Int? get() = if (_calledNumbers.size >= 2) _calledNumbers[_calledNumbers.size - 2] else null
  val remainingCount: Int get() = _availableNumbers.size
  val isGameComplete: Boolean get() = _availableNumbers.isEmpty()

  fun callNextNumber(): Int? {
    if (_availableNumbers.isEmpty()) return null
    val number = _availableNumbers.removeAt(0)
    _calledNumbers.add(number)
    return number
  }

  fun undoLastNumber(): Int? {
    if (_calledNumbers.isEmpty()) return null
    val last = _calledNumbers.removeLast()
    _availableNumbers.add(last)
    _availableNumbers.sort()
    return last
  }

  fun restoreState(called: List<Int>) {
    _calledNumbers.clear()
    _calledNumbers.addAll(called)
    _availableNumbers.clear()
    _availableNumbers.addAll((1..90).filter { it !in called.toSet() })
  }

  fun reset() {
    _calledNumbers.clear()
    _availableNumbers.clear()
    _availableNumbers.addAll((1..90).toList().shuffled())
  }
}

FILE 3: util/NumberAnnouncer.kt
interface NumberAnnouncer {
  fun announce(number: Int)
  fun announceGameComplete()
  fun shutdown()
}
class NoOpNumberAnnouncer @Inject constructor() : NumberAnnouncer {
  override fun announce(number: Int) {}
  override fun announceGameComplete() {}
  override fun shutdown() {}
}

FILE 4: util/SoundEffectPlayer.kt
interface SoundEffectPlayer {
  fun playNumberCalled()
  fun playWinnerRecorded()
  fun playGameComplete()
}
class NoOpSoundEffectPlayer @Inject constructor() : SoundEffectPlayer {
  override fun playNumberCalled() {}
  override fun playWinnerRecorded() {}
  override fun playGameComplete() {}
}

FILE 5: util/Extensions.kt
fun Long.toFormattedDate(): String { /* use SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) */ }
fun Long.toFormattedTime(): String { /* use SimpleDateFormat("hh:mm a", Locale.getDefault()) */ }
fun Double.toRupees(): String = "₹${String.format("%.0f", this)}"

FILE 6: data/repository/PlayerRepository.kt
@Singleton class PlayerRepository @Inject constructor(private val playerDao: PlayerDao) {
  fun getAllPlayers(): Flow<List<Player>> = playerDao.getAllPlayers().map { list -> list.map { it.toDomain() } }
  suspend fun getPlayerById(id: Long): Player? = playerDao.getById(id)?.toDomain()
  suspend fun getPlayersByIds(ids: List<Long>): List<Player> = playerDao.getByIds(ids).map { it.toDomain() }
  suspend fun insertPlayer(name: String): Long = playerDao.insert(PlayerEntity(name = name.trim(), createdAt = System.currentTimeMillis()))
  suspend fun updatePlayer(id: Long, name: String) { playerDao.getById(id)?.let { playerDao.update(it.copy(name = name.trim())) } }
  suspend fun deletePlayer(id: Long) { playerDao.getById(id)?.let { playerDao.delete(it) } }
}

FILE 7: data/repository/MatchRepository.kt
@Singleton class MatchRepository @Inject constructor(
  private val matchDao: MatchDao,
  private val prizeCategoryDao: PrizeCategoryDao,
  private val winnerDao: WinnerDao,
  private val calledNumberDao: CalledNumberDao,
  private val playerDao: PlayerDao
) {
  fun getAllMatches(): Flow<List<Match>> = matchDao.getAllMatches().map { list -> list.map { it.toDomain() } }

  suspend fun createMatch(potAmount: Double, playerIds: List<Long>, prizeCategories: List<PrizeCategory>): Long {
    val matchEntity = MatchEntity(createdAt = System.currentTimeMillis(), totalPotAmount = potAmount, playerIds = playerIds.joinToString(","))
    val matchId = matchDao.insert(matchEntity)
    val categoryEntities = prizeCategories.mapIndexed { index, cat ->
      PrizeCategoryEntity(matchId = matchId, name = cat.name, amount = cat.amount, sortOrder = index)
    }
    prizeCategoryDao.insertAll(categoryEntities)
    return matchId
  }

  suspend fun getMatchDetail(id: Long): MatchDetail? {
    val matchEntity = matchDao.getById(id) ?: return null
    val match = matchEntity.toDomain()
    val players = playerDao.getByIds(match.playerIds).map { it.toDomain() }
    val categories = prizeCategoryDao.getForMatch(id).map { it.toDomain() }
    val winners = winnerDao.getForMatch(id).map { w ->
      val player = playerDao.getById(w.playerId)
      val category = prizeCategoryDao.getForMatch(id).find { it.id == w.prizeCategoryId }
      w.toDomain(playerName = player?.name ?: "", categoryName = category?.name ?: "")
    }
    val calledNumbers = calledNumberDao.getForMatch(id).map { it.toDomain() }
    return MatchDetail(match, players, categories, winners, calledNumbers)
  }

  suspend fun completeMatch(id: Long) {
    matchDao.getById(id)?.let { matchDao.update(it.copy(isCompleted = true, completedAt = System.currentTimeMillis())) }
  }

  suspend fun addCalledNumber(matchId: Long, number: Int, callOrder: Int) {
    calledNumberDao.insert(CalledNumberEntity(matchId = matchId, number = number, calledAt = System.currentTimeMillis(), callOrder = callOrder))
  }

  suspend fun removeLastCalledNumber(matchId: Long): Int? {
    val last = calledNumberDao.getLastNumber(matchId)
    calledNumberDao.deleteLastForMatch(matchId)
    return last
  }

  suspend fun getCalledNumbers(matchId: Long): List<CalledNumber> = calledNumberDao.getForMatch(matchId).map { it.toDomain() }

  suspend fun recordWinner(matchId: Long, prizeCategoryId: Long, playerId: Long, prizeAmount: Double) {
    winnerDao.insert(WinnerEntity(matchId = matchId, prizeCategoryId = prizeCategoryId, playerId = playerId, prizeAmount = prizeAmount, recordedAt = System.currentTimeMillis()))
  }

  suspend fun getWinnersForMatch(matchId: Long): List<Winner> {
    val winners = winnerDao.getForMatch(matchId)
    val categories = prizeCategoryDao.getForMatch(matchId)
    return winners.map { w ->
      val player = playerDao.getById(w.playerId)
      val category = categories.find { it.id == w.prizeCategoryId }
      w.toDomain(playerName = player?.name ?: "", categoryName = category?.name ?: "")
    }
  }

  suspend fun getPrizeCategoriesForMatch(matchId: Long): List<PrizeCategory> = prizeCategoryDao.getForMatch(matchId).map { it.toDomain() }

  suspend fun resetMatch(matchId: Long) {
    calledNumberDao.deleteAllForMatch(matchId)
    winnerDao.deleteForMatch(matchId)
  }

  suspend fun deleteMatch(id: Long) {
    matchDao.getById(id)?.let { matchDao.delete(it) }
  }
}

FILE 8: data/repository/StatisticsRepository.kt
@Singleton class StatisticsRepository @Inject constructor(
  private val playerDao: PlayerDao,
  private val winnerDao: WinnerDao,
  private val prizeCategoryDao: PrizeCategoryDao,
  private val matchDao: MatchDao
) {
  fun getAllPlayerStatistics(): Flow<List<PlayerStatistics>> = playerDao.getAllPlayers().map { players ->
    players.map { playerEntity -> computeStats(playerEntity.toDomain()) }
  }

  private suspend fun computeStats(player: Player): PlayerStatistics {
    val allWinners = winnerDao.getForPlayer(player.id)
    val completedMatches = matchDao.getCompletedMatchesForPlayer(player.id)
    var corners = 0; var lines = 0; var houses = 0; var laddus = 0; var bps = 0
    allWinners.forEach { w ->
      val categories = prizeCategoryDao.getForMatch(w.matchId)
      val catName = categories.find { it.id == w.prizeCategoryId }?.name?.lowercase() ?: ""
      when {
        catName.contains("corner") -> corners++
        catName.contains("line") -> lines++
        catName.contains("house") -> houses++
        catName.contains("laddu") -> laddus++
        catName.contains("bp") -> bps++
      }
    }
    return PlayerStatistics(
      player = player,
      gamesPlayed = completedMatches.size,
      totalWins = allWinners.size,
      totalPrizeMoney = allWinners.sumOf { it.prizeAmount },
      cornersWon = corners, linesWon = lines, housesWon = houses, laddusWon = laddus, bpWon = bps,
      lastWinDate = allWinners.maxOfOrNull { it.recordedAt }
    )
  }
}

Also add toDomain() extension functions to all Entity classes.
Also add toEntity() extension functions to all domain models where needed.

FILE 9: di/AppModule.kt
@Module @InstallIn(SingletonComponent::class) object AppModule {
  @Provides @Singleton fun provideNumberAnnouncer(): NumberAnnouncer = NoOpNumberAnnouncer()
  @Provides @Singleton fun provideSoundEffectPlayer(): SoundEffectPlayer = NoOpSoundEffectPlayer()
}

ACCEPTANCE CRITERIA:
- All repositories compile
- GameEngine unit test passes (call 90 numbers, verify no duplicates, verify undo works)
```

---

### Phase 4 Prompt — Navigation

```
Build Phase 4 of Super Bingo. Repositories from Phase 3 are complete.

TASK: Wire up complete navigation graph with stub screens.

FILE 1: navigation/Screen.kt
(Exact code as defined in blueprint Section 6.1)

FILE 2: navigation/AppNavigation.kt
Create NavHost with ALL routes.
For LiveMatch and MatchDetail and MatchSummary, extract matchId from backStackEntry.arguments as Long.

FILE 3: MainActivity.kt
Update to include:
val navController = rememberNavController()
AppNavigation(navController = navController)

STUB SCREENS: Create minimal Composable stubs for all screens that just show the screen name as text.
These will be replaced in later phases.

ACCEPTANCE CRITERIA:
- App launches on Home screen
- Can navigate to all stub screens
- Back button works on all screens
```

---

### Phase 5 Prompt — Player Management

```
Build Phase 5 of Super Bingo. Navigation from Phase 4 is complete.

TASK: Implement full Player Management feature.

Follow the UI spec from Section 7.2 of the blueprint exactly.

PlayerManagementViewModel:
- @HiltViewModel
- Collect getAllPlayers() as StateFlow<PlayerManagementUiState>
- Functions: addPlayer(name), editPlayer(id, name), deletePlayer(id)
- Validation: blank name → nameError; duplicate name → nameError
- Use viewModelScope.launch { withContext(Dispatchers.IO) { } } for all DB calls
- Emit UiEvent.ShowSnackbar for success/error messages

PlayerManagementScreen:
- collectAsStateWithLifecycle() for state
- LazyColumn with player cards
- Each card: name on left, edit + delete icons on right
- FAB "+" bottom right
- AlertDialog for add/edit with TextField
- ConfirmationDialog for delete

ui/components/EmptyStateView.kt:
- Takes message: String parameter
- Shows centered Column with emoji icon + message text

ui/components/ConfirmationDialog.kt:
- Parameters: title, message, onConfirm, onDismiss
- Material 3 AlertDialog

ACCEPTANCE CRITERIA:
- Add player works and shows in list
- Edit player updates name in list
- Delete player removes from list
- After app restart, players persist
- Duplicate name shows error
- Empty state shows when no players
```

---

### Phase 6 Prompt — New Match Setup

```
Build Phase 6 of Super Bingo. Player management from Phase 5 is complete.

TASK: Implement New Match setup screen.

Follow UI spec from Section 7.3 exactly.

NewMatchViewModel:
- @HiltViewModel
- Init: load all players from PlayerRepository
- State: NewMatchUiState
- Functions:
  - updatePotAmount(amount: String)
  - togglePlayer(playerId: Long)
  - addPrizeCategory(name: String, amount: Double)
  - updatePrizeCategory(index: Int, name: String, amount: Double)
  - removePrizeCategory(index: Int)
  - startMatch() → creates match via MatchRepository → emits NavigateTo(LiveMatch)
- Default prize categories loaded from Constants.DEFAULT_CATEGORY_NAMES with amount 0.0
- isStartEnabled = potAmount > 0 AND selectedPlayerIds.isNotEmpty()

NewMatchScreen:
- Scrollable Column
- Section 1: OutlinedTextField for pot amount (keyboardType = KeyboardType.Decimal)
  - Prefix: "₹"
- Section 2: Player list with checkboxes
  - Auto-count shown: "(X selected)"
- Section 3: Prize category list
  - Each row: name (clickable to rename) | amount field | delete button
  - "Add Category" button
- Bottom: "Start Match" primary button

ui/components/PrizeCategoryRow.kt:
- Parameters: category, onNameChange, onAmountChange, onDelete
- Row with OutlinedTextField for name, OutlinedTextField for amount (numeric), IconButton delete

ACCEPTANCE CRITERIA:
- Pot amount validates correctly
- Players load from DB and checkboxes work
- Prize categories can be added/edited/deleted
- Start Match creates match in Room DB (verify with DB inspector)
- Navigates to LiveMatch screen with correct matchId
```

---

### Phase 7 Prompt — Live Match Screen (MOST IMPORTANT)

```
Build Phase 7 of Super Bingo. Match setup from Phase 6 is complete.

TASK: Implement the complete Live Match game screen.

This is the most critical screen. Follow Section 7.4, Section 9, and Section 8 precisely.

LiveMatchViewModel:
- @HiltViewModel
- @Inject: MatchRepository, NumberAnnouncer, SoundEffectPlayer
- Private: val gameEngine = GameEngine()
- Init(matchId): load match detail, restore GameEngine state from called numbers in DB
- State: LiveMatchUiState
- Functions:
  - callNextNumber(): generate number, save to DB, update state
  - undoLastNumber(): call gameEngine.undoLastNumber(), remove from DB, update state
  - restartMatch(): clear DB called numbers and winners, reset gameEngine
  - endMatch(): completeMatch in DB, emit NavigateTo(MatchSummary)
  - showRecordWinnerFor(category): update state to show dialog
  - recordWinner(categoryId, playerId)
  - dismissRecordWinnerDialog()

NUMBER BOARD:
- Create ui/components/NumberBoard.kt
- LazyVerticalGrid(columns = GridCells.Fixed(10)) or use a fixed-size grid
- 90 cells numbered 1–90
- Each NumberCell colored based on state

NumberCell.kt:
- Card with number text
- Background color: surfaceVariant (uncalled), Color(0xFF4CAF50) (called), Color(0xFFE53935) (current)
- Current number: add infiniteTransition alpha animation (pulsing effect)
- Number text: White for called/current, default for uncalled

Current Number Display:
- Center the current number in a large Card
- Font size: 96sp
- Color: Red / Primary
- AnimatedContent for smooth number transitions

Call Next Number Button:
- Large, full-width
- ElevatedButton with primary color
- Disabled when isGameComplete = true

Undo Button:
- IconButton in top bar
- Icon: Icons.Default.Undo
- Disabled when calledNumbers is empty

Recently Called Row:
- Create ui/components/RecentNumbersRow.kt
- LazyRow, newest (last called) on LEFT
- Each item: small circle chip with number

Prize Claims Section:
- List each PrizeCategory
- If winner exists for category: show winner name + checkmark chip
- If no winner: show "Claim" ElevatedButton

CountdownConfirmDialog.kt:
- Parameters: title, message, countdownSeconds, onConfirm, onDismiss
- Internal state: secondsLeft (starts at countdownSeconds, counts down)
- LaunchedEffect: delay(1000ms) each iteration
- OK button disabled while secondsLeft > 0
- Button label: "OK (Xs)" while counting, "OK" when done

RecordWinnerDialog.kt:
- Shown as Dialog over LiveMatch
- Dropdown (ExposedDropdownMenuBox) with match players
- Prize amount shown
- Save button disabled until player selected

ACCEPTANCE CRITERIA:
- Call 10 numbers, verify on board (gray→red→green progression)
- Undo last number, verify board reverts
- Call all 90 numbers, verify button disabled
- Restart match with countdown dialog, verify board resets
- Record winner for a category, verify shown
- End match, navigate to summary screen
- CLOSE APP AND REOPEN: called numbers still on board (state restored from DB)
```

---

### Phase 8 Prompt — Winner + Phase 9 Prompt — Summary/History

```
Build Phases 8, 9 of Super Bingo.

PHASE 8 is handled inside LiveMatchViewModel (RecordWinnerDialog already built in Phase 7).
Verify winner recording works.

PHASE 9:

MatchSummaryViewModel:
- Load MatchDetail by matchId
- State: MatchSummaryUiState

MatchSummaryScreen:
- Header card: date, time, pot amount, player count
- Winners section: for each prize category, show winner or "—"
- "Back to Home" button → navigate to Home (popBackStack to home)
- "View in History" → navigate to MatchHistory

MatchHistoryViewModel:
- Collect getAllMatches() as Flow
- State: MatchHistoryUiState

MatchHistoryScreen:
- LazyColumn of MatchHistoryCard
- Empty state if no matches

MatchHistoryCard.kt:
- Shows: formatted date, player count, pot amount, winner count

MatchDetailViewModel + MatchDetailScreen:
- Same as MatchSummary but read-only
- No action buttons except Back

ACCEPTANCE CRITERIA:
- Play and end a match, summary shows correct data
- History shows the match
- Open history item, see full detail
- All data survives app restart
```

---

### Phase 10 Prompt — Statistics

```
Build Phase 10 of Super Bingo.

StatisticsViewModel:
- Collect getAllPlayerStatistics() as Flow
- State: StatisticsUiState

StatisticsScreen:
- LazyColumn of StatCard per player
- Empty state: "Play some matches to see statistics!"

StatCard.kt:
- Large player name
- Row grid of stat items (icon + value + label)
- Use MaterialTheme.colorScheme.primaryContainer for card background

ACCEPTANCE CRITERIA:
- Stats are accurate after playing 2+ matches
- Games played count correct
- Win count correct
- Prize money totals correct
```

---

### Phase 11 Prompt — Polish + Home Screen

```
Build Phase 11 of Super Bingo (final polish).

HOME SCREEN:
- App title "Super Bingo 🎱" in titleLarge, primary color
- Tagline "Tambola · Housie · Family Fun" in bodyMedium
- 5 navigation cards:
  - "🎮 Start New Match" — Full width, ElevatedCard, primary container background
  - "📋 Match History" and "👥 Players" — Row of 2 cards, equal width
  - "📊 Statistics" and "⚙️ Settings" — Row of 2 cards, equal width
- Each card: icon (large), title (medium bold), subtitle

SETTINGS SCREEN:
- Header: "Settings"
- Grayed-out toggles: "Voice Announcements", "Sound Effects"
- Label: "Cloud Backup" (coming soon)
- App version from BuildConfig

POLISH TASKS:
1. All Snackbars styled consistently
2. All loading states show CircularProgressIndicator
3. All error states handled (try-catch in all repo calls)
4. Smooth navigation transitions (default Compose transitions)
5. Number board cells sized correctly on all screen sizes (use weight modifier)
6. strings.xml: app_name = "Super Bingo"
7. AndroidManifest: label = @string/app_name

ACCEPTANCE CRITERIA:
- Full end-to-end test: Add 3 players → Start Match → Call 15 numbers → Record 2 winners → End Match → View Summary → View History → Check Statistics
- No crashes at any step
- App icon visible on launcher
- Back navigation works everywhere
```

---

---

# SECTION 15 — RISK ANALYSIS

## 15.1 Architecture Risks

| Risk | Impact | Mitigation |
|------|--------|------------|
| GameEngine state lost on process death | Medium | Restore from DB on ViewModel init (Phase 7) |
| Room schema migration breaks existing installs | High | Use `fallbackToDestructiveMigration()` in MVP; add proper migrations before v2 |
| StateFlow not collected properly causing UI freeze | Medium | Always use `collectAsStateWithLifecycle()` not `collectAsState()` |
| ViewModel scoping error (wrong NavBackStackEntry) | Medium | Use `hiltViewModel()` from hilt-navigation-compose |

## 15.2 Data Consistency Risks

| Risk | Impact | Mitigation |
|------|--------|------------|
| Called number saved to DB but GameEngine not updated | High | Always update GameEngine first, then persist to DB in same coroutine block |
| Duplicate winner for same category | Medium | Check winners list in ViewModel before showing Claim button |
| Player deleted while in active match | Low | PlayerIds stored as IDs; show "Unknown Player" if lookup fails |
| Undo after winner recorded for that number | Low | Document: undo does NOT remove winners; host handles manually |

## 15.3 UI Risks

| Risk | Impact | Mitigation |
|------|--------|------------|
| Number board too small on small screens | Medium | Use `fillMaxWidth()` + weight layout; test on 5" device |
| Dialog state lost on rotation | Low | Dialog state in ViewModel StateFlow, survives rotation |
| Countdown timer not cancelling on dismiss | Low | Use `LaunchedEffect` with key; cancel on dismiss |

## 15.4 Play Store Risks

| Risk | Impact | Mitigation |
|------|--------|------------|
| App flagged as gambling app | High | App description must say "family game companion, no real money wagering" |
| Crash on API 24 (minSdk) | Medium | Test on API 24 emulator before release |
| Missing privacy policy | Medium | Create simple privacy policy page; link in Play Store listing |
| Large APK size | Low | Enable R8, use Compose only (no Views) |

---

---

# SECTION 16 — LAUNCH READINESS CHECKLIST

## Functionality ✓
- [ ] Add/edit/delete players works
- [ ] New match setup with all fields works
- [ ] 90 numbers can be called without duplicates
- [ ] Undo works correctly
- [ ] Restart with countdown works
- [ ] Winner recording works
- [ ] Match summary shows all data
- [ ] Match history shows all past matches
- [ ] Statistics show accurate data
- [ ] All navigation works (forward and back)

## Stability ✓
- [ ] No crashes during full game play
- [ ] App restart doesn't lose data
- [ ] Screen rotation doesn't crash
- [ ] Back button works everywhere without crash
- [ ] Empty states shown correctly
- [ ] No ANR (all DB on IO dispatcher)

## Testing ✓
- [ ] GameEngine unit tests pass (no duplicates, undo, restore)
- [ ] Repository unit tests (mock DAOs)
- [ ] Manual end-to-end test completed
- [ ] Tested on API 24 emulator
- [ ] Tested on API 33/34 device

## Privacy ✓
- [ ] No network calls in MVP
- [ ] No personal data collected
- [ ] No analytics SDK added
- [ ] Privacy policy page exists (even if minimal)
- [ ] No third-party SDKs beyond declared dependencies

## Play Store Readiness ✓
- [ ] App icon (512x512 PNG, adaptive icon)
- [ ] Feature graphic (1024x500 PNG)
- [ ] Screenshots (phone: min 2)
- [ ] Short description (< 80 chars): "Host Tambola/Housie games for family & friends!"
- [ ] Full description written (family-friendly, no gambling language)
- [ ] Content rating: Everyone
- [ ] R8/ProGuard enabled in release build
- [ ] Release signing keystore created and stored safely
- [ ] Version code = 1, version name = "1.0.0"
- [ ] `android:exported` set correctly on MainActivity
- [ ] Target SDK = 34 (required by Play Store)

---

---

# APPENDIX A — DOMAIN MODEL RELATIONSHIPS

```
Player
  id: Long
  name: String
  createdAt: Long

Match
  id: Long
  createdAt: Long
  totalPotAmount: Double
  playerIds: List<Long>      ← refers to Player.id
  isCompleted: Boolean
  completedAt: Long?

PrizeCategory
  id: Long
  matchId: Long              ← refers to Match.id
  name: String
  amount: Double
  sortOrder: Int

Winner
  id: Long
  matchId: Long              ← refers to Match.id
  prizeCategoryId: Long      ← refers to PrizeCategory.id
  playerId: Long             ← refers to Player.id
  prizeAmount: Double
  recordedAt: Long
  playerName: String         ← denormalized for display
  categoryName: String       ← denormalized for display

CalledNumber
  id: Long
  matchId: Long              ← refers to Match.id
  number: Int                ← 1–90
  calledAt: Long
  callOrder: Int             ← order in which it was called

MatchDetail (aggregated, not stored)
  match: Match
  players: List<Player>
  prizeCategories: List<PrizeCategory>
  winners: List<Winner>
  calledNumbers: List<CalledNumber>

PlayerStatistics (computed, not stored)
  player: Player
  gamesPlayed: Int
  totalWins: Int
  totalPrizeMoney: Double
  cornersWon: Int
  linesWon: Int
  housesWon: Int
  laddusWon: Int
  bpWon: Int
  lastWinDate: Long?
```

---

# APPENDIX B — COLOR PALETTE

```
Primary:        #7C3AED  (Vibrant Purple)
OnPrimary:      #FFFFFF
PrimaryContainer: #EDE9FE
Secondary:      #F59E0B  (Amber)
OnSecondary:    #FFFFFF
Tertiary:       #0D9488  (Teal)
Background:     #FFFFFF
Surface:        #F8F7FF
SurfaceVariant: #E9E7F5
NumberCalled:   #4CAF50  (Green)
NumberCurrent:  #E53935  (Red)
NumberUncalled: #E9E7F5  (Light gray-purple)
Error:          #EF4444
```

---

# APPENDIX C — UNIT TESTING STRATEGY

## GameEngine Tests (Priority: HIGH)

File: `test/util/GameEngineTest.kt`

```
testCallNextNumber_returnsNumberInRange()
testCallNextNumber_noDuplicates()
testCallNextNumber_returnsNullWhenExhausted()
testUndoLastNumber_restoresNumber()
testUndoLastNumber_returnsNullWhenEmpty()
testRestoreState_correctAvailableNumbers()
testReset_clearsAllState()
testFullGame_calls90UniqueNumbers()
```

## Repository Tests (Priority: MEDIUM)

Use `Room.inMemoryDatabaseBuilder()` for testing.

File: `androidTest/repository/PlayerRepositoryTest.kt`

```
testInsertAndGetPlayer()
testDuplicatePlayerNameThrows()
testDeletePlayer()
testGetPlayersByIds()
```

## ViewModel Tests (Priority: MEDIUM)

Use `TestCoroutineDispatcher` + mock repositories.

```
testCallNextNumber_updatesState()
testUndoNumber_updatesState()
testGameComplete_disablesButton()
```

---

*Blueprint Version: 1.0*
*Target: Android Studio AI Agent*
*App: Super Bingo (Tambola/Housie Companion)*
*Author: AI Blueprint Generator*
*Date: June 2026*
