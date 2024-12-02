package com.github.crisacm.sessionmanager.data.repo

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
  fun isUserAuthenticated(): Flow<Boolean>

  fun getGoogleSignInIntent(): Flow<IntentSenderRequest?>

  fun handleGoogleSignInResult(intent: Intent): Flow<FirebaseUser?>

  fun signInWithEmailAndPassword(
    email: String,
    password: String,
  ): Flow<FirebaseUser?>

  fun signOut(): Flow<Boolean>
}