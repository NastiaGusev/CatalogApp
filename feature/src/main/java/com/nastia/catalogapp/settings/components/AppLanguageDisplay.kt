package com.nastia.catalogapp.settings.components

import androidx.annotation.StringRes
import com.nastia.catalogapp.R
import com.nastia.catalogapp.model.AppLanguage

val AppLanguage.displayNameRes: Int
    @StringRes get() = when (this) {
        AppLanguage.ENGLISH -> R.string.settings_language_english
        AppLanguage.HEBREW -> R.string.settings_language_hebrew
    }