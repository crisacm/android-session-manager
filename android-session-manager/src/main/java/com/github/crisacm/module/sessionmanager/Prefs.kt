package com.github.crisacm.module.sessionmanager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore

internal object Prefs {
    private const val DATABASE_NAME = "android-session-manager"
    val Context.dataStore: DataStore<SessionInfo> by dataStore(fileName = DATABASE_NAME, serializer = SessionInfoSerializer)
}
