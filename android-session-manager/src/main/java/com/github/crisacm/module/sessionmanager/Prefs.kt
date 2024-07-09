package com.github.crisacm.module.sessionmanager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

internal object Prefs {
    private const val DATABASE_NAME = "android-session-manager"
    val Context.dataStore: DataStore<SessionInfo> by dataStore(fileName = DATABASE_NAME, serializer = SessionInfoSerializer)

    val USER = stringPreferencesKey("user")
    val PASSWORD = stringPreferencesKey("password")
    val TOKEN = stringPreferencesKey("token")
    val SIGN_IN_DATE = stringPreferencesKey("sign_in_date")
    val IS_LOGGED = booleanPreferencesKey("is_logged")
}
