package com.nastia.catalogapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isDarkMode: Flow<Boolean?> // null = follow system
    val language: Flow<String>

    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setLanguage(languageCode: String)
}