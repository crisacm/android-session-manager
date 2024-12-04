package com.github.crisacm.module.sessionmanager

import android.content.Context
import com.github.crisacm.module.sessionmanager.core.DefaultSessionManager
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.module.sessionmanager.datastore.SessionDataStore
import com.github.crisacm.module.sessionmanager.util.Prefs.dataStore

object SessionManagerProvider {

  @Volatile
  private var instance: SessionManager? = null

  fun getInstance(
    context: Context
  ): SessionManager {
    return instance ?: synchronized(this) {
      instance ?: DefaultSessionManager(
        SessionDataStore(context.dataStore),
      ).also {
        instance = it
      }
    }
  }
}
