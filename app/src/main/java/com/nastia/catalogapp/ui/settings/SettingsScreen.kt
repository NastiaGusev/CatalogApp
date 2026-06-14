package com.nastia.catalogapp.ui.settings

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nastia.catalogapp.R
import com.nastia.catalogapp.domain.model.AppLanguage
import com.nastia.catalogapp.ui.settings.components.LanguageOption
import com.nastia.catalogapp.util.LocaleHelper

@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.settings_appearance),
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(R.string.settings_dark_mode))
                    Text(
                        text = if (uiState.isDarkMode == null) stringResource(R.string.settings_dark_mode_system)
                        else if (uiState.isDarkMode == true) stringResource(R.string.settings_dark_mode_on)
                        else stringResource(R.string.settings_dark_mode_off),
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

            Text(
                stringResource(R.string.settings_language),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val context = LocalContext.current
                val activity = context as? Activity

                AppLanguage.entries.forEach { language ->
                    LanguageOption(
                        selected = uiState.language == language.code,
                        label = stringResource(language.displayNameRes),
                        onClick = {
                            viewModel.setLanguage(language.code)
                            activity?.let {
                                LocaleHelper.setAppLocale(it, language.code)
                            }
                        }
                    )
                }
            }

            HorizontalDivider()

            Text(
                stringResource(R.string.settings_security),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.settings_biometric))
                Switch(
                    checked = uiState.isBiometricEnabled,
                    onCheckedChange = { viewModel.setBiometricEnabled(it) }
                )
            }

            HorizontalDivider()

            Text(
                stringResource(R.string.settings_data),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(stringResource(R.string.settings_reset))
            }

            HorizontalDivider()

            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.settings_logout))
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.settings_reset_dialog_title)) },
            text = { Text(stringResource(R.string.settings_reset_dialog_text)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetLocalChanges()
                    showResetDialog = false
                }) { Text(stringResource(R.string.settings_reset_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showResetDialog = false
                }) { Text(stringResource(R.string.settings_cancel)) }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(stringResource(R.string.settings_logout_dialog_title)) },
            text = { Text(stringResource(R.string.settings_logout_dialog_text)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.logout(onLogout)
                    showLogoutDialog = false
                }) { Text(stringResource(R.string.settings_logout)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                }) { Text(stringResource(R.string.settings_cancel)) }
            }
        )
    }
}