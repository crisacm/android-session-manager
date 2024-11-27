package com.github.crisacm.module.sessionmanager.core

import com.github.crisacm.module.sessionmanager.model.SessionInfo
import com.github.crisacm.module.sessionmanager.model.SessionValidationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration

interface SessionManager {
  suspend fun registerSession(sessionInfoOld: SessionInfo)
  suspend fun clearSession()
  fun getSessionInfo(): Flow<SessionInfo?>
  fun isSessionActive(): Flow<Boolean>
  fun configure(options: Map<String, Any>)
  fun getOption(key: String): Any?
  suspend fun validateSession(
    key: String,
    value: String,
  ): SessionValidationResult

  val sessionExpired: StateFlow<Boolean>
  /** Esta función inicializa el monitoreo de la expiración de la sesión en la variable [sessionExpired]. */
  fun monitorSessionExpiration()
  /** Esta función inicializa el monitoreo de la expiración de la sesión en la variable [sessionExpired] con
   * un temporizador. **NOTA:** No es posible utilizar ambos monitoreos al tiempo. */
  fun monitorSessionExpirationWithTimer(duration: Duration = Duration.ofHours(1))
}
