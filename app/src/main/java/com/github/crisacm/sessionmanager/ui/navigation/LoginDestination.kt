package com.github.crisacm.sessionmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.crisacm.sessionmanager.ui.feature.login.LoginContracts
import com.github.crisacm.sessionmanager.ui.feature.login.LoginViewModel
import com.github.crisacm.sessionmanager.ui.feature.login.composables.LoginScreen
import org.koin.androidx.compose.getViewModel

@Suppress("FunctionNaming")
@Composable
fun LoginScreenDestination(navController: NavController) {
  val viewModel = getViewModel<LoginViewModel>()
  LoginScreen(
    state = viewModel.viewState.value,
    effectFlow = viewModel.effect,
    onEventSent = viewModel::setEvent,
    onNavigationRequested = {
      when (it) {
        LoginContracts.Effect.Navigation.ToMain -> {
          navController.popBackStack()
          navController.navigate(Home)
        }

        LoginContracts.Effect.Navigation.ToRegister -> {
          navController.popBackStack()
          navController.navigate(Register)
        }
      }
    }
  )
}
