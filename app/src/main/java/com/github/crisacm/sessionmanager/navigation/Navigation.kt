package com.github.crisacm.sessionmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.crisacm.sessionmanager.navigation.destinations.HomeScreenDestination
import com.github.crisacm.sessionmanager.navigation.destinations.LoginScreenDestination
import com.github.crisacm.sessionmanager.navigation.destinations.RegisterScreenDestination
import com.github.crisacm.sessionmanager.navigation.destinations.SplashScreenDestination

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavItem.Splash.route
    ) {
        composable(NavItem.Home) {
            HomeScreenDestination(navController = navController)
        }

        composable(NavItem.Login) {
            LoginScreenDestination(navController = navController)
        }

        composable(NavItem.Register) {
            RegisterScreenDestination(navController = navController)
        }

        composable(NavItem.Splash) {
            SplashScreenDestination(navController = navController)
        }

        /*
        composable(NavItem.Home) {
            MainScreen(onNavigate = {
                navController.navigate(NavItem.NoteDetails.createRoute(it))
            })
        }

        composable(NavItem.NoteDetails) { backStackEntry ->
            NoteDetailsScreen(
                noteId = backStackEntry.findArg<Long>(NavArgs.NoteId.key).toLong(),
                onUpClick = { navController.popBackStack() }
            )
        }
        */
    }
}

private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navItem.route,
        arguments = navItem.args
    ) {
        content(it)
    }
}

private inline fun <reified T> NavBackStackEntry.findArgs(key: String): T {
    val value = arguments?.get(key)
    requireNotNull(value)
    return value as T
}
