package com.github.crisacm.module.sessionmanager.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.github.crisacm.module.sessionmanager.proto.SessionInfoProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionDataStore(private val dataStore: DataStore<SessionInfoProto>) {

  suspend fun saveSessionInfo(sessionInfo: SessionInfoProto) {
    try {
      dataStore.updateData { currentSessionInfo ->
        currentSessionInfo.toBuilder()
          .setUser(sessionInfo.user)
          .setPassword(sessionInfo.password)
          .setToken(sessionInfo.token)
          .setExpiration(sessionInfo.expiration)
          .putAllMetadata(sessionInfo.metadata ?: mapOf())
          .build()
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  fun getSessionInfo(): Flow<SessionInfoProto?> {
    return dataStore.data.map { if (it.user.isEmpty()) null else it }
  }

  suspend fun clearSession() {
    try {
      dataStore.updateData {
        it.toBuilder().clear().build()
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }
}
