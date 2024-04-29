package com.github.crisacm.sessionmanager.ui.screens.login.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun GoogleButton(
    onClick: () -> Unit
) {
    Button(modifier = Modifier
        .padding(24.dp, 0.dp, 24.dp, 0.dp)
        .fillMaxWidth(), colors = ButtonDefaults.buttonColors(
        containerColor = Color.White
    ), border = BorderStroke(2.dp, Color.LightGray), shape = RoundedCornerShape(4.dp), onClick = {
        onClick()
    }) {
        val imgUrl = "https://imagepng.org/wp-content/uploads/2019/08/google-icon-1.png"

        Image(painter = rememberAsyncImagePainter(imgUrl), contentDescription = null, modifier = Modifier.size(24.dp))
        Text(text = "Google", color = Color.Gray, modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 0.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GoogleButtonPreview() {
    GoogleButton {}
}
