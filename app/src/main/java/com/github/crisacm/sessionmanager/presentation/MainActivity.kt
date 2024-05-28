package com.github.crisacm.sessionmanager.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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
                val startDestination = when (intent.getStringExtra(IntentNames.START_DESTINATION)) {
                    IntentNames.LOGIN -> Login
                    IntentNames.HOME -> Home(
                        intent.getStringExtra(IntentNames.NAME),
                        intent.getStringExtra(IntentNames.EMAIL),
                        intent.getStringExtra(IntentNames.PHOTO_URL)
                    )

                    else -> Login
                }

                Navigation(startDestination)
            }
        }
    }
}
