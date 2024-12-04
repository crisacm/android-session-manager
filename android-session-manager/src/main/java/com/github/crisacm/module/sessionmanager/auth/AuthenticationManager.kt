package com.github.crisacm.module.sessionmanager.auth

import com.github.crisacm.module.sessionmanager.model.TokenInfo

@Deprecated("It was planned to be implemented but in the end only the session management was left.")
interface AuthenticationManager {
  suspend fun authenticate(user: String, password: String): Result<TokenInfo>
  suspend fun refreshToken(token: String): Result<TokenInfo>
}
