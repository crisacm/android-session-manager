package com.github.crisacm.sessionmanager.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register

@Serializable
data class Home(
    var name: String?,
    var email: String?,
    var photoUrl: String?
)
