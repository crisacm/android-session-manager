package com.github.crisacm.sessionmanager.presentation.screens.register

import com.github.crisacm.sessionmanager.domain.model.User
import com.github.crisacm.sessionmanager.presentation.base.ViewEvent
import com.github.crisacm.sessionmanager.presentation.base.ViewSideEffect
import com.github.crisacm.sessionmanager.presentation.base.ViewState
import com.github.crisacm.sessionmanager.util.FieldError

class RegisterContracts {

    sealed interface Event : ViewEvent {
        data class Register(val name: String, val user: String, val password: String) : Event
        data object ToLogin : Event
    }

    data class State(
        val isLoading: Boolean,
        val errorNameText: FieldError?,
        val errorUserText: FieldError?,
        val errorPassText: FieldError?
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        data class ShowSnack(val msg: String) : Effect

        sealed interface Navigation : Effect {
            data class ToMain(val user: User) : Navigation
            data object ToLogin : Navigation
        }
    }
}
