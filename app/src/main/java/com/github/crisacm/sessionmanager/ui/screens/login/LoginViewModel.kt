package com.github.crisacm.sessionmanager.ui.screens.login

import com.github.crisacm.sessionmanager.ui.base.BaseViewModel

class LoginViewModel : BaseViewModel<LoginContracts.Event, LoginContracts.State, LoginContracts.Effect>() {

    override fun setInitialState(): LoginContracts.State =
        LoginContracts.State(
            isLoading = false,
            isError = false
        )

    override fun handleEvent(event: LoginContracts.Event) {
        when (event) {
            is LoginContracts.Event.Failed -> setEffect { LoginContracts.Effect.Failed }
            is LoginContracts.Event.SingIn -> setEffect { LoginContracts.Effect.UserLogged }
        }
    }
}
