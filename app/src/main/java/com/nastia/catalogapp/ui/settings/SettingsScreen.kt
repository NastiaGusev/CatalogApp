package com.nastia.catalogapp.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Appearance", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Dark mode")
                    Text(
                        text = if (uiState.isDarkMode == null) "Following system" else if (uiState.isDarkMode == true) "On" else "Off",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = uiState.isDarkMode == true,
                    onCheckedChange = { viewModel.setDarkMode(it) }
                )
            }

            HorizontalDivider()

            Text("Language", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = uiState.language == "en",
                    onClick = { viewModel.setLanguage("en") }
                )
                Text("English", modifier = Modifier.padding(end = 24.dp))

                RadioButton(
                    selected = uiState.language == "he",
                    onClick = { viewModel.setLanguage("he") }
                )
                Text("עברית")
            }

            HorizontalDivider()

            Text("Security", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable biometric login")
                Switch(
                    checked = uiState.isBiometricEnabled,
                    onCheckedChange = { viewModel.setBiometricEnabled(it) }
                )
            }

            HorizontalDivider()

            Text("Data", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
            ) {
                Text("Reset local changes from API")
            }

            HorizontalDivider()

            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Logout")
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset local changes?") },
            text = { Text("This will discard all local edits, added, and deleted products, restoring data from the API on next refresh.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetLocalChanges()
                    showResetDialog = false
                }) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out?") },
            text = { Text("You'll need to log in again to access the app.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.logout(onLogout)
                    showLogoutDialog = false
                }) { Text("Logout") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }
}