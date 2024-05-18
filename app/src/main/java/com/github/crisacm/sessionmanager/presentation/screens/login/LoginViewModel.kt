package com.github.crisacm.sessionmanager.presentation.screens.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.crisacm.sessionmanager.domain.model.User
import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import com.github.crisacm.sessionmanager.util.FieldValidations
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : BaseViewModel<LoginContracts.Event, LoginContracts.State, LoginContracts.Effect>() {

    override fun setInitialState(): LoginContracts.State =
        LoginContracts.State(
            isSplashVisible = true,
            isLoading = false,
            errorUserText = null,
            errorPassText = null
        )

    override fun handleEvent(event: LoginContracts.Event) {
        when (event) {
            is LoginContracts.Event.SingIn -> singInEvent(event.user, event.password)
            is LoginContracts.Event.Register -> setEffect { LoginContracts.Effect.Navigation.ToRegister }
            is LoginContracts.Event.SingInWithGoogle -> {}
        }
    }

    private fun singInEvent(user: String, pass: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            FieldValidations.isEmpty(user)?.let {
                setState { copy(isLoading = false, errorUserText = it) }
                setEffect { LoginContracts.Effect.ShowSnack("Please enter a username") }
                return@launch
            }

            FieldValidations.isValidEmail(user)?.let {
                setState { copy(isLoading = false, errorUserText = it) }
                setEffect { LoginContracts.Effect.ShowSnack("Please enter a valid email") }
                return@launch
            }

            FieldValidations.isEmpty(pass)?.let {
                setState { copy(isLoading = false, errorUserText = null, errorPassText = it) }
                setEffect { LoginContracts.Effect.ShowSnack("Please enter a password") }
                return@launch
            }

            signInWithEmailAndPassword(user, pass)
        }
    }

    private suspend fun signInWithEmailAndPassword(user: String, pass: String) = withContext(Dispatchers.IO) {
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(user, pass)
            .addOnCompleteListener {
                setState { copy(isLoading = false) }

                if (it.isSuccessful) {
                    setEffect {
                        LoginContracts.Effect.Navigation.ToMain(
                            User(
                                auth.currentUser?.displayName.toString(),
                                auth.currentUser?.email.toString(),
                                auth.currentUser?.photoUrl.toString()
                            )
                        )
                    }
                } else {
                    if (it.exception is FirebaseAuthInvalidUserException) {
                        setEffect { LoginContracts.Effect.ShowSnack("Invalid user or password") }
                    }

                    Log.i("Error", it.exception.toString())
                }
            }
    }
}
