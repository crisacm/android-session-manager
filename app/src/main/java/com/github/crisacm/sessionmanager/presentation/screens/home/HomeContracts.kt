package com.github.crisacm.sessionmanager.presentation.screens.home

import com.github.crisacm.sessionmanager.presentation.base.ViewEvent
import com.github.crisacm.sessionmanager.presentation.base.ViewSideEffect
import com.github.crisacm.sessionmanager.presentation.base.ViewState

object HomeContracts {

    sealed interface Event : ViewEvent {
        data object ToLogin : Event
    }

    class State : ViewState

    sealed interface Effect : ViewSideEffect {
        data class ShowToast(val msg: String) : Effect

        sealed interface Navigation : Effect {
            data object ToLogin : Navigation
        }
    }
}
