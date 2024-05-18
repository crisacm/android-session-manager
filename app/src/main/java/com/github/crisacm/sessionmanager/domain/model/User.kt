package com.github.crisacm.sessionmanager.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var name: String,
    var email: String,
    var photoUrl: String
)
