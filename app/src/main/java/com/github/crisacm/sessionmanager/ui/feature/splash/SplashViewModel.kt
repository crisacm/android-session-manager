package com.github.crisacm.sessionmanager.ui.feature.splash

import androidx.lifecycle.viewModelScope
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.sessionmanager.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class SplashViewModel(
  private val sessionManager: SessionManager
) : BaseViewModel<SplashContracts.Event, SplashContracts.State, SplashContracts.Effect>() {

  init {
    viewModelScope.launch(Dispatchers.IO) {
      setEffect { SplashContracts.Effect.Navigation.ToLogin }
      if (sessionManager.isSessionActive().lastOrNull() == null) {
        setEffect { SplashContracts.Effect.Navigation.ToMain }
      } else {
        setEffect { SplashContracts.Effect.Navigation.ToLogin }
      }
    }
  }

  override fun setInitialState(): SplashContracts.State = SplashContracts.State()

  override fun handleEvent(event: SplashContracts.Event) = Unit
}
