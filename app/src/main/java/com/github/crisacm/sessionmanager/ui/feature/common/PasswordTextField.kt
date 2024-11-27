package com.github.crisacm.sessionmanager.ui.feature.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PasswordTextField(
    modifier: Modifier,
    textValue: String,
    placeHolder: String,
    isError: Boolean = false,
    supportedText: String = "",
    isEnable: Boolean = true,
    onTextChange: (text: String) -> Unit
) {
    val showPassword = remember { mutableStateOf(false) }

    Column(modifier) {
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
        isError = false,
        supportedText = "",
        isEnable = true,
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
        isError = true,
        supportedText = "Wrong password",
        isEnable = true,
        onTextChange = {}
    )
}
