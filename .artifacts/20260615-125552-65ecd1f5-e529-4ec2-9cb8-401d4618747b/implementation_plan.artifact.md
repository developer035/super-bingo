# Fix Winner Selection Dropdown

The user reported that the "select options box" (dropdown) in the `RecordWinnerDialog` is not clickable and the dropdown menu does not appear.

## Investigation Findings

The current implementation of `RecordWinnerDialog` uses an `OutlinedTextField` with a `.clickable` modifier inside a `Box`. However:
1. `OutlinedTextField` (and `TextField`) by default consumes touch events, which often prevents a `.clickable` modifier on its `Modifier` from firing, even when `readOnly = true`.
2. A better approach in Material 3 is to use `ExposedDropdownMenuBox`, which is specifically designed for this purpose.
3. If not using `ExposedDropdownMenuBox`, the `OutlinedTextField` needs to have its interaction disabled or be wrapped in a way that captures the click.

## Proposed Changes

### UI Component

#### [RecordWinnerDialog.kt](file:///K:/InternProjects/SuperBingo/app/src/main/java/com/nerdsaladstudios/superbingo/ui/screens/winner/RecordWinnerDialog.kt)

- Replace the manual `Box` + `OutlinedTextField` + `DropdownMenu` implementation with the Material 3 `ExposedDropdownMenuBox`.
- This will provide the correct "dropdown" behavior and ensure the entire field is clickable to open the menu.

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordWinnerDialog(
    category: PrizeCategory,
    players: List<Player>,
    onSave: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Winner — ${category.name}") },
        text = {
            Column {
                Text(
                    text = "Prize: ${category.amount.toRupees()}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedPlayer?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select a player") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        label = { Text("Winner") }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        players.forEach { player ->
                            DropdownMenuItem(
                                text = { Text(player.name) },
                                onClick = {
                                    selectedPlayer = player
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }
        },
        // ... confirm/dismiss buttons remain the same
    )
}
```

## Verification Plan

### Manual Verification
- Deploy the app to a device/emulator.
- Navigate to "Start New Match".
- Add a few players and start a match.
- In the "Live Match" screen, click "Claim" for any prize category.
- Verify that clicking anywhere on the "Winner" field in the `RecordWinnerDialog` opens the dropdown list of players.
- Verify that selecting a player updates the field and enables the "Save" button.
- Verify that saving records the winner correctly.
