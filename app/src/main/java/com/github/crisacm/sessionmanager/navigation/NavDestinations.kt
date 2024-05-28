package com.github.crisacm.sessionmanager.navigation

import kotlinx.serialization.Serializable

interface NavDestinations

@Serializable
object Login : NavDestinations

@Serializable
object Register : NavDestinations

@Serializable
data class Home(
    var name: String?,
    var email: String?,
    var photoUrl: String?
) : NavDestinations
