package com.github.crisacm.sessionmanager.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.crisacm.sessionmanager.navigation.Home
import com.github.crisacm.sessionmanager.navigation.Register
import com.github.crisacm.sessionmanager.presentation.screens.login.LoginContracts
import com.github.crisacm.sessionmanager.presentation.screens.login.LoginViewModel
import com.github.crisacm.sessionmanager.presentation.screens.login.composables.LoginScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreenDestination(navController: NavController) {
    val viewModel = getViewModel<LoginViewModel>()
    LoginScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = {
            when (it) {
                is LoginContracts.Effect.Navigation.ToMain -> {
                    navController.navigate(
                        Home(
                            it.user.name.ifEmpty { null },
                            it.user.email.ifEmpty { null },
                            it.user.photoUrl.ifEmpty { null }
                        )
                    )
                }

                is LoginContracts.Effect.Navigation.ToRegister -> {
                    navController.navigate(Register)
                }
            }
        }
    )
}
