package com.github.crisacm.sessionmanager.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.crisacm.sessionmanager.navigation.NavItem
import com.github.crisacm.sessionmanager.presentation.screens.splash.SplashContracts
import com.github.crisacm.sessionmanager.presentation.screens.splash.SplashViewModel
import com.github.crisacm.sessionmanager.presentation.screens.splash.composables.SplashScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun SplashScreenDestination(navController: NavController) {
    val viewModel = getViewModel<SplashViewModel>()
    SplashScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SplashContracts.Effect.Navigation.ToMain -> {
                    navController.navigate(NavItem.Home.route)
                }

                is SplashContracts.Effect.Navigation.ToLogin -> {
                    navController.navigate(NavItem.Login.route)
                }
            }
        }
    )
}
