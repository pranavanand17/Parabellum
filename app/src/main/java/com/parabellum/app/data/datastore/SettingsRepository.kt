package com.parabellum.app.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.parabellum.app.model.AccentColor
import com.parabellum.app.model.AnimationIntensity
import com.parabellum.app.model.ThemeOption
import com.parabellum.app.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class SettingsRepository(private val context: Context) {

    private object PreferenceKeys {
        val THEME_OPTION = stringPreferencesKey("theme_option")
        val ACCENT_COLOR = stringPreferencesKey("accent_color")
        val ANIMATION_INTENSITY = stringPreferencesKey("animation_intensity")
        val COMPACT_LAYOUT = booleanPreferencesKey("compact_layout")
        val FOCUS_DURATION = intPreferencesKey("focus_duration")
        val SHORT_BREAK_DURATION = intPreferencesKey("short_break_duration")
        val LONG_BREAK_DURATION = intPreferencesKey("long_break_duration")
        val SESSIONS_BEFORE_LONG_BREAK = intPreferencesKey("sessions_before_long_break")
    }

    val userSettingsFlow: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        val themeOptionName = preferences[PreferenceKeys.THEME_OPTION] ?: ThemeOption.PARABELLUM.name
        val accentColorName = preferences[PreferenceKeys.ACCENT_COLOR] ?: AccentColor.GREEN.name
        val animIntensityName = preferences[PreferenceKeys.ANIMATION_INTENSITY] ?: AnimationIntensity.NORMAL.name

        UserSettings(
            themeOption = try { ThemeOption.valueOf(themeOptionName) } catch (e: Exception) { ThemeOption.PARABELLUM },
            accentColor = try { AccentColor.valueOf(accentColorName) } catch (e: Exception) { AccentColor.GREEN },
            animationIntensity = try { AnimationIntensity.valueOf(animIntensityName) } catch (e: Exception) { AnimationIntensity.NORMAL },
            compactTaskLayout = preferences[PreferenceKeys.COMPACT_LAYOUT] ?: false,
            focusDurationMinutes = preferences[PreferenceKeys.FOCUS_DURATION] ?: 25,
            shortBreakDurationMinutes = preferences[PreferenceKeys.SHORT_BREAK_DURATION] ?: 5,
            longBreakDurationMinutes = preferences[PreferenceKeys.LONG_BREAK_DURATION] ?: 15,
            focusSessionsBeforeLongBreak = preferences[PreferenceKeys.SESSIONS_BEFORE_LONG_BREAK] ?: 4
        )
    }

    suspend fun updateThemeOption(themeOption: ThemeOption) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME_OPTION] = themeOption.name
        }
    }

    suspend fun updateAccentColor(accentColor: AccentColor) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.ACCENT_COLOR] = accentColor.name
        }
    }

    suspend fun updateAnimationIntensity(animationIntensity: AnimationIntensity) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.ANIMATION_INTENSITY] = animationIntensity.name
        }
    }

    suspend fun updateCompactTaskLayout(compact: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.COMPACT_LAYOUT] = compact
        }
    }

    suspend fun updateFocusDuration(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.FOCUS_DURATION] = minutes.coerceIn(1, 120)
        }
    }

    suspend fun updateShortBreakDuration(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.SHORT_BREAK_DURATION] = minutes.coerceIn(1, 60)
        }
    }

    suspend fun updateLongBreakDuration(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.LONG_BREAK_DURATION] = minutes.coerceIn(1, 60)
        }
    }

    suspend fun updateSessionsBeforeLongBreak(sessions: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.SESSIONS_BEFORE_LONG_BREAK] = sessions.coerceIn(1, 12)
        }
    }
}
