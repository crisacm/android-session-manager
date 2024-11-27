package com.github.crisacm.module.sessionmanager.core

import com.github.crisacm.module.sessionmanager.auth.AuthenticationManager
import com.github.crisacm.module.sessionmanager.datastore.SessionDataStore
import com.github.crisacm.module.sessionmanager.model.SessionInfo
import com.github.crisacm.module.sessionmanager.model.SessionValidationResult
import com.github.crisacm.module.sessionmanager.model.toDomain
import com.github.crisacm.module.sessionmanager.model.toProto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.time.Instant

@Suppress("TooManyFunctions")
class DefaultSessionManager(
  private val authenticationManager: AuthenticationManager,
  private val sessionDataStore: SessionDataStore,
) : SessionManager {
  private val options: MutableMap<String, Any> = mutableMapOf()

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

  override suspend fun validateSession(key: String, value: String): SessionValidationResult {
    val sessionInfo = sessionDataStore.getSessionInfo().firstOrNull()?.toDomain()

    return when (key) {
      "user" -> {
        if (sessionInfo?.userId == value) {
          SessionValidationResult.Success
        } else {
          SessionValidationResult.Failure("Invalid user ID")
        }
      }

      "password" -> {
        // Supongamos que tienes un mecanismo para validar contraseñas
        val isValidPassword = validatePassword(sessionInfo, value)
        if (isValidPassword) {
          SessionValidationResult.Success
        } else {
          SessionValidationResult.Failure("Invalid password")
        }
      }

      "token" -> {
        if (sessionInfo?.token == value) {
          SessionValidationResult.Success
        } else {
          SessionValidationResult.Failure("Invalid token")
        }
      }

      else -> SessionValidationResult.Failure("Invalid key for validation")
    }
  }

  private fun validatePassword(sessionInfo: SessionInfo?, password: String): Boolean {
    // Lógica para validar la contraseña; puede ser comparativa o hashing.
    return sessionInfo?.metadata?.get("passwordHash") == hashPassword(password)
  }

  private fun hashPassword(password: String): String {
    // Implementa tu función de hashing.
    return password.reversed() // Ejemplo simple, NO usar en producción
  }

  private val _sessionExpired = MutableStateFlow(false)
  override val sessionExpired: StateFlow<Boolean> get() = _sessionExpired

  override fun monitorSessionExpiration() {
    sessionDataStore.getSessionInfo().map { sessionInfoProto -> sessionInfoProto?.toDomain() }.map { sessionInfo ->
      sessionInfo?.expiration?.isBefore(Instant.now()) == true
    }.distinctUntilChanged() // Solo emite cambios si el estado de sesión cambia
      .onEach { isExpired ->
        if (isExpired) {
          _sessionExpired.value = true
          clearSession()
        } else {
          _sessionExpired.value = false
        }
      }.launchIn(CoroutineScope(Dispatchers.IO))
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun monitorSessionExpirationWithTimer(duration: Duration) {
    flow {
      while (true) {
        emit(Unit)
        delay(duration)
      }
    }.flatMapLatest { sessionDataStore.getSessionInfo() }.map { sessionInfoProto -> sessionInfoProto?.toDomain() }
      .map { sessionInfo ->
        sessionInfo?.expiration?.isBefore(Instant.now()) == true
      }.distinctUntilChanged().onEach { isExpired ->
        if (isExpired) {
          _sessionExpired.value = true
          clearSession()
        } else {
          _sessionExpired.value = false
        }
      }.launchIn(CoroutineScope(Dispatchers.IO))
  }
}
