package com.nastia.catalogapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>
    val username: Flow<String?>
    val isBiometricEnabled: Flow<Boolean>

    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun logout()
    suspend fun setBiometricEnabled(enabled: Boolean)
}