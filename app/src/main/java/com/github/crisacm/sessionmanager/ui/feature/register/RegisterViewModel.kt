package com.github.crisacm.sessionmanager.ui.feature.register

import androidx.lifecycle.viewModelScope
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.sessionmanager.data.repo.AuthRepository
import com.github.crisacm.sessionmanager.ui.base.BaseViewModel
import com.github.crisacm.sessionmanager.util.FieldValidations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel(
  private val sessionManager: SessionManager,
  private val authRepository: AuthRepository,
) : BaseViewModel<RegisterContracts.Event, RegisterContracts.State, RegisterContracts.Effect>() {

  private val ioDispatcher = Dispatchers.IO

  override fun setInitialState(): RegisterContracts.State = RegisterContracts.State(
    isLoading = false,
    errorNameText = null,
    errorUserText = null,
    errorPassText = null
  )

  override fun handleEvent(event: RegisterContracts.Event) {
    when (event) {
      is RegisterContracts.Event.Register -> register(event.name, event.user, event.password)
      is RegisterContracts.Event.ToLogin -> setEffect { RegisterContracts.Effect.Navigation.ToLogin }
    }
  }

  @Suppress("MagicNumber")
  private fun register(name: String, email: String, pass: String) {
    viewModelScope.launch(ioDispatcher) {
      setState { copy(isLoading = true) }

      FieldValidations.isEmpty(name)?.let {
        setState { RegisterContracts.State(errorNameText = "Enter your name") }
        setEffect { RegisterContracts.Effect.ShowSnack("Please enter your first name") }
        return@launch
      }

      FieldValidations.isEmpty(email)?.let {
        setState { RegisterContracts.State(errorUserText = "Enter an email") }
        setEffect { RegisterContracts.Effect.ShowSnack("Please enter an email") }
        return@launch
      }

      FieldValidations.isValidEmail(email)?.let {
        setState { RegisterContracts.State(errorUserText = "Enter a valid name") }
        setEffect { RegisterContracts.Effect.ShowSnack("Please enter a valid email") }
        return@launch
      }

      FieldValidations.isEmpty(pass)?.let {
        setState { RegisterContracts.State(errorPassText = "Enter a password") }
        setEffect { RegisterContracts.Effect.ShowSnack("Please enter a password") }
        return@launch
      }

      authRepository.register(name, email, pass).collect { result ->
        result
          .onSuccess { user ->
            setState { RegisterContracts.State(isSuccess = true) }
            delay(500)
            setEffect { RegisterContracts.Effect.Navigation.ToMain }
          }
          .onFailure { t ->
            setState { RegisterContracts.State() }
            setEffect { RegisterContracts.Effect.ShowSnack(t.message ?: "Error registering user") }
          }
      }
    }
  }
}
