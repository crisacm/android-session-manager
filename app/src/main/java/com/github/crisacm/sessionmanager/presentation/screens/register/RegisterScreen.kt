package com.github.crisacm.sessionmanager.presentation.screens.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.crisacm.sessionmanager.presentation.component.DefaultInput
import com.github.crisacm.sessionmanager.presentation.component.PasswordInput


@Composable
fun Register(
    onLogin: () -> Unit, onHome: () -> Unit
) {
    val context = LocalContext.current
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(24.dp, 36.dp, 24.dp, 0.dp)
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.clickable { onLogin() })

                Text(
                    text = "Create Account ✨", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 0.dp)
                )
            }

            Text(
                text = "We will need some of your information, don't worry, it's just routine.",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 12.dp, 24.dp, 0.dp)
            )

            DefaultInput(
                text = username,
                label = "Full name",
                placeHolder = "Olalekan Daramola",
                isRequired = true,
                modifier = Modifier
                    .padding(24.dp, 48.dp, 24.dp, 0.dp)
                    .fillMaxWidth(),
            ) {
                username = it
            }

            DefaultInput(
                text = username,
                label = "Email Address",
                placeHolder = "user@gmail.com",
                isRequired = true,
                modifier = Modifier
                    .padding(24.dp, 24.dp, 24.dp, 0.dp)
                    .fillMaxWidth(),
            ) {
                username = it
            }

            PasswordInput(
                text = password,
                label = "Password",
                placeHolder = "password",
                isRequired = true,
                modifier = Modifier
                    .padding(24.dp, 24.dp, 24.dp, 0.dp)
                    .fillMaxWidth()
            ) {
                password = it
            }

            Button(modifier = Modifier
                .padding(24.dp, 36.dp, 24.dp, 0.dp)
                .fillMaxWidth(), shape = RoundedCornerShape(6.dp), onClick = {
                Toast.makeText(context, "sign in clicked!", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "Sign Up")
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account ?", fontSize = 16.sp, color = Color.Gray
                )

                Text(text = "Sign In", fontSize = 16.sp, color = Color.Blue, modifier = Modifier
                    .padding(12.dp, 0.dp, 0.dp, 0.dp)
                    .clickable {
                        onLogin()
                    })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    Register({}, {})
}
