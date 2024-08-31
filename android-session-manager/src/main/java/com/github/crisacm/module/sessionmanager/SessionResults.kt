package com.github.crisacm.module.sessionmanager

sealed class DefaultSessionExceptions : Throwable() {
    data class InvalidPassword(val pass: String) : DefaultSessionExceptions()
    data class InvalidToken(val token: String) : DefaultSessionExceptions()
    data class InvalidUser(val user: String) : DefaultSessionExceptions()
}
