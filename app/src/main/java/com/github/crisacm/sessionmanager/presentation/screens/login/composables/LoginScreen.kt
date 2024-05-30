package com.github.crisacm.sessionmanager.presentation.screens.login.composables

import android.app.Activity.RESULT_OK
import android.content.res.Configuration
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
import com.github.crisacm.sessionmanager.presentation.base.SIDE_EFFECTS_KEY
import com.github.crisacm.sessionmanager.presentation.component.EmailTextField
import com.github.crisacm.sessionmanager.presentation.component.LoadingButton
import com.github.crisacm.sessionmanager.presentation.component.PasswordTextField
import com.github.crisacm.sessionmanager.presentation.screens.login.LoginContracts
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun LoginScreen(
    state: LoginContracts.State,
    effectFlow: Flow<LoginContracts.Effect>?,
    onEventSent: (event: LoginContracts.Event) -> Unit,
    onNavigationRequested: (navigationEffect: LoginContracts.Effect.Navigation) -> Unit
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    var username by rememberSaveable { mutableStateOf("c.a.c.m997@gmail.com") }
    var password by rememberSaveable { mutableStateOf("123456") }

    val result = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        if (it.resultCode == RESULT_OK) {
            onEventSent(LoginContracts.Event.ManageSignInResult(it.data))
        }
    }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is LoginContracts.Effect.Navigation.ToMain -> {
                    onNavigationRequested(LoginContracts.Effect.Navigation.ToMain(effect.user))
                }

                is LoginContracts.Effect.Navigation.ToRegister -> {
                    onNavigationRequested(LoginContracts.Effect.Navigation.ToRegister)
                }

                is LoginContracts.Effect.ShowSnack -> {
                    snackBarHostState.showSnackbar(effect.msg)
                }
                
                is LoginContracts.Effect.LaunchSelectGoogleAccount -> {
                    result.launch(effect.intentSenderRequest)
                }
            }
        }?.collect()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = (snackBarHostState)) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
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
            EmailTextField(
                modifier = Modifier
                    .padding(24.dp, 48.dp, 24.dp, 0.dp)
                    .fillMaxWidth(),
                textValue = username,
                placeHolder = "user@mail.com",
                label = "Email Address",
                isError = (state.errorUserText != null && state.errorUserText.isError),
                isEnabled = !state.isLoading,
                supportingText = state.errorUserText?.errorMessage.toString(),
                isRequired = true,
                onEmailChange = { username = it }
            )
            PasswordTextField(
                modifier = Modifier
                    .padding(24.dp, 12.dp, 24.dp, 0.dp)
                    .fillMaxWidth(),
                textValue = password,
                placeHolder = "password@123",
                label = "Password",
                isError = (state.errorPassText != null && state.errorPassText.isError),
                supportedText = state.errorPassText?.errorMessage.toString(),
                isEnable = !state.isLoading,
                isRequired = true,
                onTextChange = { password = it }
            )
            LoadingButton(
                modifier = Modifier.padding(24.dp, 36.dp, 24.dp, 0.dp),
                loading = state.isLoading
            ) { onEventSent(LoginContracts.Event.SingIn(username, password)) }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                GoogleButton(
                    isEnable = !state.isLoading,
                    onClick = { onEventSent(LoginContracts.Event.SingInWGoogle) }
                )
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
                    .clickable { onEventSent(LoginContracts.Event.Register) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SessionManagerTheme {
        LoginScreen(
            state = LoginContracts.State(isLoading = false),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreviewDark() {
    SessionManagerTheme {
        LoginScreen(
            state = LoginContracts.State(isLoading = false),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    SessionManagerTheme {
        LoginScreen(
            state = LoginContracts.State(isLoading = true),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}
