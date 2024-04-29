package com.github.crisacm.sessionmanager.ui.screens.login

import com.github.crisacm.sessionmanager.ui.base.ViewEvent
import com.github.crisacm.sessionmanager.ui.base.ViewSideEffect
import com.github.crisacm.sessionmanager.ui.base.ViewState

class LoginContracts {

    sealed class Event : ViewEvent {
        object Failed : Event()
        data class SingIn(val user: String, val password: String) : Event()
    }

    data class State(
        val isLoading: Boolean,
        val isError: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object Failed : Effect()
        object UserLogged : Effect()
        object UserRegistered : Effect()

        sealed class Navigation : Effect() {
            object ToMain : Navigation()
            object ToRegister : Navigation()
        }
    }
}