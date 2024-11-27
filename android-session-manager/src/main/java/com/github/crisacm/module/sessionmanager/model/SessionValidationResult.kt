package com.github.crisacm.module.sessionmanager.model

sealed class SessionValidationResult {
  object Success : SessionValidationResult()
  data class Failure(val reason: String) : SessionValidationResult()
}