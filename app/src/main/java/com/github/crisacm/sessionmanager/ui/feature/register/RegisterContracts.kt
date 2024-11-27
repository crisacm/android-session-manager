package com.github.crisacm.sessionmanager.ui.feature.register

import com.github.crisacm.sessionmanager.ui.base.ViewEvent
import com.github.crisacm.sessionmanager.ui.base.ViewSideEffect
import com.github.crisacm.sessionmanager.ui.base.ViewState
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
            data object ToMain : Navigation
            data object ToLogin : Navigation
        }
    }
}
