package com.github.crisacm.sessionmanager.auth

import com.github.crisacm.module.sessionmanager.auth.AuthenticationManager
import com.github.crisacm.module.sessionmanager.model.TokenInfo

class AuthenticationManagerImp : AuthenticationManager {

  override suspend fun authenticate(
    user: String,
    password: String
  ): Result<TokenInfo> {
    return Result.success(TokenInfo("token", "refreshToken", 1000))
  }

  override suspend fun refreshToken(token: String): Result<TokenInfo> {
    return Result.success(TokenInfo("token", "refreshToken", 1000))
  }
}