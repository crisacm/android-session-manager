package com.github.crisacm.sessionmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.crisacm.sessionmanager.domain.model.User
import com.github.crisacm.sessionmanager.navigation.destinations.HomeScreenDestination
import com.github.crisacm.sessionmanager.navigation.destinations.LoginScreenDestination
import com.github.crisacm.sessionmanager.navigation.destinations.RegisterScreenDestination

@Composable
fun Navigation(isUserLoggedIn: Boolean, user: User?) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = getStartDestination(isUserLoggedIn, user)
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

@Composable
fun getStartDestination(isUserLoggedIn: Boolean, user: User?): Any {
    return if (!isUserLoggedIn) Login else Home(user?.name, user?.email, user?.photoUrl)
}
