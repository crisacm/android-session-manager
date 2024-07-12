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
    // TODO: Hacer que cuando se cierre la sesión, retorne la información de la sesión cerrada.
    suspend fun logout()
}
