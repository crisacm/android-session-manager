package com.github.crisacm.module.sessionmanager

import android.content.Context
import androidx.datastore.core.IOException
import com.github.crisacm.module.sessionmanager.Prefs.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionManagerInstance(context: Context) : SessionManager {

    private val dataStore = context.dataStore
    private val cryptHelper: CryptHelper = CryptHelper(context)

    @Suppress("TooGenericExceptionCaught")
    override suspend fun signIn(
        user: String,
        password: String,
        token: String?,
        args: Map<String, String>?
    ): Result<SessionInfo> {
        return try {
            val sessionInfo = dataStore.updateData { currentInfo ->
                currentInfo.copy(
                    user = cryptHelper.encrypt(user).toString(),
                    password = cryptHelper.encrypt(password).toString(),
                    token = token?.let { cryptHelper.encrypt(it).toString() },
                    args = args,
                    signInDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                    isLogged = true
                )
            }

            Result.success(sessionInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun signOut(): Result<SessionInfo> {
        return try {
            /** Replace data value with an new object with all vars in null, deleting last session */
            val lastSession = dataStore.data.last()
            lastSession.apply {
                user = cryptHelper.decrypt(user?.toByteArray() ?: byteArrayOf())
                password = cryptHelper.decrypt(password?.toByteArray() ?: byteArrayOf())
                token = cryptHelper.decrypt(token?.toByteArray() ?: byteArrayOf())
            }
            dataStore.updateData { SessionInfo() }
            Result.success(lastSession)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun validateSessionByUser(user: String?): Result<SessionInfo> {
        return try {
            val lastSession = dataStore.data.last()
            if (user == null || lastSession.user != user) {
                Result.failure(DefaultSessionExceptions.InvalidUser(user.toString()))
            } else {
                Result.success(lastSession)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun validateSessionByPassword(password: String?): Result<SessionInfo> {
        return try {
            val lastSession = dataStore.data.last()
            if (password == null || lastSession.password != password) {
                Result.failure(DefaultSessionExceptions.InvalidPassword(password.toString()))
            } else {
                Result.success(lastSession)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun validateSessionByToken(token: String?): Result<SessionInfo> {
        return try {
            val lastSession = dataStore.data.last()
            if (token == null || lastSession.token != token) {
                Result.failure(DefaultSessionExceptions.InvalidToken(token.toString()))
            } else {
                Result.success(lastSession)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSessionInfo(): Flow<SessionInfo?> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(SessionInfoSerializer.defaultValue)
            } else {
                throw exception
            }
        }

    override fun getSessionState(): Flow<Boolean> = dataStore.data
        .map { it.isLogged }
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(SessionInfoSerializer.defaultValue.isLogged)
            } else {
                throw exception
            }
        }
}
