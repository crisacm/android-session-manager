package com.github.crisacm.sessionmanager.presentation.screens.register.composables

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
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
import com.github.crisacm.sessionmanager.presentation.component.DefaultInput
import com.github.crisacm.sessionmanager.presentation.component.EmailTextField
import com.github.crisacm.sessionmanager.presentation.component.PasswordTextField
import com.github.crisacm.sessionmanager.presentation.component.LoadingButton
import com.github.crisacm.sessionmanager.presentation.screens.register.RegisterContracts
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun Register(
    state: RegisterContracts.State,
    effectFlow: Flow<RegisterContracts.Effect>?,
    onEventSent: (event: RegisterContracts.Event) -> Unit,
    onNavigationRequested: (navigationEffect: RegisterContracts.Effect.Navigation) -> Unit
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is RegisterContracts.Effect.Navigation.ToMain -> {
                    onNavigationRequested(RegisterContracts.Effect.Navigation.ToMain(effect.user))
                }

                is RegisterContracts.Effect.Navigation.ToLogin -> {
                    onNavigationRequested(RegisterContracts.Effect.Navigation.ToLogin)
                }

                is RegisterContracts.Effect.ShowSnack -> {
                    snackBarHostState.showSnackbar(effect.msg)
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
            Row(
                modifier = Modifier
                    .padding(24.dp, 36.dp, 24.dp, 0.dp)
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.clickable { onEventSent(RegisterContracts.Event.ToLogin) }
                )
                Text(
                    text = "Create Account ✨",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 0.dp)
                )
            }
            Text(
                text = "We will need some of your information, don't worry, it's just routine.",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 12.dp, 24.dp, 0.dp)
            )
            DefaultInput(
                modifier = Modifier
                    .padding(24.dp, 18.dp, 24.dp, 0.dp)
                    .fillMaxWidth(),
                textValue = name,
                placeHolder = "name",
                label = "User First Name",
                isError = (state.errorNameText != null && state.errorNameText.isError),
                isEnabled = !state.isLoading,
                supportingText = state.errorNameText?.errorMessage.toString(),
                isRequired = true,
                onTextChange = { name = it }
            )
            EmailTextField(
                modifier = Modifier
                    .padding(24.dp, 18.dp, 24.dp, 0.dp)
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
            ) {
                onEventSent(RegisterContracts.Event.Register(name, username, password))
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Already have an account ?", fontSize = 16.sp, color = Color.Gray)
                Text(
                    text = "Sign In",
                    fontSize = 16.sp,
                    color = Color.Blue,
                    modifier = Modifier
                        .padding(12.dp, 0.dp, 0.dp, 0.dp)
                        .clickable { onEventSent(RegisterContracts.Event.ToLogin) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    SessionManagerTheme {
        Register(
            state = RegisterContracts.State(
                isLoading = false,
                errorNameText = null,
                errorUserText = null,
                errorPassText = null
            ),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RegisterPreviewDark() {
    SessionManagerTheme {
        Register(
            state = RegisterContracts.State(
                isLoading = false,
                errorNameText = null,
                errorUserText = null,
                errorPassText = null
            ),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterLoadingPreview() {
    SessionManagerTheme {
        Register(
            state = RegisterContracts.State(
                isLoading = true,
                errorNameText = null,
                errorUserText = null,
                errorPassText = null
            ),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}
