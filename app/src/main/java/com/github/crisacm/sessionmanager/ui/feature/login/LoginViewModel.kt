package com.github.crisacm.sessionmanager.ui.feature.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.sessionmanager.data.repo.AuthRepository
import com.github.crisacm.sessionmanager.ui.base.BaseViewModel
import com.github.crisacm.sessionmanager.ui.feature.login.googleSign.GoogleAuthUiClient
import com.github.crisacm.sessionmanager.util.FieldValidations
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
  private val auth: FirebaseAuth,
  private val sessionManager: SessionManager,
  private val context: Context,
  private val appIoDispatcher: CoroutineDispatcher,
  private val authRepository: AuthRepository,
) : BaseViewModel<LoginContracts.Event, LoginContracts.State, LoginContracts.Effect>() {

  private val googleAuthUiClient by lazy { GoogleAuthUiClient(context, auth, Identity.getSignInClient(context)) }

  override fun setInitialState(): LoginContracts.State =
    LoginContracts.State(
      isLoading = false,
      errorUserText = null,
      errorPassText = null
    )

  override fun handleEvent(event: LoginContracts.Event) {
    when (event) {
      is LoginContracts.Event.SingIn -> singInWithEmailAndPassword(event.user, event.password)
      is LoginContracts.Event.Register -> setEffect { LoginContracts.Effect.Navigation.ToRegister }
      is LoginContracts.Event.SingInWGoogle -> signInWithGoogle()
      is LoginContracts.Event.SingInWFacebook -> {}
      is LoginContracts.Event.HandleSignInRequest -> handleSignInResult(event.data)
    }
  }

  private fun singInWithEmailAndPassword(user: String, pass: String) {
    viewModelScope.launch(appIoDispatcher) {
      setState { LoginContracts.State(isLoading = true) }

      FieldValidations.isEmpty(user)?.let {
        setState { LoginContracts.State(errorUserText = it) }
        setEffect { LoginContracts.Effect.ShowSnack("Please enter a username") }
        return@launch
      }

      FieldValidations.isValidEmail(user)?.let {
        setState { LoginContracts.State(errorUserText = it) }
        setEffect { LoginContracts.Effect.ShowSnack("Please enter a valid email") }
        return@launch
      }

      FieldValidations.isEmpty(pass)?.let {
        setState { LoginContracts.State(errorPassText = it) }
        setEffect { LoginContracts.Effect.ShowSnack("Please enter a password") }
        return@launch
      }

      authRepository.signInWithEmailAndPassword(
        email = user,
        password = pass
      ).collectLatest {
        if (it != null) {
          // sessionManager.signIn(user, pass)
          setEffect { LoginContracts.Effect.ShowSnack("SignIn Successful") }
          setEffect { LoginContracts.Effect.Navigation.ToMain }
        } else {
          setState { LoginContracts.State() }
          setEffect { LoginContracts.Effect.ShowSnack("Invalid user or password") }
        }
      }

      /*
      auth.signInWithEmailAndPassword(user, pass)
        .addOnCompleteListener {
          viewModelScope.launch(appIoDispatcher) {
            if (it.isSuccessful) {
              // sessionManager.signIn(user, pass)
              setEffect { LoginContracts.Effect.ShowSnack("SignIn Successful") }
              setEffect { LoginContracts.Effect.Navigation.ToMain }
            } else {
              setState { copy(isLoading = false) }

              val error: String? = when (it.exception) {
                is FirebaseAuthInvalidUserException -> "Invalid user or password"
                is FirebaseNetworkException -> "Check your network connection"
                else -> null
              }

              error?.let { setEffect { LoginContracts.Effect.ShowSnack(it) } }
              Timber.i(it.exception.toString())
            }
          }
        }
      */
    }
  }

  private fun signInWithGoogle() {
    viewModelScope.launch(appIoDispatcher) {
      setState { LoginContracts.State(isLoading = true) }

      /*
      val signInIntentSender = googleAuthUiClient.signIn()

      if (signInIntentSender == null) {
        setState { copy(isLoading = false) }
        setEffect { LoginContracts.Effect.ShowSnack("Cannot get accounts, please verify your connection") }
        return@launch
      }

      val intentSenderRequest = IntentSenderRequest
        .Builder(signInIntentSender)
        .build()
      */

      authRepository.getGoogleSignInIntent().collectLatest {
        if (it == null) {
          setState { copy(isLoading = false) }
          setEffect { LoginContracts.Effect.ShowSnack("Cannot get accounts, please verify your connection") }
          return@collectLatest
        }

        setEffect { LoginContracts.Effect.LaunchSelectGoogleAccount(it) }
      }
    }
  }

  private fun handleSignInResult(data: Intent?) {
    viewModelScope.launch(appIoDispatcher) {
      if (data == null) {
        setState { copy(isLoading = false) }
        setEffect { LoginContracts.Effect.ShowSnack("Can't get user account information") }
        return@launch
      }

      authRepository.handleGoogleSignInResult(data).collectLatest {
        if (it != null) {
          // sessionManager.signIn(it.displayName.toString(), "google")
          setEffect { LoginContracts.Effect.ShowSnack("SignIn Successful") }
          setEffect { LoginContracts.Effect.Navigation.ToMain }
        } else {
          setState { copy(isLoading = false) }
          setEffect { LoginContracts.Effect.ShowSnack("Can't get user account information") }
        }
      }

      /*
      val signInResult = googleAuthUiClient.signInWithIntent(data)

      if (signInResult.data != null) {
        // sessionManager.signIn(auth.currentUser?.displayName.toString(), "google")
        setEffect { LoginContracts.Effect.ShowSnack("SignIn Successful") }
        setEffect { LoginContracts.Effect.Navigation.ToMain }
      }

      if (signInResult.errorMessage != null) {
        setState { copy(isLoading = false) }
        setEffect { LoginContracts.Effect.ShowSnack(signInResult.errorMessage.toString()) }
      }
      */
    }
  }
}
