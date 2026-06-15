package com.nastia.catalogapp.data.repository

import com.nastia.catalogapp.data.local.datastore.UserPreferencesDataStore
import com.nastia.catalogapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : SettingsRepository {

    override val isDarkMode: Flow<Boolean?> = dataStore.isDarkMode
    override val language: Flow<String> = dataStore.language

    override suspend fun setDarkMode(enabled: Boolean) {
        dataStore.setDarkMode(enabled)
    }

    override suspend fun setLanguage(languageCode: String) {
        dataStore.setLanguage(languageCode)
    }
}