package com.github.crisacm.sessionmanager.presentation.screens.home.composables

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
import com.github.crisacm.sessionmanager.presentation.base.SIDE_EFFECTS_KEY
import com.github.crisacm.sessionmanager.presentation.component.AppBar
import com.github.crisacm.sessionmanager.presentation.component.AppBarAction
import com.github.crisacm.sessionmanager.presentation.screens.home.HomeContracts
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
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
            }
        }?.collect()
    }

    Scaffold(
        topBar = {
            AppBar(title = "Home", actions = {
                AppBarAction(imageVector = Icons.Outlined.MoreVert, onClick = {
                    Toast.makeText(context, "Menu click!", Toast.LENGTH_SHORT).show()
                })
            })
        }, modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = "Hello, Welcome back 👋🏻",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 48.dp, 24.dp, 0.dp)
            )
            Text(
                text = "We need some information, its yust for ",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 6.dp, 24.dp, 0.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 36.dp, 0.dp, 24.dp)
            ) {
                Card(
                    modifier = Modifier.size(84.dp),
                    shape = CircleShape
                ) {
                    if ("user.photoUrl".isEmpty()) {
                        Image(
                            painter = painterResource(R.drawable.github),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        )
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("user.photoUrl")
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.github),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        )
                    }
                }
                Column(modifier = Modifier.padding(24.dp, 0.dp, 24.dp, 0.dp)) {
                    Row {
                        Text(text = "Name:", fontWeight = FontWeight.Bold)
                        Text(
                            text = "user.name".ifEmpty { "<Nothing>" },
                            modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 0.dp)
                        )
                    }
                    Row {
                        Text(text = "Email:", fontWeight = FontWeight.Bold)
                        Text(
                            text = "user.email".ifEmpty { "<Nothing>" },
                            modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 0.dp)
                        )
                    }
                }
            }
            Button(
                onClick = { onEventSent(HomeContracts.Event.ToLogin) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 36.dp, 24.dp, 0.dp),
                shape = RoundedCornerShape(6.dp),
            ) { Text(text = "Logout") }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    SessionManagerTheme {
        Home(
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomePreviewDark() {
    SessionManagerTheme {
        Home(
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}
