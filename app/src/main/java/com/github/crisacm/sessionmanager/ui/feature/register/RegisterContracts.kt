package com.github.crisacm.sessionmanager.ui.feature.register

import com.github.crisacm.sessionmanager.ui.base.ViewEvent
import com.github.crisacm.sessionmanager.ui.base.ViewSideEffect
import com.github.crisacm.sessionmanager.ui.base.ViewState

class RegisterContracts {
  sealed interface Event : ViewEvent {
    data class Register(val name: String, val user: String, val password: String) : Event
    data object ToLogin : Event
  }

  data class State(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorNameText: String? = null,
    val errorUserText: String? = null,
    val errorPassText: String? = null
  ) : ViewState

  sealed interface Effect : ViewSideEffect {
    data class ShowSnack(val msg: String) : Effect

    sealed interface Navigation : Effect {
      data object ToMain : Navigation
      data object ToLogin : Navigation
    }
  }
}
