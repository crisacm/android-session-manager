package com.github.crisacm.sessionmanager.ui.feature.login.composables

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.crisacm.sessionmanager.R
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme

@Suppress("FunctionNaming")
@Composable
fun CircleButton(
  modifier: Modifier = Modifier,
  isEnable: Boolean = true,
  painter: Painter,
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
      painter = painter,
      contentDescription = null,
      modifier = Modifier.size(30.dp)
    )
  }
}

@Suppress("FunctionNaming")
@Preview(showBackground = true)
@Composable
fun CircleButtonPreview() {
  SessionManagerTheme {
    CircleButton(
      painter = painterResource(R.drawable.google)
    ) {}
  }
}

@Suppress("FunctionNaming")
@Preview(
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CircleButtonPreviewDark() {
  SessionManagerTheme {
    CircleButton(
      painter = painterResource(R.drawable.facebook)
    ) {}
  }
}
