package com.github.crisacm.sessionmanager.presentation.screens.home

import androidx.lifecycle.viewModelScope
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.sessionmanager.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sessionManager: SessionManager
) : BaseViewModel<HomeContracts.Event, HomeContracts.State, HomeContracts.Effect>() {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override fun setInitialState(): HomeContracts.State = HomeContracts.State()

    override fun handleEvent(event: HomeContracts.Event) {
        when (event) {
            is HomeContracts.Event.ToLogin -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch(ioDispatcher) {
            /*
            sessionManager.signOut()
                .onSuccess { setEffect { HomeContracts.Effect.Navigation.ToLogin } }
                .onFailure { setEffect { HomeContracts.Effect.ShowToast("Error") } }
            */
        }
    }
}
