package com.github.crisacm.sessionmanager.presentation.screens.login

import android.content.Context
import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.viewModelScope
import com.github.crisacm.sessionmanager.domain.model.User
import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import com.github.crisacm.sessionmanager.presentation.screens.login.googleSign.GoogleAuthUiClient
import com.github.crisacm.sessionmanager.util.FieldValidations
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel(
    private val auth: FirebaseAuth,
    private val context: Context,
    private val appIoDispatcher: CoroutineDispatcher
) : BaseViewModel<LoginContracts.Event, LoginContracts.State, LoginContracts.Effect>() {

    companion object {
        private const val DELAY_0_5S = 500L
    }

    private val googleAuthUiClient by lazy { GoogleAuthUiClient(context, auth, Identity.getSignInClient(context)) }

    override fun setInitialState(): LoginContracts.State =
        LoginContracts.State(
            isLoading = false,
            errorUserText = null,
            errorPassText = null
        )

    override fun handleEvent(event: LoginContracts.Event) {
        when (event) {
            is LoginContracts.Event.SingIn -> singInEvent(event.user, event.password)
            is LoginContracts.Event.Register -> setEffect { LoginContracts.Effect.Navigation.ToRegister }
            is LoginContracts.Event.SingInWGoogle -> signInWithGoogle()
            is LoginContracts.Event.ManageSignInResult -> manageSignInResult(event.data)
        }
    }

    private fun singInEvent(user: String, pass: String) {
        viewModelScope.launch(appIoDispatcher) {
            setState { LoginContracts.State(isLoading = true) }

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

            auth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener {
                    viewModelScope.launch(appIoDispatcher) {
                        setState { copy(isLoading = false) }

                        if (it.isSuccessful) {
                            setEffect { LoginContracts.Effect.ShowSnack("SignIn Successful") }
                            delay(DELAY_0_5S)
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
                            val error: String? = when (it.exception) {
                                is FirebaseAuthInvalidUserException -> "Invalid user or password"
                                is FirebaseNetworkException -> "Check your network connection"
                                else -> null
                            }

                            error?.let { setEffect { LoginContracts.Effect.ShowSnack(it) } }
                            Timber.i(it.exception.toString())
                        }
                    }
                }
        }
    }

    private fun signInWithGoogle() {
        viewModelScope.launch(appIoDispatcher) {
            setState { LoginContracts.State(isLoading = true) }

            val signInIntentSender = googleAuthUiClient.signIn()

            if (signInIntentSender == null) {
                setState { copy(isLoading = false) }
                setEffect { LoginContracts.Effect.ShowSnack("Cannot get accounts, please verify your connection") }
                return@launch
            }

            val intentSenderRequest = IntentSenderRequest
                .Builder(signInIntentSender)
                .build()

            setEffect { LoginContracts.Effect.LaunchSelectGoogleAccount(intentSenderRequest) }
        }
    }

    private fun manageSignInResult(data: Intent?) {
        viewModelScope.launch(appIoDispatcher) {
            setState { copy(isLoading = false) }

            if (data == null) {
                setEffect { LoginContracts.Effect.ShowSnack("Can't get user account information") }
                return@launch
            }

            val signInResult = googleAuthUiClient.signInWithIntent(data)

            if (signInResult.data != null) {
                setEffect { LoginContracts.Effect.ShowSnack("SignIn Successful") }
                delay(DELAY_0_5S)
                setEffect {
                    LoginContracts.Effect.Navigation.ToMain(
                        User(
                            auth.currentUser?.displayName.toString(),
                            auth.currentUser?.email.toString(),
                            auth.currentUser?.photoUrl.toString()
                        )
                    )
                }
            }

            if (signInResult.errorMessage != null) {
                setEffect { LoginContracts.Effect.ShowSnack(signInResult.errorMessage.toString()) }
            }
        }
    }
}
