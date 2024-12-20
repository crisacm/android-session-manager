package com.github.crisacm.sessionmanager.ui.feature.register.composables

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.crisacm.sessionmanager.ui.base.SIDE_EFFECTS_KEY
import com.github.crisacm.sessionmanager.ui.feature.common.DefaultInput
import com.github.crisacm.sessionmanager.ui.feature.common.EmailTextField
import com.github.crisacm.sessionmanager.ui.feature.common.LoadingButton
import com.github.crisacm.sessionmanager.ui.feature.common.LoadingButtonState
import com.github.crisacm.sessionmanager.ui.feature.common.PasswordTextField
import com.github.crisacm.sessionmanager.ui.feature.register.RegisterContracts
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Suppress("LongMethod", "FunctionNaming", "CyclomaticComplexMethod")
@Composable
fun Register(
  state: RegisterContracts.State,
  effectFlow: Flow<RegisterContracts.Effect>?,
  onEventSent: (event: RegisterContracts.Event) -> Unit,
  onNavigationRequested: (navigationEffect: RegisterContracts.Effect.Navigation) -> Unit
) {
  val snackBarHostState = remember { SnackbarHostState() }

  var name by remember { mutableStateOf("") }
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  LaunchedEffect(SIDE_EFFECTS_KEY) {
    effectFlow?.onEach { effect ->
      when (effect) {
        is RegisterContracts.Effect.Navigation.ToMain -> {
          onNavigationRequested(RegisterContracts.Effect.Navigation.ToMain)
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
          .padding(start = 24.dp, top = 36.dp, end = 24.dp)
          .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = null,
          modifier = Modifier.clickable {
            if (!state.isLoading && !state.isSuccess) onEventSent(RegisterContracts.Event.ToLogin)
          }
        )
        Text(
          text = "Create Account ✨",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        )
      }
      Text(
        text = "It's not that we don't trust you, but we will need some information from you.",
        fontSize = 16.sp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 16.dp)
      )
      Row(modifier = Modifier.padding(start = 24.dp, top = 18.dp, end = 24.dp, bottom = 8.dp)) {
        Text(text = "User First Name", fontSize = 16.sp)
        Text(
          text = "*",
          fontSize = 16.sp,
          color = Color.Red,
          modifier = Modifier.padding(start = 6.dp)
        )
      }
      DefaultInput(
        modifier = Modifier
          .padding(start = 24.dp, end = 24.dp)
          .fillMaxWidth(),
        textValue = name,
        placeHolder = "name",
        isError = (state.errorNameText != null),
        isEnabled = (!state.isLoading && !state.isSuccess),
        errorText = state.errorNameText?.toString() ?: "",
        onTextChange = { name = it }
      )
      Row(modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp, bottom = 8.dp)) {
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
          .padding(start = 24.dp, end = 24.dp)
          .fillMaxWidth(),
        textValue = username,
        placeHolder = "user@mail.com",
        isError = (state.errorUserText != null),
        isEnabled = (!state.isLoading && !state.isSuccess),
        errorText = state.errorUserText?.toString() ?: "",
        onTextChange = { username = it }
      )
      Row(modifier = Modifier.padding(start = 24.dp, top = 12.dp, end = 24.dp, bottom = 8.dp)) {
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
        isError = (state.errorPassText != null),
        errorText = state.errorPassText?.toString() ?: "",
        isEnable = (!state.isLoading && !state.isSuccess),
        onTextChange = { password = it }
      )
      LoadingButton(
        modifier = Modifier.padding(24.dp, 36.dp, 24.dp, 0.dp),
        state = when {
          state.isLoading && !state.isSuccess -> LoadingButtonState.LOADING
          !state.isLoading && state.isSuccess -> LoadingButtonState.SUCCESS
          else -> LoadingButtonState.IDLE
        },
        idleText = "Sign Up",
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
            .clickable { if (!state.isLoading && !state.isSuccess) onEventSent(RegisterContracts.Event.ToLogin) }
        )
      }
    }
  }
}

@Suppress("FunctionNaming")
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

@Suppress("FunctionNaming")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
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

@Suppress("FunctionNaming")
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
