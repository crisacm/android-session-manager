package com.github.crisacm.module.sessionmanager.core

import com.github.crisacm.module.sessionmanager.model.SessionInfo
import com.github.crisacm.module.sessionmanager.model.SessionValidationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SessionManager {
  val sessionExpired: StateFlow<Boolean>

  suspend fun registerSession(sessionInfo: SessionInfo)

  suspend fun clearSession()

  fun getSessionInfo(): Flow<SessionInfo?>

  fun isSessionActive(): Flow<Boolean>

  fun configure(options: Map<String, Any>)

  fun getOption(key: String): Any?

  suspend fun validateSession(
    key: ValidateSessionKeys,
    value: String,
  ): SessionValidationResult
}

enum class ValidateSessionKeys {
  USER,
  PASSWORD,
  TOKEN,
}
