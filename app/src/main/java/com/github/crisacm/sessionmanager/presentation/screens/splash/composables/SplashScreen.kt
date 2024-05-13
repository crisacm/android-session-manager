@file:OptIn(ExperimentalComposeUiApi::class)

package com.github.crisacm.sessionmanager.presentation.screens.splash.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.crisacm.sessionmanager.R
import com.github.crisacm.sessionmanager.presentation.base.SIDE_EFFECTS_KEY
import com.github.crisacm.sessionmanager.presentation.screens.splash.SplashContracts
import com.github.crisacm.sessionmanager.ui.theme.colorBlueLight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun SplashScreen(
    state: SplashContracts.State,
    effectFlow: Flow<SplashContracts.Effect>?,
    onEventSent: (event: SplashContracts.Event) -> Unit,
    onNavigationRequested: (navigationEffect: SplashContracts.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                SplashContracts.Effect.Navigation.ToLogin -> {
                    onNavigationRequested(SplashContracts.Effect.Navigation.ToLogin)
                }

                SplashContracts.Effect.Navigation.ToMain -> {
                    onNavigationRequested(SplashContracts.Effect.Navigation.ToMain)
                }
            }
        }?.collect()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            AnimatedVisibility(
                visible = state.isLoading,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(
                        durationMillis = 5000,
                        easing = LinearEasing
                    )
                )
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorBlueLight)
                ) {
                    val (box, text) = createRefs()

                    Box(
                        modifier = Modifier
                            .height(dimensionResource(R.dimen.start_content_size))
                            .width(dimensionResource(R.dimen.start_content_size))
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = Color.White)
                            .constrainAs(box) { centerTo(parent) },
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(36.dp),
                            imageVector = Icons.TwoTone.Lock,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.text_splash_screen),
                        color = Color.White,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(36.dp)
                            .constrainAs(text) {
                                bottom.linkTo(parent.bottom)
                                centerHorizontallyTo(parent)
                            }
                    )
                }
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
