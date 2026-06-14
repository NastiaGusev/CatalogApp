package com.nastia.catalogapp.ui.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Button(onClick = onLoginSuccess) {
            Text("Login (placeholder)")
        }
    }
}