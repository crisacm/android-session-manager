package com.github.crisacm.sessionmanager.ui.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.crisacm.sessionmanager.ui.navigation.Home
import com.github.crisacm.sessionmanager.ui.navigation.Login
import com.github.crisacm.sessionmanager.ui.navigation.Navigation
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme
import com.github.crisacm.sessionmanager.util.IntentNames

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SessionManagerTheme {
        val destination: Any = when (intent.getStringExtra(IntentNames.START_DESTINATION)) {
          IntentNames.LOGIN -> Login
          IntentNames.HOME -> Home
          else -> Login
        }

        Navigation(destination)
      }
    }
  }
}
