package com.github.crisacm.module.sessionmanager

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class SessionInfo(
    var user: String = "",
    var password: String = "",
    var args: Map<String, String> = emptyMap(),
    var signInDate: String = "", // This date are in format DD/MM/YYYY
    var isLogged: Boolean = false
)

object SessionInfoSerializer : Serializer<SessionInfo> {

    override val defaultValue: SessionInfo = SessionInfo()

    override suspend fun readFrom(input: InputStream): SessionInfo {
        try {
            return Json.decodeFromString(
                SessionInfo.serializer(), input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            throw CorruptionException("Unable to read Session Informatio", e)
        }
    }

    override suspend fun writeTo(t: SessionInfo, output: OutputStream) {
        output.write(
            Json.encodeToString(SessionInfo.serializer(), t)
                .encodeToByteArray()
        )
    }
}
