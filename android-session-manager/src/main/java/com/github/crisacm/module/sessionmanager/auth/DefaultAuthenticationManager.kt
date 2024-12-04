package com.github.crisacm.module.sessionmanager.auth

import com.github.crisacm.module.sessionmanager.model.TokenInfo

@Deprecated("It was planned to be implemented but in the end only the session management was left.")
class DefaultAuthenticationManager : AuthenticationManager {
  override suspend fun authenticate(user: String, password: String): Result<TokenInfo> {
    throw NotImplementedError("Authentication logic must be provided by the project.")
  }

  override suspend fun refreshToken(token: String): Result<TokenInfo> {
    throw NotImplementedError("Token refresh logic must be provided by the project.")
  }
}