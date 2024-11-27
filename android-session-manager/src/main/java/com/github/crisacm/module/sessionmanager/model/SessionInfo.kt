package com.github.crisacm.module.sessionmanager.model

import com.github.crisacm.module.sessionmanager.proto.SessionInfoProto
import java.time.Instant

data class SessionInfo(
  val userId: String = "",
  val token: String = "",
  val expiration: Instant = Instant.now(),
  val metadata: Map<String, String>? = null
)

fun SessionInfoProto.toDomain(): SessionInfo {
  return SessionInfo(
    userId = this.userId,
    token = this.token,
    expiration = Instant.ofEpochSecond(this.expiration),
    metadata = this.metadataMap
  )
}

internal fun SessionInfo.toProto(): SessionInfoProto {
  return SessionInfoProto.newBuilder()
    .setUserId(this.userId)
    .setToken(this.token)
    .setExpiration(this.expiration.epochSecond)
    .putAllMetadata(this.metadata)
    .build()
}
