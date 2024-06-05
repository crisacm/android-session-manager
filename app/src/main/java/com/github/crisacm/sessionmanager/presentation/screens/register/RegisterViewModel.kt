package com.github.crisacm.sessionmanager.presentation.screens.register

import androidx.lifecycle.viewModelScope
import com.github.crisacm.sessionmanager.domain.model.User
import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import com.github.crisacm.sessionmanager.util.FieldValidations
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class RegisterViewModel : BaseViewModel<RegisterContracts.Event, RegisterContracts.State, RegisterContracts.Effect>() {

    private val ioDispatcher = Dispatchers.IO

    override fun setInitialState(): RegisterContracts.State = RegisterContracts.State(
        isLoading = false,
        errorNameText = null,
        errorUserText = null,
        errorPassText = null
    )

    override fun handleEvent(event: RegisterContracts.Event) {
        when (event) {
            is RegisterContracts.Event.Register -> register(event.name, event.user, event.password)
            is RegisterContracts.Event.ToLogin -> setEffect { RegisterContracts.Effect.Navigation.ToLogin }
        }
    }

    private fun register(name: String, email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }

            FieldValidations.isEmpty(name)?.let {
                setState { copy(isLoading = false, errorUserText = it) }
                setEffect { RegisterContracts.Effect.ShowSnack("Please enter your first name") }
                return@launch
            }

            FieldValidations.isEmpty(email)?.let {
                setState { copy(isLoading = false, errorUserText = it) }
                setEffect { RegisterContracts.Effect.ShowSnack("Please enter an email") }
                return@launch
            }

            FieldValidations.isValidEmail(email)?.let {
                setState { copy(isLoading = false, errorNameText = null, errorUserText = it) }
                setEffect { RegisterContracts.Effect.ShowSnack("Please enter a valid email") }
                return@launch
            }

            FieldValidations.isEmpty(pass)?.let {
                setState { copy(isLoading = false, errorNameText = null, errorUserText = null, errorPassText = it) }
                setEffect { RegisterContracts.Effect.ShowSnack("Please enter a password") }
                return@launch
            }

            checkIfEmailExists(email) { exists, exception ->
                if (exists) {
                    setState {
                        copy(isLoading = false, errorNameText = null, errorUserText = null, errorPassText = null)
                    }

                    setEffect { RegisterContracts.Effect.ShowSnack("Email already are registered") }
                    return@checkIfEmailExists
                } else {
                    Timber.i(exception?.message.toString())
                }

                createUser(email, pass) { isRegister, firebaseUser, e ->
                    if (isRegister) {
                        setState {
                            copy(
                                isLoading = true,
                                errorNameText = null,
                                errorUserText = null,
                                errorPassText = null
                            )
                        }
                        setEffect { RegisterContracts.Effect.ShowSnack("User register successfully") }
                        delay(500)
                        setEffect { RegisterContracts.Effect.Navigation.ToMain }
                    } else {
                        setState {
                            copy(
                                isLoading = false,
                                errorNameText = null,
                                errorUserText = null,
                                errorPassText = null
                            )
                        }
                        setEffect { RegisterContracts.Effect.ShowSnack(e?.message.toString()) }
                    }
                }
            }
        }
    }

    private suspend fun checkIfEmailExists(
        email: String,
        callback: suspend (Boolean, Exception?) -> Unit
    ) = withContext(ioDispatcher) {
        Firebase.auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (!signInMethods.isNullOrEmpty()) {
                        viewModelScope.launch(ioDispatcher) { callback(true, null) } // Email already exists
                    } else {
                        viewModelScope.launch(ioDispatcher) { callback(false, null) } // Email does not exist
                    }
                } else {
                    when (val exception = task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            viewModelScope.launch(ioDispatcher) { callback(false, exception) } // Invalid email format
                        }

                        is FirebaseAuthUserCollisionException -> {
                            viewModelScope.launch(ioDispatcher) { callback(true, exception) } // Email already exists
                        }

                        else -> {
                            viewModelScope.launch(ioDispatcher) { callback(false, exception) } // Other error
                        }
                    }
                }
            }
    }

    private suspend fun createUser(
        email: String,
        password: String,
        callback: suspend (Boolean, FirebaseUser?, Exception?) -> Unit
    ) = withContext(ioDispatcher) {
        val auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    viewModelScope.launch(ioDispatcher) { callback(true, auth.currentUser, null) }
                } else {
                    viewModelScope.launch(ioDispatcher) { callback(false, null, it.exception) }
                }
            }
    }
}
