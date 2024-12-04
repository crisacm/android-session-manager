package com.github.crisacm.sessionmanager.ui.feature.splash

import androidx.lifecycle.viewModelScope
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.sessionmanager.data.repo.AuthRepository
import com.github.crisacm.sessionmanager.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class SplashViewModel(
  private val sessionManager: SessionManager,
  private val authRepository: AuthRepository,
) : BaseViewModel<SplashContracts.Event, SplashContracts.State, SplashContracts.Effect>() {

  init {
    viewModelScope.launch(Dispatchers.IO) {
      setEffect { SplashContracts.Effect.Navigation.ToLogin }
      combine(
        authRepository.isUserAuthenticated(),
        sessionManager.isSessionActive()
      ) { isAuthenticated, isSessionActive ->
        if (isAuthenticated && isSessionActive) {
          setEffect { SplashContracts.Effect.Navigation.ToMain }
        } else {
          setEffect { SplashContracts.Effect.Navigation.ToLogin }
        }
      }.collect()
    }
  }

  override fun setInitialState(): SplashContracts.State = SplashContracts.State()

  override fun handleEvent(event: SplashContracts.Event) = Unit
}
