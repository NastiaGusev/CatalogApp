package com.nastia.catalogapp.repository

import com.nastia.catalogapp.model.AuthError
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>
    val username: Flow<String?>
    val isBiometricEnabled: Flow<Boolean>

    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun logout()
    suspend fun setBiometricEnabled(enabled: Boolean)
}

class AuthException(val error: AuthError) : Exception()