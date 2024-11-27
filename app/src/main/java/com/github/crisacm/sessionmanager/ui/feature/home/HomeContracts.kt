package com.github.crisacm.sessionmanager.ui.feature.home

import com.github.crisacm.sessionmanager.ui.base.ViewEvent
import com.github.crisacm.sessionmanager.ui.base.ViewSideEffect
import com.github.crisacm.sessionmanager.ui.base.ViewState

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
