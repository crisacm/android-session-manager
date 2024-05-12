package com.github.crisacm.sessionmanager.presentation.screens.splash

import com.github.crisacm.sessionmanager.presentation.base.ViewEvent
import com.github.crisacm.sessionmanager.presentation.base.ViewSideEffect
import com.github.crisacm.sessionmanager.presentation.base.ViewState

class SplashContracts {

    sealed interface Event : ViewEvent

    data class State(val isLoading: Boolean) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            data object ToMain : Navigation
            data object ToLogin : Navigation
        }
    }
}