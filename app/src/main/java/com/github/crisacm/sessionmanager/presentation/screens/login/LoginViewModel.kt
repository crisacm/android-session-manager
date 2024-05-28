package com.github.crisacm.sessionmanager.presentation.screens.login

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.viewModelScope
import com.github.crisacm.sessionmanager.domain.model.User
import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import com.github.crisacm.sessionmanager.presentation.screens.login.googleSign.GoogleAuthUiClient
import com.github.crisacm.sessionmanager.util.FieldValidations
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : BaseViewModel<LoginContracts.Event, LoginContracts.State, LoginContracts.Effect>() {

    private val auth = Firebase.auth
    private lateinit var googleAuthUiClient: GoogleAuthUiClient

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
            is LoginContracts.Event.SingInWGoogle -> signInWithGoogle(event.context)
            is LoginContracts.Event.ManageSignInResult -> manageSignInResult(event.data)
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
                manageSignInResult(it)
            }
    }

    private fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            googleAuthUiClient = GoogleAuthUiClient(context, Identity.getSignInClient(context))
            val signInIntentSender = googleAuthUiClient.signIn()

            if (signInIntentSender == null) {
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
        viewModelScope.launch {
            if (data == null) {
                setEffect { LoginContracts.Effect.ShowSnack("Can't get user account information") }
                return@launch
            }

            val signInResult = googleAuthUiClient.signInWithIntent(data)

            if (signInResult.data != null) {
                setEffect { LoginContracts.Effect.ShowSnack("SignIn Successful") }
                delay(500)
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

    private fun manageSignInResult(task: Task<AuthResult>) {
        task.addOnCompleteListener {
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
                val error: String? = when (it.exception) {
                    is FirebaseAuthInvalidUserException -> "Invalid user or password"
                    is FirebaseNetworkException -> "Check your network connection"
                    else -> null
                }

                error?.let { setEffect { LoginContracts.Effect.ShowSnack(it) } }
                Log.i("Error", it.exception.toString())
            }
        }
    }
}
