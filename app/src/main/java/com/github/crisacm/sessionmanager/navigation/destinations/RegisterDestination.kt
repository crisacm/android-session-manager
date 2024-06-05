package com.github.crisacm.sessionmanager.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.crisacm.sessionmanager.navigation.Home
import com.github.crisacm.sessionmanager.navigation.Login
import com.github.crisacm.sessionmanager.navigation.Register
import com.github.crisacm.sessionmanager.presentation.screens.register.RegisterContracts
import com.github.crisacm.sessionmanager.presentation.screens.register.RegisterViewModel
import com.github.crisacm.sessionmanager.presentation.screens.register.composables.Register
import org.koin.androidx.compose.getViewModel

@Composable
fun RegisterScreenDestination(navController: NavController) {
    val viewModel = getViewModel<RegisterViewModel>()
    Register(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = {
            when (it) {
                RegisterContracts.Effect.Navigation.ToLogin -> {
                    navController.popBackStack()
                    navController.navigateUp()
                }
                RegisterContracts.Effect.Navigation.ToMain -> {
                    navController.popBackStack()
                    navController.navigate(Home)
                }
            }
        }
    )
}
