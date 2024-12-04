package com.github.crisacm.module.sessionmanager.core

import com.github.crisacm.module.sessionmanager.datastore.SessionDataStore
import com.github.crisacm.module.sessionmanager.model.SessionInfo
import com.github.crisacm.module.sessionmanager.model.SessionValidationResult
import com.github.crisacm.module.sessionmanager.model.toDomain
import com.github.crisacm.module.sessionmanager.model.toProto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.Instant

@Suppress("TooManyFunctions")
internal class DefaultSessionManager(
  private val sessionDataStore: SessionDataStore,
) : SessionManager {
  private val _sessionExpired = MutableStateFlow(false)
  override val sessionExpired: StateFlow<Boolean> get() = _sessionExpired

  init {
    sessionDataStore.getSessionInfo().map { sessionInfoProto -> sessionInfoProto?.toDomain() }.map { sessionInfo ->
      sessionInfo?.expiration?.isBefore(Instant.now()) == true
    }.distinctUntilChanged() // Only change when session state changes
      .onEach { isExpired ->
        if (isExpired) {
          _sessionExpired.value = true
          clearSession()
        } else {
          _sessionExpired.value = false
        }
      }.launchIn(CoroutineScope(Dispatchers.IO))
  }

  private val options: MutableMap<String, Any> = mutableMapOf()


  /**
   * Registers a new session by saving the provided session information.
   * **Note**: It is recommended to hash the password before storing it.
   *
   * @param sessionInfo The session information to be saved.
   */
  override suspend fun registerSession(sessionInfo: SessionInfo) {
    sessionDataStore.saveSessionInfo(sessionInfo.toProto())
  }

  override suspend fun clearSession() {
    sessionDataStore.clearSession()
  }

  override fun getSessionInfo(): Flow<SessionInfo?> {
    return sessionDataStore.getSessionInfo().map { sessionInfoProto ->
      sessionInfoProto?.toDomain()
    }
  }

  override fun isSessionActive(): Flow<Boolean> {
    return getSessionInfo().map { sessionInfo ->
      sessionInfo?.expiration?.isAfter(Instant.now()) == true
    }
  }

  override fun configure(options: Map<String, Any>) {
    this.options.clear()
    this.options.putAll(options)
  }

  override fun getOption(key: String): Any? {
    return options[key]
  }

  override suspend fun validateSession(key: ValidateSessionKeys, value: String): SessionValidationResult {
    val sessionInfo = sessionDataStore.getSessionInfo().firstOrNull()?.toDomain()

    return when (key) {
      ValidateSessionKeys.USER -> {
        if (sessionInfo?.user == value) {
          SessionValidationResult.Success
        } else {
          SessionValidationResult.Failure("Invalid user ID")
        }
      }

      ValidateSessionKeys.PASSWORD -> {
        if (sessionInfo?.pass == value) {
          SessionValidationResult.Success
        } else {
          SessionValidationResult.Failure("Invalid password")
        }
      }

      ValidateSessionKeys.TOKEN -> {
        if (sessionInfo?.token == value) {
          SessionValidationResult.Success
        } else {
          SessionValidationResult.Failure("Invalid token")
        }
      }

      else -> SessionValidationResult.Failure("Invalid key for validation")
    }
  }
}
