package com.github.crisacm.sessionmanager.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.crisacm.sessionmanager.navigation.NavItem
import com.github.crisacm.sessionmanager.ui.screens.register.Register

@Composable
fun RegisterScreenDestination(navController: NavController) {
    Register(
        onLogin = { navController.navigate(NavItem.Login.route) },
        onHome = { navController.navigate(NavItem.Home.route) }
    )
}
