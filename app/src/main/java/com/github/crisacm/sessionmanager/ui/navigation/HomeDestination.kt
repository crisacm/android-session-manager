package com.github.crisacm.sessionmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.crisacm.sessionmanager.ui.feature.home.HomeContracts
import com.github.crisacm.sessionmanager.ui.feature.home.HomeViewModel
import com.github.crisacm.sessionmanager.ui.feature.home.composables.Home
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreenDestination(navController: NavController) {
    val viewModel = getViewModel<HomeViewModel>()
    Home(
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                HomeContracts.Effect.Navigation.ToLogin -> {
                    navController.popBackStack()
                    navController.navigate(Login)
                }
            }
        }
    )
}
