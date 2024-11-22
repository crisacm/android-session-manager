package com.github.crisacm.sessionmanager.presentation.screens.splash

import androidx.lifecycle.viewModelScope
import com.github.crisacm.module.sessionmanager.SessionManager
import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class SplashViewModel(
  private val sessionManager: SessionManager
) : BaseViewModel<SplashContracts.Event, SplashContracts.State, SplashContracts.Effect>() {

  init {
    viewModelScope.launch(Dispatchers.IO) {
      setEffect { SplashContracts.Effect.Navigation.ToLogin }
      /* TODO: Fix this later
      sessionManager.getSessionInfo()
        .lastOrNull()
        .let {
          setEffect {
            if (it == null) {
              SplashContracts.Effect.Navigation.ToMain
            } else {
              SplashContracts.Effect.Navigation.ToLogin
            }
          }
        }
      */
    }
  }

  override fun setInitialState(): SplashContracts.State = SplashContracts.State()

  override fun handleEvent(event: SplashContracts.Event) = Unit
}
