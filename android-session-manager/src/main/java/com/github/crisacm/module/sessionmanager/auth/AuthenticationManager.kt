package com.github.crisacm.module.sessionmanager.auth

import com.github.crisacm.module.sessionmanager.model.TokenInfo

interface AuthenticationManager {
  suspend fun authenticate(user: String, password: String): Result<TokenInfo>
  suspend fun refreshToken(token: String): Result<TokenInfo>
}
