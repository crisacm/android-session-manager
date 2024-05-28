package com.github.crisacm.sessionmanager.presentation.component

import android.app.UiModeManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordTextField(
    modifier: Modifier,
    textValue: String,
    placeHolder: String,
    label: String? = null,
    isError: Boolean = false,
    supportedText: String = "",
    isEnable: Boolean = true,
    isRequired: Boolean = false,
    onTextChange: (text: String) -> Unit
) {
    val uiModeManager = LocalContext.current.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    val isNightModeActive = (uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES)
    val showPassword = remember { mutableStateOf(false) }

    Column(modifier) {
        if (label != null) {
            Row(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)) {
                val textColor = if (isNightModeActive) Color.Black else Color.White
                Text(
                    text = label,
                    fontSize = 16.sp,
                    color = textColor
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
            value = textValue,
            onValueChange = onTextChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(placeHolder) },
            enabled = isEnable,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = supportedText,
                        color = Color.Red
                    )
                }
            },
            visualTransformation = if (showPassword.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (isError) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "error",
                        tint = Color.Red
                    )
                } else {
                    val icon = if (showPassword.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (showPassword.value) "Hide password" else "Show password"

                    IconButton(onClick = { showPassword.value = !showPassword.value }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordTextFieldPreview() {
    PasswordTextField(
        modifier = Modifier,
        textValue = "",
        placeHolder = "password",
        label = "Password",
        isError = false,
        supportedText = "",
        isEnable = true,
        isRequired = true,
        onTextChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordTextFieldErrorPreview() {
    PasswordTextField(
        modifier = Modifier,
        textValue = "",
        placeHolder = "password",
        label = "Password",
        isError = true,
        supportedText = "Wrong password",
        isEnable = true,
        isRequired = false,
        onTextChange = {}
    )
}
