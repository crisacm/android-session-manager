package com.github.crisacm.sessionmanager.presentation.screens.login

import android.content.Context
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
        data class SingInWGoogle(val context: Context) : Event
        data class ManageSignInResult(val data: Intent?) : Event
    }

    data class State(
        val isSplashVisible: Boolean,
        val isLoading: Boolean,
        val errorUserText: FieldError?,
        val errorPassText: FieldError?
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
