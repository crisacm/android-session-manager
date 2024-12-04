package com.github.crisacm.sessionmanager.ui.feature.home

import com.github.crisacm.sessionmanager.ui.base.ViewEvent
import com.github.crisacm.sessionmanager.ui.base.ViewSideEffect
import com.github.crisacm.sessionmanager.ui.base.ViewState
import com.github.crisacm.sessionmanager.ui.feature.common.LoadingButtonState
import com.google.firebase.auth.FirebaseUser

object HomeContracts {
  sealed interface Event : ViewEvent {
    object Start : Event
    data object ToLogin : Event
  }

  data class State(
    var isLoading: Boolean = true,
    var buttonState: LoadingButtonState = LoadingButtonState.IDLE,
    var user: FirebaseUser? = null
  ) : ViewState

  sealed interface Effect : ViewSideEffect {
    data class ShowToast(val msg: String) : Effect

    sealed interface Navigation : Effect {
      data object ToLogin : Navigation
    }
  }
}
