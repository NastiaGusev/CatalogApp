package com.nastia.catalogapp.domain.model

import androidx.annotation.StringRes
import com.nastia.catalogapp.R

enum class AppLanguage(val code: String, @StringRes val displayNameRes: Int) {
    ENGLISH("en", R.string.settings_language_english),
    HEBREW("he", R.string.settings_language_hebrew);
}