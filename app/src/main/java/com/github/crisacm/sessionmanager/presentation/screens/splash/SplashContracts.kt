package com.github.crisacm.sessionmanager.presentation.screens.splash

import com.github.crisacm.sessionmanager.presentation.base.ViewEvent
import com.github.crisacm.sessionmanager.presentation.base.ViewSideEffect
import com.github.crisacm.sessionmanager.presentation.base.ViewState

object SplashContracts {

    sealed interface Event : ViewEvent

    data class State(
        val isSplashVisible: Boolean
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            data object ToLogin : Navigation
            data object ToMain : Navigation
        }
    }
}
