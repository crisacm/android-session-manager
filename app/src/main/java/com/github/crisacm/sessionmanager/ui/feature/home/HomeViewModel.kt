package com.github.crisacm.sessionmanager.ui.feature.home

import androidx.lifecycle.viewModelScope
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.sessionmanager.data.repo.AuthRepository
import com.github.crisacm.sessionmanager.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
      is HomeContracts.Event.ToLogin -> logout()
    }
  }

  private fun logout() {
    viewModelScope.launch(ioDispatcher) {
      authRepository.signOut().collectLatest {
        if (it) {
          setEffect { HomeContracts.Effect.Navigation.ToLogin }
        } else {
          setEffect { HomeContracts.Effect.ShowToast("Error") }
        }
      }

      /*
      sessionManager.signOut()
          .onSuccess { setEffect { HomeContracts.Effect.Navigation.ToLogin } }
          .onFailure { setEffect { HomeContracts.Effect.ShowToast("Error") } }
      */
    }
  }
}
