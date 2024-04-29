package com.github.crisacm.sessionmanager.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.crisacm.sessionmanager.navigation.NavItem
import com.github.crisacm.sessionmanager.ui.screens.home.Home

@Composable
fun HomeScreenDestination(navController: NavController) {
    Home {
        navController.navigate(NavItem.Login.route)
    }
}
