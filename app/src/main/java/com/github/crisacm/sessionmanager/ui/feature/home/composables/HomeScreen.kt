package com.github.crisacm.sessionmanager.ui.feature.home.composables

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.crisacm.sessionmanager.R
import com.github.crisacm.sessionmanager.ui.base.SIDE_EFFECTS_KEY
import com.github.crisacm.sessionmanager.ui.feature.common.LoadingButton
import com.github.crisacm.sessionmanager.ui.feature.common.LoadingButtonState
import com.github.crisacm.sessionmanager.ui.feature.home.HomeContracts
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Suppress("FunctionNaming", "LongMethod", "CyclomaticComplexMethod")
@Composable
fun Home(
  state: HomeContracts.State,
  effectFlow: Flow<HomeContracts.Effect>?,
  onEventSent: (event: HomeContracts.Event) -> Unit,
  onNavigationRequested: (navigationEffect: HomeContracts.Effect.Navigation) -> Unit
) {
  val context = LocalContext.current

  LaunchedEffect(SIDE_EFFECTS_KEY) {
    effectFlow?.onEach {
      when (it) {
        HomeContracts.Effect.Navigation.ToLogin -> {
          onNavigationRequested(HomeContracts.Effect.Navigation.ToLogin)
        }

        is HomeContracts.Effect.ShowToast -> {
          Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
        }
      }
    }?.collect()
  }

  LaunchedEffect(Unit) {
    onEventSent(HomeContracts.Event.Start)
  }

  Scaffold(
    modifier = Modifier.fillMaxSize()
  ) { paddingValues ->
    Column(modifier = Modifier.padding(paddingValues)) {
      Text(
        text = "We are glad to have you back 👻",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 24.dp, top = 48.dp, end = 24.dp)
      )
      Text(
        text = "It's not that we didn't know, we just wanted to make sure it was you.",
        fontSize = 16.sp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 24.dp, top = 6.dp, end = 24.dp)
      )
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Color.Gray)
      ) {
        if (state.isLoading && state.user == null) {
          CircularProgressIndicator(
            Modifier
              .padding(24.dp)
              .align(Alignment.CenterHorizontally)
          )
        }
        if (!state.isLoading && state.user != null) {
          SectionUserInfo(
            photoUrl = state.user?.photoUrl,
            name = state.user?.displayName ?: "",
            email = state.user?.email ?: "",
            phone = state.user?.phoneNumber ?: ""
          )
        }
        if (!state.isLoading && state.user == null) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "No user info found 🙁", fontSize = 16.sp)
          }
        }
      }
      Spacer(modifier = Modifier.weight(1f))
      LoadingButton(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        state = state.buttonState,
        idleText = "Logout"
      ) {
        onEventSent(HomeContracts.Event.ToLogin)
      }
    }
  }
}

@Suppress("FunctionNaming", "LongMethod")
@Preview
@Composable
fun HomePreview() {
  SessionManagerTheme {
    Home(
      state = HomeContracts.State(isLoading = false, user = null),
      effectFlow = null,
      onEventSent = {},
      onNavigationRequested = {}
    )
  }
}

@Suppress("FunctionNaming", "LongMethod")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomePreviewDark() {
  SessionManagerTheme {
    Home(
      state = HomeContracts.State(isLoading = false),
      effectFlow = null,
      onEventSent = {},
      onNavigationRequested = {}
    )
  }
}
