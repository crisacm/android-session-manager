package com.github.crisacm.sessionmanager.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.crisacm.sessionmanager.navigation.Home
import com.github.crisacm.sessionmanager.navigation.Login
import com.github.crisacm.sessionmanager.navigation.Splash
import com.github.crisacm.sessionmanager.presentation.screens.splash.SplashContracts
import com.github.crisacm.sessionmanager.presentation.screens.splash.SplashViewModel
import com.github.crisacm.sessionmanager.presentation.screens.splash.composables.SplashScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun SplashScreenDestination(navController: NavController) {
    val viewModel = getViewModel<SplashViewModel>()
    SplashScreen(
        effectFlow = viewModel.effect,
        onNavigationRequested = {
            when (it) {
                SplashContracts.Effect.Navigation.ToLogin -> {
                    navController.navigate(Login)
                }

                is SplashContracts.Effect.Navigation.ToMain -> {
                    navController.navigate(
                        Home(
                            it.user.name.ifEmpty { null },
                            it.user.email.ifEmpty { null },
                            it.user.photoUrl.ifEmpty { null }
                        )
                    )
                }
            }
        }
    )
}
