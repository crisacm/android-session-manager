package com.github.crisacm.sessionmanager.presentation.screens.splash.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.crisacm.sessionmanager.R
import com.github.crisacm.sessionmanager.presentation.screens.splash.SplashContracts
import kotlinx.coroutines.flow.Flow

@Composable
fun SplashScreen(
    state: SplashContracts.State,
    effectFlow: Flow<SplashContracts.Effect>?,
    onEventSent: (event: SplashContracts.Event) -> Unit,
    onNavigationRequested: (navigationEffect: SplashContracts.Effect.Navigation) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(
                    durationMillis = 5000,
                    easing = LinearEasing
                )
            ),
            exit = fadeOut()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(0.dp)
                    .background(Color.LightGray)
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(id = R.string.text_splash_screen),
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(24.dp),
                )
                Box(
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.start_content_size))
                        .width(dimensionResource(R.dimen.start_content_size))
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = Color.White)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        state = SplashContracts.State(true),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {}
    )
}
