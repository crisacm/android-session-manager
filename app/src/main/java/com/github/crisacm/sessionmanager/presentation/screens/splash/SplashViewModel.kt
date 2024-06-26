package com.github.crisacm.sessionmanager.presentation.screens.splash

import androidx.lifecycle.viewModelScope
import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel<SplashContracts.Event, SplashContracts.State, SplashContracts.Effect>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val auth = Firebase.auth
            val currentUser = auth.currentUser

            setEffect {
                if (currentUser == null) {
                    SplashContracts.Effect.Navigation.ToLogin
                } else {
                    SplashContracts.Effect.Navigation.ToMain
                }
            }
        }
    }

    override fun setInitialState(): SplashContracts.State = SplashContracts.State(true)

    override fun handleEvent(event: SplashContracts.Event) = Unit
}
