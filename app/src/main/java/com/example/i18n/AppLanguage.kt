package com.example.i18n

import androidx.compose.runtime.staticCompositionLocalOf

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    FR("fr", "Français", "🇫🇷"),
    EN("en", "English", "🇬🇧"),
    HA("ha", "Hausa", "🇳🇬")
}

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.FR }
