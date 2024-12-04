package com.github.crisacm.sessionmanager.ui.feature.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Suppress("LongParameterList", "FunctionNaming")
@Composable
fun DefaultInput(
  modifier: Modifier,
  textValue: String,
  placeHolder: String,
  isError: Boolean = false,
  errorText: String = "",
  isEnabled: Boolean = true,
  onTextChange: (String) -> Unit
) {
  OutlinedTextField(
    modifier = modifier.fillMaxWidth(),
    value = textValue,
    onValueChange = { onTextChange(it) },
    placeholder = { Text(placeHolder) },
    enabled = isEnabled,
    isError = isError,
    supportingText = {
      if (isError) {
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = errorText,
          color = Color.Red
        )
      }
    },
    trailingIcon = {
      if (isError) {
        Icon(
          imageVector = Icons.Filled.Error,
          contentDescription = "error",
          tint = Color.Red
        )
      }
    },
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
  )
}

@Suppress("FunctionNaming")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultInputPreview() {
  DefaultInput(
    modifier = Modifier,
    textValue = "",
    placeHolder = "user name",
    isError = false,
    isEnabled = true,
    errorText = "",
    onTextChange = {}
  )
}

@Suppress("FunctionNaming")
@Preview(showBackground = true)
@Composable
fun DefaultInputErrorPreview() {
  DefaultInput(
    modifier = Modifier,
    textValue = "invalid_name",
    placeHolder = "user name",
    isError = true,
    isEnabled = true,
    errorText = "Please enter valid email",
    onTextChange = {}
  )
}
