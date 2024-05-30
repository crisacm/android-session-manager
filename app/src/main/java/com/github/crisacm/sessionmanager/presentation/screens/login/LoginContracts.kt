package com.github.crisacm.sessionmanager.presentation.screens.login

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.github.crisacm.sessionmanager.domain.model.User
import com.github.crisacm.sessionmanager.presentation.base.ViewEvent
import com.github.crisacm.sessionmanager.presentation.base.ViewSideEffect
import com.github.crisacm.sessionmanager.presentation.base.ViewState
import com.github.crisacm.sessionmanager.util.FieldError

class LoginContracts {

    sealed interface Event : ViewEvent {
        data object Register : Event
        data class SingIn(val user: String, val password: String) : Event
        data object SingInWGoogle : Event
        data class ManageSignInResult(val data: Intent?) : Event
    }

    data class State(
        val isLoading: Boolean = false,
        val errorUserText: FieldError? = null,
        val errorPassText: FieldError? = null
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        data class ShowSnack(val msg: String) : Effect
        data class LaunchSelectGoogleAccount(val intentSenderRequest: IntentSenderRequest) : Effect

        sealed interface Navigation : Effect {
            data class ToMain(val user: User) : Navigation
            data object ToRegister : Navigation
        }
    }
}
