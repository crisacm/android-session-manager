package com.github.crisacm.module.sessionmanager.model

data class TokenInfo(
  val accessToken: String,
  val refreshToken: String?,
  val expiration: Long // Timestamp in seconds or milliseconds
)
