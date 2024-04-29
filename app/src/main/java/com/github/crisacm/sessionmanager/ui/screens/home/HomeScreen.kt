package com.github.crisacm.sessionmanager.ui.screens.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.crisacm.sessionmanager.ui.components.AppBar
import com.github.crisacm.sessionmanager.ui.components.AppBarAction

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(onLogout: () -> Unit) {
    val context = LocalContext.current

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

            Button(
                onClick = { onLogout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 36.dp, 24.dp, 0.dp),
                shape = RoundedCornerShape(6.dp),
            ) {
                Text(text = "Cerrar sesión")
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    Home {}
}
