package com.github.crisacm.module.sessionmanager

import kotlinx.coroutines.flow.Flow

interface SessionManager {
    fun isSignIn(): Flow<Boolean>
    fun getSignInDate(): Flow<String?>
    fun getSignIn(): Flow<SessionInfo?>
    suspend fun signIn(
        user: String,
        password: String,
        args: Map<String, String> = emptyMap()
    )
    suspend fun logout()
}
