package com.github.crisacm.sessionmanager.ui.screens.login

import com.github.crisacm.sessionmanager.ui.base.ViewEvent
import com.github.crisacm.sessionmanager.ui.base.ViewSideEffect
import com.github.crisacm.sessionmanager.ui.base.ViewState

class LoginContracts {

    sealed class Event : ViewEvent {
        data object Register : Event()
        data class SingIn(val user: String, val password: String) : Event()
        data class SingInWithGoogle(val account: String) : Event()
    }

    data class State(
        val isLoading: Boolean,
        val isErrorUserEmpty: Boolean,
        val isErrorPassEmpty: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data object FailSingIn : Effect()
        data object UserEmpty : Effect()
        data object PassEmpty : Effect()
        data object WrongPass : Effect()
        data object UserLogged : Effect()
        data object UserRegistered : Effect()

        sealed class Navigation : Effect() {
            data object ToMain : Navigation()
            data object ToRegister : Navigation()
        }
    }
}
