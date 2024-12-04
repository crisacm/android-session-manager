package com.github.crisacm.sessionmanager.ui.feature.common

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.crisacm.sessionmanager.ui.theme.greenSuccess
import com.github.crisacm.sessionmanager.ui.theme.greenSuccessLight

@Composable
fun LoadingButton(
  modifier: Modifier,
  state: LoadingButtonState,
  idleText: String,
  onClickAction: () -> Unit
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(40.dp)
  ) {
    when (state) {
      LoadingButtonState.LOADING -> {
        AnimatedVisibility(
          visible = true,
          enter = fadeIn(),
          exit = fadeOut()
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
              CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 3.dp)
            }
          }
        }
      }

      LoadingButtonState.SUCCESS -> {
        AnimatedVisibility(
          visible = true,
          enter = fadeIn(),
          exit = fadeOut()
        ) {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .height(40.dp),
            colors = CardDefaults.cardColors(containerColor = greenSuccessLight),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(2.dp, greenSuccess)
          ) {
            Row(
              modifier = Modifier.fillMaxSize(),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = null,
                tint = greenSuccess
              )
            }
          }
        }
      }

      LoadingButtonState.IDLE -> {
        AnimatedVisibility(
          visible = true,
          enter = fadeIn(),
          exit = fadeOut()
        ) {
          Button(
            modifier = Modifier
              .fillMaxWidth()
              .height(40.dp),
            shape = RoundedCornerShape(6.dp), onClick = { onClickAction() }
          ) { Text(text = idleText) }
        }
      }
    }
  }
}

enum class LoadingButtonState {
  LOADING,
  SUCCESS,
  IDLE
}

@Preview(showBackground = true)
@Composable
fun LoadingButtonLoadingPreview() {
  LoadingButton(Modifier, LoadingButtonState.LOADING, "Click Me!") {}
}

@Preview(showBackground = true)
@Composable
fun LoadingButtonPreviewIdle() {
  LoadingButton(Modifier, LoadingButtonState.IDLE, "Click Me!") {}
}

@Preview(showBackground = true)
@Composable
fun LoadingButtonPreviewSuccess() {
  LoadingButton(Modifier, LoadingButtonState.SUCCESS, "Click Me!") {}
}
