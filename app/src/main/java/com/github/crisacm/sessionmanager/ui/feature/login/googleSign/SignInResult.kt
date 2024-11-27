package com.github.crisacm.sessionmanager.ui.feature.login.googleSign


data class SignInResult(
    val data: UserData?,
    var errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)
