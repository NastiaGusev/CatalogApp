package com.nastia.catalogapp.data.repository

import com.nastia.catalogapp.data.local.datastore.UserPreferencesDataStore
import com.nastia.catalogapp.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : AuthRepository {

    override val isLoggedIn: Flow<Boolean> = dataStore.isLoggedIn
    override val username: Flow<String?> = dataStore.username
    override val isBiometricEnabled: Flow<Boolean> = dataStore.isBiometricEnabled

    override suspend fun login(username: String, password: String): Result<Unit> {
        // Simulate network latency for realistic loading state
        delay(800)

        return if (VALID_CREDENTIALS[username] == password) {
            dataStore.setLoggedIn(username)
            Result.success(Unit)
        } else {
            Result.failure(InvalidCredentialsException())
        }
    }

    override suspend fun logout() {
        dataStore.logout()
    }

    override suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.setBiometricEnabled(enabled)
    }

    companion object {
        // Test credentials — documented in README
        private val VALID_CREDENTIALS = mapOf(
            "testuser" to "password123",
            "admin" to "admin123"
        )
    }
}

class InvalidCredentialsException : Exception("Invalid username or password")