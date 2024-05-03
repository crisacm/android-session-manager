package com.github.crisacm.sessionmanager.ui.screens.login.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginButton(
    modifier: Modifier,
    loading: Boolean,
    onClickAction: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(2.dp, Color.LightGray)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Logging ...", modifier = Modifier.padding(end = 12.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 3.dp
                )
            }
        }
        AnimatedVisibility(
            visible = !loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(6.dp), onClick = { onClickAction() }
            ) { Text(text = "Sign In") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginButtonLoadingPreview() {
    LoginButton(Modifier, true) {}
}

@Preview(showBackground = true)
@Composable
fun LoginButtonPreview() {
    LoginButton(Modifier, false) {}
}
