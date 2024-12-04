package com.github.crisacm.sessionmanager.ui.feature.login

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.github.crisacm.sessionmanager.ui.base.ViewEvent
import com.github.crisacm.sessionmanager.ui.base.ViewSideEffect
import com.github.crisacm.sessionmanager.ui.base.ViewState
import com.github.crisacm.sessionmanager.util.FieldError

class LoginContracts {
  sealed interface Event : ViewEvent {
    data object Register : Event
    data class SingIn(val user: String, val password: String) : Event
    data object SingInWGoogle : Event
    data object SingInWFacebook : Event
    data class HandleSignInRequest(val data: Intent?) : Event
  }

  data class State(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorUserText: FieldError? = null,
    val errorPassText: FieldError? = null
  ) : ViewState

  sealed interface Effect : ViewSideEffect {
    data class ShowSnack(val msg: String) : Effect
    data class LaunchSelectGoogleAccount(val intentSenderRequest: IntentSenderRequest) : Effect

    sealed interface Navigation : Effect {
      data object ToMain : Navigation
      data object ToRegister : Navigation
    }
  }
}
