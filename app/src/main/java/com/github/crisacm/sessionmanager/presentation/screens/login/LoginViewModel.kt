package com.github.crisacm.sessionmanager.presentation.screens.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel<LoginContracts.Event, LoginContracts.State, LoginContracts.Effect>() {

    override fun setInitialState(): LoginContracts.State =
        LoginContracts.State(
            isLoading = false,
            isErrorUserEmpty = false,
            isErrorPassEmpty = false
        )

    override fun handleEvent(event: LoginContracts.Event) {
        when (event) {
            is LoginContracts.Event.SingIn -> singInEvent(event.user, event.password)
            is LoginContracts.Event.Register -> setEffect { LoginContracts.Effect.Navigation.ToRegister }
            is LoginContracts.Event.SingInWithGoogle -> singInWithGoogleEvent(event.account)
        }
    }

    private fun singInEvent(user: String, pass: String) {
        viewModelScope.launch {
            if (user.isEmpty()) {
                setState { copy(isErrorUserEmpty = true) }
                setEffect { LoginContracts.Effect.ShowSnack("Please enter a username") }
                return@launch
            }

            if (pass.isEmpty()) {
                setState { copy(isErrorUserEmpty = false, isErrorPassEmpty = true) }
                setEffect { LoginContracts.Effect.ShowSnack("Please enter a password") }
                return@launch
            }

            setState { copy(isLoading = true, isErrorUserEmpty = false, isErrorPassEmpty = false) }
            delay(1000)
            setState { copy(isLoading = false) }
            setEffect { LoginContracts.Effect.ShowSnack("Fail sign in") }
            Log.i("Event", "singInEvent end")
        }
    }

    private fun singInWithGoogleEvent(account: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true, isErrorUserEmpty = false, isErrorPassEmpty = false) }
            delay(1000)
            setState { copy(isLoading = false) }
            setEffect { LoginContracts.Effect.ShowSnack("Fail sign in") }
        }
    }
}
