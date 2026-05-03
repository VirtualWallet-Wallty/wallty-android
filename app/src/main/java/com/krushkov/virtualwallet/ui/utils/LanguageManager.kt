package com.krushkov.virtualwallet.ui.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageManager {
    const val LANGUAGE_EN = "en"
    const val LANGUAGE_BG = "bg"

    fun setLanguage(languageCode: String) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageCode)
        )
    }

    fun getCurrentLanguage(): String {
        val locales = AppCompatDelegate.getApplicationLocales()
        if (locales == LocaleListCompat.getEmptyLocaleList()) {
            return LANGUAGE_EN
        }
        return locales.get(0)?.language ?: LANGUAGE_EN
    }
}
