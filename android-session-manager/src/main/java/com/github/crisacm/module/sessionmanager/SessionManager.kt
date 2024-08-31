package com.github.crisacm.module.sessionmanager

import kotlinx.coroutines.flow.Flow

interface SessionManager {

    suspend fun signIn(
        user: String,
        password: String,
        token: String? = null,
        args: Map<String, String>? = null
    ): Result<SessionInfo>

    suspend fun signOut(): Result<SessionInfo>

    suspend fun validateSessionByUser(user: String?): Result<SessionInfo>

    suspend fun validateSessionByPassword(password: String?): Result<SessionInfo>

    suspend fun validateSessionByToken(token: String?): Result<SessionInfo>

    fun getSessionInfo(): Flow<SessionInfo?>

    fun getSessionState(): Flow<Boolean>
}
