package com.github.crisacm.sessionmanager.ui.feature.home

import androidx.lifecycle.viewModelScope
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.sessionmanager.data.repo.AuthRepository
import com.github.crisacm.sessionmanager.ui.base.BaseViewModel
import com.github.crisacm.sessionmanager.ui.feature.common.LoadingButtonState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
  private val sessionManager: SessionManager,
  private val authRepository: AuthRepository,
) : BaseViewModel<HomeContracts.Event, HomeContracts.State, HomeContracts.Effect>() {

  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

  override fun setInitialState(): HomeContracts.State = HomeContracts.State()

  override fun handleEvent(event: HomeContracts.Event) {
    when (event) {
      is HomeContracts.Event.Start -> {
        viewModelScope.launch(ioDispatcher) {
          setState { copy(isLoading = true) }
          authRepository.getCurrentUser().collectLatest {
            setState { copy(isLoading = false, user = it) }
          }
        }
      }
      is HomeContracts.Event.ToLogin -> logout()
    }
  }

  @Suppress("MagicNumber")
  private fun logout() {
    viewModelScope.launch(ioDispatcher) {
      setState { copy(buttonState = LoadingButtonState.LOADING) }
      delay(500)

      sessionManager.clearSession()
      authRepository.signOut().collectLatest {
        if (it) {
          setState { copy(buttonState = LoadingButtonState.SUCCESS) }
          delay(500)
          setEffect { HomeContracts.Effect.Navigation.ToLogin }
        } else {
          setState { copy(buttonState = LoadingButtonState.IDLE) }
          setEffect { HomeContracts.Effect.ShowToast("Error") }
        }
      }
    }
  }
}
