package com.github.crisacm.sessionmanager.data.repo

import android.content.Context
import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.github.crisacm.sessionmanager.BuildConfig
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
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
        .setServerClientId(BuildConfig.FIREBASE_KEY)
        .setFilterByAuthorizedAccounts(false)
        .build()
    )
    .setAutoSelectEnabled(true)
    .build()

  @Suppress("TooGenericExceptionThrown", "TooGenericExceptionCaught", "SwallowedException")
  override fun getGoogleSignInIntent(): Flow<IntentSenderRequest?> = flow<IntentSenderRequest?> {
    try {
      val intent = oneTapClient.beginSignIn(signInRequest).await()
      emit(
        IntentSenderRequest
          .Builder(intent.pendingIntent.intentSender)
          .build()
      )
    } catch (e: Exception) {
      throw Exception("Error al obtener el intent de inicio de sesión con Google: ${e.message}")
    }
  }.catch {
    it.printStackTrace()
    emit(null)
  }

  @Suppress("TooGenericExceptionThrown", "TooGenericExceptionCaught", "SwallowedException")
  override fun handleGoogleSignInResult(intent: Intent): Flow<FirebaseUser?> = flow {
    try {
      val credential = GoogleAuthProvider.getCredential(
        Identity.getSignInClient(context).getSignInCredentialFromIntent(intent).googleIdToken,
        null
      )
      val result = firebaseAuth.signInWithCredential(credential).await()
      emit(result.user)
    } catch (e: Exception) {
      throw Exception("Error al iniciar sesión con Google: ${e.message}")
    }
  }.catch {
    it.printStackTrace()
    emit(null)
  }

  override fun isUserAuthenticated(): Flow<Boolean> = flow {
    emit(firebaseAuth.currentUser != null)
  }

  override fun getCurrentUser(): Flow<FirebaseUser?> = flow {
    emit(firebaseAuth.currentUser)
  }

  override fun signInWithEmailAndPassword(
    email: String,
    password: String
  ): Flow<Result<FirebaseUser?>> = flow {
    val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
    emit(Result.success(result.user))
  }.catch {
    it.printStackTrace()
    emit(Result.failure(it))
  }

  override fun signOut(): Flow<Boolean> = flow {
    firebaseAuth.signOut()
    emit(true)
  }

  @Suppress("TooGenericExceptionThrown", "TooGenericExceptionCaught", "SwallowedException")
  override fun register(
    name: String,
    email: String,
    password: String
  ): Flow<Result<FirebaseUser?>> = flow {
    try {
      val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

      result.user?.updateProfile(
        userProfileChangeRequest {
          displayName = name
        }
      )?.await()

      emit(Result.success(result.user))
    } catch (e: FirebaseAuthUserCollisionException) {
      e.printStackTrace()
      throw Exception("El correo electrónico ya está registrado")
    } catch (e: Exception) {
      throw Exception("Error al registrar el usuario: ${e.message}")
    }
  }.catch {
    it.printStackTrace()
    emit(Result.failure(it))
  }
}