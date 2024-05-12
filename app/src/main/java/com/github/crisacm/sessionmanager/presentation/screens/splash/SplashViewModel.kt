package com.github.crisacm.sessionmanager.presentation.screens.splash

import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class SplashViewModel : BaseViewModel<SplashContracts.Event, SplashContracts.State, SplashContracts.Effect>() {

    init {
        if (validate()) {
            setEffect { SplashContracts.Effect.Navigation.ToMain }
        } else {
            setEffect { SplashContracts.Effect.Navigation.ToLogin }
        }
    }

    override fun setInitialState(): SplashContracts.State = SplashContracts.State(true)

    override fun handleEvent(event: SplashContracts.Event) = Unit

    private fun validate(): Boolean {
        val auth = Firebase.auth
        Timber.i("Auth Current User: ${auth.currentUser}")
        return auth.currentUser != null
    }
}
