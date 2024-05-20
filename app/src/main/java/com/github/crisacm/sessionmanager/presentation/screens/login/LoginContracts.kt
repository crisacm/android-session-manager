package com.github.crisacm.sessionmanager.presentation.screens.login

import com.github.crisacm.sessionmanager.domain.model.User
import com.github.crisacm.sessionmanager.presentation.base.ViewEvent
import com.github.crisacm.sessionmanager.presentation.base.ViewSideEffect
import com.github.crisacm.sessionmanager.presentation.base.ViewState
import com.github.crisacm.sessionmanager.presentation.screens.login.googleSign.SignInResult
import com.github.crisacm.sessionmanager.util.FieldError

class LoginContracts {

    sealed interface Event : ViewEvent {
        data object Register : Event
        data class SingIn(val user: String, val password: String) : Event
        data class SingInWithGoogle(val result: SignInResult) : Event
    }

    data class State(
        val isSplashVisible: Boolean,
        val isLoading: Boolean,
        val errorUserText: FieldError?,
        val errorPassText: FieldError?
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        data class ShowSnack(val msg: String) : Effect

        sealed interface Navigation : Effect {
            data class ToMain(val user: User) : Navigation
            data object ToRegister : Navigation
        }
    }
}
