package com.github.crisacm.module.sessionmanager.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.crisacm.module.sessionmanager.model.SessionInfoProtoSerializer
import com.github.crisacm.module.sessionmanager.proto.SessionInfoProto

internal object Prefs {
  private const val DATABASE_NAME = "session.pb"

  // At the top level of your kotlin file:
  val Context.dataStore: DataStore<SessionInfoProto> by dataStore(
    fileName = DATABASE_NAME,
    serializer = SessionInfoProtoSerializer
  )
}
