package com.github.crisacm.sessionmanager.presentation.screens.splash

import androidx.lifecycle.viewModelScope
import com.github.crisacm.module.sessionmanager.SessionManager
import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import javax.inject.Inject

class SplashViewModel(
    private val sessionManager: SessionManager
) : BaseViewModel<SplashContracts.Event, SplashContracts.State, SplashContracts.Effect>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (sessionManager.isSignIn().first()) {
                setEffect { SplashContracts.Effect.Navigation.ToMain }
            } else {
                setEffect { SplashContracts.Effect.Navigation.ToLogin }
            }
        }
    }

    override fun setInitialState(): SplashContracts.State = SplashContracts.State(true)

    override fun handleEvent(event: SplashContracts.Event) = Unit
}
