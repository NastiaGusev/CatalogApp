package com.nastia.catalogapp.auth

import com.nastia.catalogapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeAuthRepository(
    initialLoggedIn: Boolean = false,
    initialBiometricEnabled: Boolean = false,
    private val validCredentials: Map<String, String> = mapOf("testuser" to "password123")
) : AuthRepository {

    private val _isLoggedIn = MutableStateFlow(initialLoggedIn)
    override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _username = MutableStateFlow<String?>(null)
    override val username: StateFlow<String?> = _username

    private val _isBiometricEnabled = MutableStateFlow(initialBiometricEnabled)
    override val isBiometricEnabled: StateFlow<Boolean> = _isBiometricEnabled

    var loginCallCount = 0

    override suspend fun login(username: String, password: String): Result<Unit> {
        loginCallCount++
        return if (validCredentials[username] == password) {
            _isLoggedIn.value = true
            _username.value = username
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid username or password"))
        }
    }

    override suspend fun logout() {
        _isLoggedIn.value = false
        _username.value = null
    }

    override suspend fun setBiometricEnabled(enabled: Boolean) {
        _isBiometricEnabled.value = enabled
    }
}