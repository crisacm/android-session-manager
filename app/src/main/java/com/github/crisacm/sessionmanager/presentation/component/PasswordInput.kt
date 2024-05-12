package com.github.crisacm.sessionmanager.presentation.component

import android.util.Log
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordInput(
    text: String,
    label: String,
    placeHolder: String,
    isRequired: Boolean = false,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportedErrorText: String = "",
    isEnable: Boolean = true,
    onTextChange: (text: String) -> Unit
) {
    val showPassword = remember { mutableStateOf(false) }

    Column(modifier) {
        Row(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)) {
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
        OutlinedTextField(
            value = text,
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
                        text = supportedErrorText,
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
fun PasswordInputPreview() {
    PasswordInput(text = "", placeHolder = "password", label = "Password") {
        Log.i("PasswordInput", it)
    }
}
