package com.github.crisacm.sessionmanager.ui.feature.splash

import com.github.crisacm.sessionmanager.ui.base.ViewEvent
import com.github.crisacm.sessionmanager.ui.base.ViewSideEffect
import com.github.crisacm.sessionmanager.ui.base.ViewState

object SplashContracts {

    sealed interface Event : ViewEvent

    class State : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            data object ToLogin : Navigation
            data object ToMain : Navigation
        }
    }
}
