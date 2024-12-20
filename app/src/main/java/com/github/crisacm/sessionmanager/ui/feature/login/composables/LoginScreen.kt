package com.github.crisacm.sessionmanager.ui.feature.login.composables

import android.app.Activity.RESULT_OK
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.crisacm.sessionmanager.R
import com.github.crisacm.sessionmanager.ui.base.SIDE_EFFECTS_KEY
import com.github.crisacm.sessionmanager.ui.feature.common.EmailTextField
import com.github.crisacm.sessionmanager.ui.feature.common.LoadingButton
import com.github.crisacm.sessionmanager.ui.feature.common.LoadingButtonState
import com.github.crisacm.sessionmanager.ui.feature.common.PasswordTextField
import com.github.crisacm.sessionmanager.ui.feature.login.LoginContracts
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme
import com.github.crisacm.sessionmanager.ui.theme.TextLinkColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Suppress("LongMethod", "FunctionNaming")
@Composable
fun LoginScreen(
  state: LoginContracts.State,
  effectFlow: Flow<LoginContracts.Effect>?,
  onEventSent: (event: LoginContracts.Event) -> Unit,
  onNavigationRequested: (navigationEffect: LoginContracts.Effect.Navigation) -> Unit
) {
  val snackBarHostState = remember { SnackbarHostState() }

  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  val result = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
    if (it.resultCode == RESULT_OK) {
      onEventSent(LoginContracts.Event.HandleSignInRequest(it.data))
    } else {
      onEventSent(LoginContracts.Event.HandleSignInRequest(null))
    }
  }

  LaunchedEffect(SIDE_EFFECTS_KEY) {
    effectFlow?.onEach { effect ->
      when (effect) {
        is LoginContracts.Effect.Navigation.ToMain -> {
          onNavigationRequested(LoginContracts.Effect.Navigation.ToMain)
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
          .padding(start = 24.dp, top = 36.dp, end = 24.dp, bottom = 0.dp)
      )
      Text(
        text = "We need some data, it's just routine.",
        fontSize = 16.sp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 24.dp, top = 6.dp, end = 24.dp)
      )
      Row(modifier = Modifier.padding(start = 24.dp, top = 48.dp, end = 24.dp, bottom = 8.dp)) {
        Text(text = "Email Address", fontSize = 16.sp)
        Text(
          text = "*",
          fontSize = 16.sp,
          color = Color.Red,
          modifier = Modifier.padding(start = 6.dp)
        )
      }
      EmailTextField(
        modifier = Modifier
          .padding(start = 24.dp, top = 0.dp, end = 24.dp, bottom = 0.dp)
          .fillMaxWidth(),
        textValue = username,
        placeHolder = "user@mail.com",
        isError = (state.errorUserText != null && state.errorUserText.isError),
        isEnabled = (!state.isLoading && !state.isSuccess),
        errorText = state.errorUserText?.errorMessage.toString(),
        onTextChange = { username = it }
      )
      Row(modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 8.dp)) {
        Text(text = "Password", fontSize = 16.sp)
        Text(
          text = "*",
          fontSize = 16.sp,
          color = Color.Red,
          modifier = Modifier.padding(start = 6.dp)
        )
      }
      PasswordTextField(
        modifier = Modifier
          .padding(start = 24.dp, end = 24.dp)
          .fillMaxWidth(),
        textValue = password,
        placeHolder = "password@123",
        isError = (state.errorPassText != null && state.errorPassText.isError),
        errorText = state.errorPassText?.errorMessage.toString(),
        isEnable = (!state.isLoading && !state.isSuccess),
        onTextChange = { password = it }
      )
      LoadingButton(
        modifier = Modifier.padding(start = 24.dp, top = 36.dp, end = 24.dp),
        state = when {
          state.isLoading && !state.isSuccess -> LoadingButtonState.LOADING
          !state.isLoading && state.isSuccess -> LoadingButtonState.SUCCESS
          else -> LoadingButtonState.IDLE
        },
        idleText = "Sign In",
      ) { onEventSent(LoginContracts.Event.SingIn(username, password)) }
      Row(
        modifier = Modifier.padding(top = 36.dp, end = 0.dp, bottom = 36.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Divider(
          color = Color.LightGray, thickness = 1.dp, modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
            .weight(1f)
        )
        Text(text = "Or sign in With", color = Color.Gray)
        Divider(
          color = Color.LightGray, thickness = 1.dp, modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
            .weight(1f)
        )
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        CircleButton(
          isEnable = !state.isLoading,
          painter = painterResource(R.drawable.google),
          onClick = {
            if (!state.isLoading && !state.isSuccess) onEventSent(LoginContracts.Event.SingInWGoogle)
          }
        )
        /*
        CircleButton(
          modifier = Modifier.padding(start = 24.dp),
          isEnable = !state.isLoading,
          painter = painterResource(R.drawable.facebook),
          onClick = {
            if (!state.isLoading && !state.isSuccess) onEventSent(LoginContracts.Event.SingInWFacebook)
          }
        )
        */
      }
      Spacer(modifier = Modifier.weight(1f))
      Row(
        modifier = Modifier
          .padding(24.dp)
          .fillMaxWidth(), horizontalArrangement = Arrangement.Center
      ) {
        Text(text = "Do not have an account ?", fontSize = 16.sp, color = Color.Gray)
        val textLinkColor = if (isSystemInDarkTheme()) TextLinkColor else Color.Blue
        Text(text = "Create now", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textLinkColor,
          modifier = Modifier
            .padding(start = 12.dp)
            .clickable {
              if (!state.isLoading && !state.isSuccess) onEventSent(LoginContracts.Event.Register)
            }
        )
      }
    }
  }
}

@Suppress("FunctionNaming")
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  SessionManagerTheme {
    LoginScreen(
      state = LoginContracts.State(),
      effectFlow = null,
      onEventSent = {},
      onNavigationRequested = {}
    )
  }
}

@Suppress("FunctionNaming")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreviewDark() {
  SessionManagerTheme {
    LoginScreen(
      state = LoginContracts.State(),
      effectFlow = null,
      onEventSent = {},
      onNavigationRequested = {}
    )
  }
}
