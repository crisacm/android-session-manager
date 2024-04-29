package com.github.crisacm.sessionmanager.ui.screens.login.composables

import android.accounts.AccountManager
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
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
import coil.compose.rememberAsyncImagePainter
import com.github.crisacm.sessionmanager.ui.base.SIDE_EFFECTS_KEY
import com.github.crisacm.sessionmanager.ui.components.DefaultInput
import com.github.crisacm.sessionmanager.ui.components.PasswordInput
import com.github.crisacm.sessionmanager.ui.screens.login.LoginContracts
import com.google.android.gms.auth.GoogleAuthUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun Login(
    state: LoginContracts.State,
    effectFlow: Flow<LoginContracts.Effect>?,
    onEventSent: (event: LoginContracts.Event) -> Unit,
    onNavigationRequested: (navigationEffect: LoginContracts.Effect.Navigation) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage = "Testing snack!"
    val scope = rememberCoroutineScope()

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                LoginContracts.Effect.Failed -> {}
                LoginContracts.Effect.Navigation.ToMain -> {}
                LoginContracts.Effect.UserLogged -> {}
                LoginContracts.Effect.UserRegistered -> {}
                LoginContracts.Effect.Navigation.ToRegister -> {}
            }
        }
    }

    val context = LocalContext.current
    var username by rememberSaveable { mutableStateOf("user@gmail.com") }
    var password by rememberSaveable { mutableStateOf("Enter your password") }

    val result = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val accountName = it.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            Toast.makeText(context, "User: $accountName", Toast.LENGTH_SHORT).show()
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

            DefaultInput(
                text = username,
                label = "Email Address",
                placeHolder = "user@gmail.com",
                modifier = Modifier
                    .padding(24.dp, 48.dp, 24.dp, 0.dp)
                    .fillMaxWidth(),
            ) {
                username = it
            }

            PasswordInput(
                text = password, label = "Password", placeHolder = "password", modifier = Modifier
                    .padding(24.dp, 24.dp, 24.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                password = it
            }

            Button(modifier = Modifier
                .padding(24.dp, 36.dp, 24.dp, 0.dp)
                .fillMaxWidth(), shape = RoundedCornerShape(6.dp), onClick = {
                scope.launch {
                    snackBarHostState.showSnackbar(snackBarMessage)
                }
            }) {
                Text(text = "Sign In")
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
                        onNavigationRequested(LoginContracts.Effect.Navigation.ToMain)
                    })
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
            isError = false
        ),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {}
    )
}
