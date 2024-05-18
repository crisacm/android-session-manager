package com.github.crisacm.sessionmanager.presentation.screens.home

import androidx.lifecycle.viewModelScope
import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel<HomeContracts.Event, HomeContracts.State, HomeContracts.Effect>() {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override fun setInitialState(): HomeContracts.State = HomeContracts.State()

    override fun handleEvent(event: HomeContracts.Event) {
        when (event) {
            is HomeContracts.Event.ToLogin -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch(ioDispatcher) {
            Firebase.auth.signOut()
            setEffect { HomeContracts.Effect.Navigation.ToLogin }
        }
    }
}
