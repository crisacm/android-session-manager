package com.github.crisacm.sessionmanager.ui.screens.login.composables

import android.accounts.AccountManager
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.crisacm.sessionmanager.ui.base.SIDE_EFFECTS_KEY
import com.github.crisacm.sessionmanager.ui.components.DefaultInput
import com.github.crisacm.sessionmanager.ui.components.PasswordInput
import com.github.crisacm.sessionmanager.ui.screens.login.LoginContracts
import com.google.android.gms.auth.GoogleAuthUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun Login(
    state: LoginContracts.State,
    effectFlow: Flow<LoginContracts.Effect>?,
    onEventSent: (event: LoginContracts.Event) -> Unit,
    onNavigationRequested: (navigationEffect: LoginContracts.Effect.Navigation) -> Unit
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                LoginContracts.Effect.FailSingIn -> {
                    snackBarHostState.showSnackbar("Failed to sing in")
                }

                LoginContracts.Effect.Navigation.ToMain -> {
                    onNavigationRequested(LoginContracts.Effect.Navigation.ToRegister)
                }

                LoginContracts.Effect.UserLogged -> {}
                LoginContracts.Effect.UserRegistered -> {}
                LoginContracts.Effect.Navigation.ToRegister -> {
                    onNavigationRequested(LoginContracts.Effect.Navigation.ToRegister)
                }

                LoginContracts.Effect.PassEmpty -> {
                    snackBarHostState.showSnackbar("Please enter a password")
                }

                LoginContracts.Effect.UserEmpty -> {
                    snackBarHostState.showSnackbar("Please enter a username")
                }

                LoginContracts.Effect.WrongPass -> {
                    snackBarHostState.showSnackbar("Worng password")
                }
            }
        }?.collect()
    }

    val result = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val accountName = it.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            onEventSent(LoginContracts.Event.SingInWithGoogle(accountName ?: ""))
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = (snackBarHostState)) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Log.i("Event", "State value: $state")

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(
                text = "Hello, Welcome back 👋🏻",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 36.dp, 24.dp, 0.dp)
            )
            Text(
                text = "We need some information, its yust for ",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 6.dp, 24.dp, 0.dp)
            )

            DefaultInput(
                text = username,
                label = "Email Address",
                placeHolder = "user@gmail.com",
                modifier = Modifier
                    .padding(24.dp, 48.dp, 24.dp, 0.dp)
                    .fillMaxWidth(),
                isError = state.isErrorUserEmpty,
                supportedErrorText = "Enter a username",
                isEnable = !state.isLoading
            ) {
                username = it
            }

            PasswordInput(
                text = password,
                label = "Password",
                placeHolder = "password",
                modifier = Modifier
                    .padding(24.dp, 24.dp, 24.dp, 0.dp)
                    .fillMaxWidth(),
                isError = state.isErrorPassEmpty,
                supportedErrorText = "Enter an password",
                isEnable = !state.isLoading
            ) {
                password = it
            }

            LoginButton(
                modifier = Modifier.padding(24.dp, 36.dp, 24.dp, 0.dp),
                loading = state.isLoading
            ) {
                onEventSent(LoginContracts.Event.SingIn(username, password))
            }

            Row(
                modifier = Modifier.padding(0.dp, 36.dp, 0.dp, 36.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    color = Color.LightGray, thickness = 1.dp, modifier = Modifier
                        .padding(24.dp, 0.dp, 24.dp, 0.dp)
                        .weight(1f)
                )
                Text(text = "Or sign in With", color = Color.Gray)
                Divider(
                    color = Color.LightGray, thickness = 1.dp, modifier = Modifier
                        .padding(24.dp, 0.dp, 24.dp, 0.dp)
                        .weight(1f)
                )
            }

            GoogleButton {
                val intent = AccountManager
                    .newChooseAccountIntent(
                        null,
                        null,
                        arrayOf(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE),
                        null,
                        null,
                        null,
                        null
                    )

                result.launch(intent)
            }

            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Do not have an account ?", fontSize = 16.sp, color = Color.Gray)
                Text(text = "Create now", fontSize = 16.sp, color = Color.Blue, modifier = Modifier
                    .padding(12.dp, 0.dp, 0.dp, 0.dp)
                    .clickable {
                        Log.i("Event", "Register click")
                        onEventSent(LoginContracts.Event.Register)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(
        state = LoginContracts.State(
            isLoading = false,
            isErrorUserEmpty = false,
            isErrorPassEmpty = false
        ),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginLoadingPreview() {
    Login(
        state = LoginContracts.State(
            isLoading = true,
            isErrorUserEmpty = false,
            isErrorPassEmpty = false
        ),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {}
    )
}
