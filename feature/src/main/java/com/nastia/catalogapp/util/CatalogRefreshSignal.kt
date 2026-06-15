package com.nastia.catalogapp.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRefreshSignal @Inject constructor() {
    private val _resetVersion = MutableStateFlow(0)
    val resetVersion: StateFlow<Int> = _resetVersion.asStateFlow()

    fun notifyReset() {
        android.util.Log.d("RefreshSignal", "notifyReset called, new version: ${_resetVersion.value + 1}")
        _resetVersion.update { it + 1 }
    }
}