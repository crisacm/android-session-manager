package com.github.crisacm.module.sessionmanager

import android.content.Context
import com.github.crisacm.module.sessionmanager.util.Prefs.dataStore
import com.github.crisacm.module.sessionmanager.auth.AuthenticationManager
import com.github.crisacm.module.sessionmanager.core.DefaultSessionManager
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.module.sessionmanager.datastore.SessionDataStore

object SessionManagerProvider {

  @Volatile
  private var instance: SessionManager? = null

  fun getInstance(
    authenticationManager: AuthenticationManager,
    context: Context
  ): SessionManager {
    return instance ?: synchronized(this) {
      instance ?: DefaultSessionManager(
        authenticationManager,
        SessionDataStore(context.dataStore),
      ).also {
        instance = it
      }
    }
  }
}
