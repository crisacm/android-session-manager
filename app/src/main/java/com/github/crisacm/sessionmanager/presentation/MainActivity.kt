package com.github.crisacm.sessionmanager.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.crisacm.sessionmanager.domain.model.User
import com.github.crisacm.sessionmanager.navigation.Home
import com.github.crisacm.sessionmanager.navigation.Login
import com.github.crisacm.sessionmanager.navigation.Navigation
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme
import com.github.crisacm.sessionmanager.util.IntentNames

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SessionManagerTheme {
                var user: User? = null
                val isUserLoggedIn = when (intent.getStringExtra(IntentNames.START_DESTINATION)) {
                    IntentNames.LOGIN -> false
                    IntentNames.HOME -> {
                        user = User(
                            intent.getStringExtra(IntentNames.NAME) ?: "<Nothing>",
                            intent.getStringExtra(IntentNames.EMAIL) ?: "<Nothing>",
                            intent.getStringExtra(IntentNames.PHOTO_URL) ?: "<Nothing>"
                        )

                        true
                    }

                    else -> false
                }

                Navigation(isUserLoggedIn, user)
            }
        }
    }
}
