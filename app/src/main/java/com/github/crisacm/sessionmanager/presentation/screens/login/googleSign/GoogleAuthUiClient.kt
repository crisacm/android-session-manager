package com.github.crisacm.sessionmanager.presentation.screens.login.googleSign

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.github.crisacm.sessionmanager.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class GoogleAuthUiClient(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val signInClient: SignInClient
) {
    suspend fun signIn(): IntentSender? {
        val result = try {
            signInClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            Timber.e("Error: ${e.message}")
            null
        }

        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = signInClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run { getSignedInUser() },
                errorMessage = null
            )
        } catch (e: Exception) {
            Timber.e("Error: ${e.message}")
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    private fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.firebase_auth_server_key))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}
