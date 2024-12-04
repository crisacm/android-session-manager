package com.github.crisacm.sessionmanager.ui.feature.login

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.sessionmanager.data.repo.AuthRepository
import com.github.crisacm.sessionmanager.ui.base.BaseViewModel
import com.github.crisacm.sessionmanager.util.FieldValidations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
  private val sessionManager: SessionManager,
  private val appIoDispatcher: CoroutineDispatcher,
  private val authRepository: AuthRepository,
) : BaseViewModel<LoginContracts.Event, LoginContracts.State, LoginContracts.Effect>() {

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

  @Suppress("MagicNumber")
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
      ).collectLatest { result ->
        result
          .onSuccess {
            if (it != null) {
              // sessionManager.signIn(user, pass)
              setState { LoginContracts.State(isSuccess = true) }
              delay(500)
              setEffect { LoginContracts.Effect.Navigation.ToMain }
            } else {
              setState { LoginContracts.State() }
              setEffect { LoginContracts.Effect.ShowSnack("Invalid user or password") }
            }
          }
          .onFailure {
            setState { LoginContracts.State() }
            setEffect { LoginContracts.Effect.ShowSnack(it.message ?: "Error") }
          }
      }
    }
  }

  private fun signInWithGoogle() {
    viewModelScope.launch(appIoDispatcher) {
      setState { LoginContracts.State(isLoading = true) }

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
    }
  }
}
