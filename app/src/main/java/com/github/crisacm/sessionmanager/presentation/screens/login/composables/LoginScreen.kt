package com.github.crisacm.sessionmanager.presentation.screens.login.composables

import android.app.Activity
import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
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
import com.github.crisacm.sessionmanager.R
import com.github.crisacm.sessionmanager.presentation.base.SIDE_EFFECTS_KEY
import com.github.crisacm.sessionmanager.presentation.component.EmailTextField
import com.github.crisacm.sessionmanager.presentation.component.LoadingButton
import com.github.crisacm.sessionmanager.presentation.component.PasswordTextField
import com.github.crisacm.sessionmanager.presentation.screens.login.LoginContracts
import com.github.crisacm.sessionmanager.presentation.screens.login.googleSign.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.sign


@Composable
fun LoginScreen(
    state: LoginContracts.State,
    effectFlow: Flow<LoginContracts.Effect>?,
    onEventSent: (event: LoginContracts.Event) -> Unit,
    onNavigationRequested: (navigationEffect: LoginContracts.Effect.Navigation) -> Unit
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    var googleSignIn by remember { mutableStateOf<GoogleSignInAccount?>(null) }

    var username by rememberSaveable { mutableStateOf("c.a.c.m997@gmail.com") }
    var password by rememberSaveable { mutableStateOf("123456") }

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
            }
        }?.collect()
    }

    val googleAuthUiClient by lazy { GoogleAuthUiClient(context, Identity.getSignInClient(context)) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        if (it.resultCode == RESULT_OK) {
            CoroutineScope(Dispatchers.IO).launch {
                val signInResult = googleAuthUiClient.signInWithIntent(it.data ?: return@launch)
                onEventSent(LoginContracts.Event.SingInWithGoogle(signInResult))
            }
        }
    }

    val result = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            task.addOnSuccessListener { account ->
                if (account != null) {
                    googleSignIn = account
                    // onEventSent(LoginContracts.Event.SingInWithGoogle(account))
                }
            }
        } else {
            Timber.i("Can't get data form activity result")
        }
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

            GoogleButton(!state.isLoading) {
                /*
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.firebase_auth_server_key))
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)

                result.launch(googleSignInClient.signInIntent)
                */

                CoroutineScope(Dispatchers.IO).launch {
                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
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
    LoginScreen(
        state = LoginContracts.State(
            isSplashVisible = false,
            isLoading = false,
            errorUserText = null,
            errorPassText = null
        ),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    LoginScreen(
        state = LoginContracts.State(
            isSplashVisible = false,
            isLoading = true,
            errorUserText = null,
            errorPassText = null
        ),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {}
    )
}
