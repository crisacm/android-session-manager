package com.github.crisacm.sessionmanager.presentation.screens.login.composables

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.github.crisacm.sessionmanager.R
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    isEnable: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier.size(56.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.LightGray),
        contentPadding = PaddingValues(0.dp),
        enabled = isEnable,
        onClick = onClick
    ) {
        Image(
            painter = painterResource(R.drawable.google),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoogleButtonPreview() {
    SessionManagerTheme {
        GoogleButton {}
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun GoogleButtonPreviewDark() {
    SessionManagerTheme {
        GoogleButton {}
    }
}
