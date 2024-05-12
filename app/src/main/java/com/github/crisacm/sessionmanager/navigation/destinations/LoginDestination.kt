package com.github.crisacm.sessionmanager.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.crisacm.sessionmanager.navigation.NavItem
import com.github.crisacm.sessionmanager.presentation.screens.login.LoginContracts
import com.github.crisacm.sessionmanager.presentation.screens.login.LoginViewModel
import com.github.crisacm.sessionmanager.presentation.screens.login.composables.LoginScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreenDestination(navController: NavController) {
    val vieWModel = getViewModel<LoginViewModel>()
    LoginScreen(
        state = vieWModel.viewState.value,
        effectFlow = vieWModel.effect,
        onEventSent = { event -> vieWModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is LoginContracts.Effect.Navigation.ToMain -> {
                    navController.navigate(NavItem.Home.route)
                }

                is LoginContracts.Effect.Navigation.ToRegister -> {
                    navController.navigate(NavItem.Register.route)
                }
            }
        }
    )
}
