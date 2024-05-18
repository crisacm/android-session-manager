package com.github.crisacm.sessionmanager.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.toRoute
import com.github.crisacm.sessionmanager.domain.model.User
import com.github.crisacm.sessionmanager.navigation.Home
import com.github.crisacm.sessionmanager.navigation.Login
import com.github.crisacm.sessionmanager.presentation.screens.home.HomeContracts
import com.github.crisacm.sessionmanager.presentation.screens.home.HomeViewModel
import com.github.crisacm.sessionmanager.presentation.screens.home.composables.Home
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreenDestination(navController: NavController, backStackEntry: NavBackStackEntry) {
    val viewModel = getViewModel<HomeViewModel>()
    val args = backStackEntry.toRoute<Home>()
    val user = User(args.name ?: "", args.email ?: "", args.photoUrl ?: "")

    Home(
        user = user,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::setEvent,
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                HomeContracts.Effect.Navigation.ToLogin -> navController.navigate(Login)
            }
        }
    )
}
