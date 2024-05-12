package com.github.crisacm.sessionmanager.presentation.screens.login

import com.github.crisacm.sessionmanager.presentation.base.ViewEvent
import com.github.crisacm.sessionmanager.presentation.base.ViewSideEffect
import com.github.crisacm.sessionmanager.presentation.base.ViewState

class LoginContracts {

    sealed interface Event : ViewEvent {
        data object Register : Event
        data class SingIn(val user: String, val password: String) : Event
        data class SingInWithGoogle(val account: String) : Event
    }

    data class State(
        val isLoading: Boolean,
        val isErrorUserEmpty: Boolean,
        val isErrorPassEmpty: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class ShowSnack(val msg: String) : Effect()

        sealed class Navigation : Effect() {
            data object ToMain : Navigation()
            data object ToRegister : Navigation()
        }
    }
}
