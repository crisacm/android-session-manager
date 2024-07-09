package com.github.crisacm.module.sessionmanager

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.github.crisacm.module.sessionmanager.Prefs.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionManagerInstance(context: Context) : SessionManager {

    private val dataStore = context.dataStore

    override fun isSignIn(): Flow<Boolean> = dataStore.data
        .map { info ->
            info.isLogged
        }

    override fun getSignInDate(): Flow<String?> = dataStore.data
        .map { info ->
            info.signInDate
        }

    override fun getSignIn(): Flow<SessionInfo?> = flow {
        if (dataStore.data.first().isLogged) {
            emit(dataStore.data.first())
        } else {
            emit(null)
        }
    }

    override suspend fun signIn(
        user: String,
        password: String,
        args: Map<String, String>
    ) {
        dataStore.updateData { currentInfo ->
            currentInfo.copy(
                user = user,
                password = password,
                args = args,
                signInDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                isLogged = true
            )
        }
    }

    override suspend fun logout() {
        dataStore.updateData { currentInfo ->
            currentInfo.copy(
                user = "",
                password = "",
                args = emptyMap(),
                signInDate = "",
                isLogged = false
            )
        }
    }
}
