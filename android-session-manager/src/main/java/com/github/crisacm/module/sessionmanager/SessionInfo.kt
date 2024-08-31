package com.github.crisacm.module.sessionmanager

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class SessionInfo(
    var user: String? = null,
    var password: String? = null,
    var token: String? = null,
    var args: Map<String, String>? = null,
    var signInDate: String? = null, // Format dd/MM/yyyy
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
            throw CorruptionException("Unable to read Session Information", e)
        }
    }

    override suspend fun writeTo(t: SessionInfo, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(SessionInfo.serializer(), t)
                    .encodeToByteArray()
            )
        }
    }
}
