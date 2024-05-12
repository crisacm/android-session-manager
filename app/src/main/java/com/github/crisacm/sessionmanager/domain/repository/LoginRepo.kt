package com.github.crisacm.sessionmanager.domain.repository

import com.github.crisacm.sessionmanager.domain.model.User

interface LoginRepo {
    
    fun sing(
        user: User,
        onSign: () -> Unit,
        onFailed: () -> Unit
    )

    fun register(
        user: User,
        onRegister: () -> Unit,
        onFailed: () -> Unit
    )
}
