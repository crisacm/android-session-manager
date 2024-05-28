package com.github.crisacm.sessionmanager.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.crisacm.sessionmanager.presentation.base.SIDE_EFFECTS_KEY
import com.github.crisacm.sessionmanager.presentation.screens.splash.SplashContracts
import com.github.crisacm.sessionmanager.presentation.screens.splash.SplashViewModel
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme
import com.github.crisacm.sessionmanager.util.IntentNames
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

class SplashActivity : ComponentActivity() {

    private var keepOnScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !keepOnScreen }

        actionBar?.hide()
        setContent {
            SessionManagerTheme {
                val viewModel = getViewModel<SplashViewModel>()
                val context = LocalContext.current

                LaunchedEffect(SIDE_EFFECTS_KEY) {
                    viewModel.effect.onEach {
                        Intent(context, MainActivity::class.java)
                            .apply {
                                val destination = when (it) {
                                    SplashContracts.Effect.Navigation.ToLogin -> IntentNames.LOGIN
                                    is SplashContracts.Effect.Navigation.ToMain -> {
                                        putExtra(IntentNames.NAME, it.user.name)
                                        putExtra(IntentNames.EMAIL, it.user.email)
                                        putExtra(IntentNames.PHOTO_URL, it.user.photoUrl)

                                        IntentNames.HOME
                                    }
                                }

                                putExtra(IntentNames.START_DESTINATION, destination)
                            }
                            .also { intent ->
                                keepOnScreen = false
                                startActivity(intent)
                                finish()
                            }
                    }.collect()
                }
            }
        }
    }
}
