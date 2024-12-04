package com.github.crisacm.module.sessionmanager.model

import com.github.crisacm.module.sessionmanager.proto.SessionInfoProto
import java.time.Instant

data class SessionInfo(
  val user: String = "",
  val pass: String = "",
  val token: String = "",
  val expiration: Instant? = null,
  val metadata: Map<String, String>? = null
)

fun SessionInfoProto.toDomain(): SessionInfo {
  return SessionInfo(
    user = this.user,
    pass = this.password,
    token = this.token,
    expiration = if (this.expiration > 0) Instant.ofEpochSecond(this.expiration) else null,
    metadata = if (this.metadataMap.isNotEmpty()) this.metadataMap else null
  )
}

internal fun SessionInfo.toProto(): SessionInfoProto {
  return SessionInfoProto.newBuilder()
    .setUser(this.user)
    .setPassword(this.pass)
    .setToken(this.token)
    .setExpiration(this.expiration?.epochSecond ?: 0)
    .putAllMetadata(this.metadata ?: emptyMap<String, String>())
    .build()
}
