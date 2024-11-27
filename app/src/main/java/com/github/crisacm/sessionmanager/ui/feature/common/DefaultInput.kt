package com.github.crisacm.sessionmanager.ui.feature.common

import android.app.UiModeManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DefaultInput(
    modifier: Modifier,
    textValue: String,
    placeHolder: String,
    label: String? = null,
    isError: Boolean = false,
    supportingText: String = "",
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    onTextChange: (String) -> Unit
) {
    val uiModeManager = LocalContext.current.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    val isNightModeActive = (uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES)

    Column(modifier) {
        if (label != null) {
            Row(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)) {
                val textColor = if (isNightModeActive) Color.Black else Color.White
                Text(
                    text = label,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                if (isRequired) {
                    Text(
                        text = "*",
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp)
                    )
                }
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textValue,
            onValueChange = { onTextChange(it) },
            placeholder = { Text(placeHolder) },
            enabled = isEnabled,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = supportingText,
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
}

@Preview(showBackground = true)
@Composable
fun DefaultInputPreview() {
    DefaultInput(
        modifier = Modifier,
        textValue = "",
        placeHolder = "user name",
        label = "Enter First Name",
        isError = false,
        isEnabled = true,
        supportingText = "",
        onTextChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultInputErrorPreview() {
    DefaultInput(
        modifier = Modifier,
        textValue = "invalid_name",
        placeHolder = "user name",
        label = "Enter First Name",
        isError = true,
        isEnabled = true,
        supportingText = "Please enter valid email",
        onTextChange = {}
    )
}
