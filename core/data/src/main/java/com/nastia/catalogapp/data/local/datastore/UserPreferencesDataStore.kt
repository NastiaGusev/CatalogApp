package com.nastia.catalogapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nastia.catalogapp.model.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    private val context: Context
) {
    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USERNAME = stringPreferencesKey("username")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val LANGUAGE = stringPreferencesKey("language") // "en" or "he"
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map {
        it[Keys.IS_LOGGED_IN] ?: false
    }

    val username: Flow<String?> = context.dataStore.data.map {
        it[Keys.USERNAME]
    }

    val isDarkMode: Flow<Boolean?> = context.dataStore.data.map {
        it[Keys.IS_DARK_MODE]
    }

    val language: Flow<String> = context.dataStore.data.map {
        it[Keys.LANGUAGE] ?: AppLanguage.ENGLISH.code
    }

    val isBiometricEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[Keys.BIOMETRIC_ENABLED] ?: false
    }

    suspend fun setLoggedIn(username: String) {
        context.dataStore.edit {
            it[Keys.IS_LOGGED_IN] = true
            it[Keys.USERNAME] = username
        }
    }

    suspend fun logout() {
        context.dataStore.edit {
            it[Keys.IS_LOGGED_IN] = false
            it.remove(Keys.USERNAME)
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit {
            it[Keys.IS_DARK_MODE] = enabled
        }
    }

    suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit {
            it[Keys.LANGUAGE] = languageCode
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit {
            it[Keys.BIOMETRIC_ENABLED] = enabled
        }
    }
}