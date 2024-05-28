package com.github.crisacm.sessionmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.crisacm.sessionmanager.navigation.destinations.HomeScreenDestination
import com.github.crisacm.sessionmanager.navigation.destinations.LoginScreenDestination
import com.github.crisacm.sessionmanager.navigation.destinations.RegisterScreenDestination

@Composable
fun Navigation(startDestination: NavDestinations) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Home> {
            HomeScreenDestination(
                navController = navController,
                backStackEntry = it
            )
        }

        composable<Login> {
            LoginScreenDestination(navController = navController)
        }

        composable<Register> {
            RegisterScreenDestination(navController = navController)
        }
    }
}
