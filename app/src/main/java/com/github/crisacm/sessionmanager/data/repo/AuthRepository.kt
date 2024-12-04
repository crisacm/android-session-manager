package com.github.crisacm.sessionmanager.data.repo

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
  fun isUserAuthenticated(): Flow<Boolean>

  fun getCurrentUser(): Flow<FirebaseUser?>

  fun getGoogleSignInIntent(): Flow<IntentSenderRequest?>

  fun handleGoogleSignInResult(intent: Intent): Flow<FirebaseUser?>

  fun signInWithEmailAndPassword(
    email: String,
    password: String,
  ): Flow<Result<FirebaseUser?>>

  fun signOut(): Flow<Boolean>

  fun register(
    name: String,
    email: String,
    password: String,
  ): Flow<Result<FirebaseUser?>>
}