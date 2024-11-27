package com.github.crisacm.module.sessionmanager.model

import androidx.datastore.core.Serializer
import com.github.crisacm.module.sessionmanager.proto.SessionInfoProto
import java.io.InputStream
import java.io.OutputStream

object SessionInfoProtoSerializer : Serializer<SessionInfoProto> {

  override suspend fun readFrom(input: InputStream): SessionInfoProto {
    return SessionInfoProto.parseFrom(input)
  }

  override suspend fun writeTo(
    t: SessionInfoProto,
    output: OutputStream,
  ) {
    return t.writeTo(output)
  }

  override val defaultValue: SessionInfoProto =
    SessionInfoProto.getDefaultInstance()
}
