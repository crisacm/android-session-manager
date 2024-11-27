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
          .setUserId(sessionInfo.userId)
          .setToken(sessionInfo.token)
          .setExpiration(sessionInfo.expiration)
          .putAllMetadata(sessionInfo.metadata)
          .build()
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  fun getSessionInfo(): Flow<SessionInfoProto?> {
    return dataStore.data.map { if (it.userId.isEmpty()) null else it }
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
