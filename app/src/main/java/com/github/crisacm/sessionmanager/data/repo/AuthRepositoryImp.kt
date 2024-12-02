package com.github.crisacm.sessionmanager.data.repo

import android.content.Context
import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.github.crisacm.sessionmanager.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp(
  private val firebaseAuth: FirebaseAuth,
  private val context: Context,
) : AuthRepository {

  private var oneTapClient: SignInClient = Identity.getSignInClient(context)
  private var signInRequest: BeginSignInRequest = BeginSignInRequest.builder()
    .setGoogleIdTokenRequestOptions(
      BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
        .setSupported(true)
        .setServerClientId(context.getString(R.string.firebase_auth_server_key))
        .setFilterByAuthorizedAccounts(false)
        .build()
    )
    .setAutoSelectEnabled(true)
    .build()

  override fun getGoogleSignInIntent(): Flow<IntentSenderRequest?> = flow {
    try {
      val intent = oneTapClient.beginSignIn(signInRequest).await()
      emit(
        IntentSenderRequest
          .Builder(intent.pendingIntent.intentSender)
          .build()
      )
    } catch (e: Exception) {
      e.printStackTrace()
      emit(null)
    }
  }.catch { emit(null) }

  override fun handleGoogleSignInResult(intent: Intent): Flow<FirebaseUser?> = flow {
    try {
      val credential = GoogleAuthProvider.getCredential(
        Identity.getSignInClient(context).getSignInCredentialFromIntent(intent).googleIdToken,
        null
      )
      val result = firebaseAuth.signInWithCredential(credential).await()
      emit(result.user)
    } catch (e: Exception) {
      e.printStackTrace()
      emit(null)
    }
  }.catch { emit(null) }

  override fun isUserAuthenticated(): Flow<Boolean> = flow {
    emit(firebaseAuth.currentUser != null)
  }

  override fun signInWithEmailAndPassword(
    email: String,
    password: String
  ): Flow<FirebaseUser?> = flow {
    val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
    emit(result.user)
  }

  override fun signOut(): Flow<Boolean> = flow {
    firebaseAuth.signOut()
    emit(true)
  }
}