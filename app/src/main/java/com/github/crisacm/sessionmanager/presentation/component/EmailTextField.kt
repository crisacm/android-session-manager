package com.github.crisacm.sessionmanager.presentation.component

import androidx.compose.foundation.layout.Column
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

@Composable
fun EmailTextField(
    modifier: Modifier,
    textValue: String,
    placeHolder: String,
    isError: Boolean = false,
    supportingText: String = "",
    isEnabled: Boolean = true,
    onEmailChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textValue,
            onValueChange = { onEmailChange(it) },
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
fun EmailTextFieldPreview() {
    EmailTextField(
        modifier = Modifier,
        textValue = "c.a.c.m997@gmail.com",
        placeHolder = "user@mail.com",
        isError = false,
        isEnabled = true,
        onEmailChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EmailTextFieldErrorPreview() {
    EmailTextField(
        modifier = Modifier,
        textValue = "invalid_value",
        placeHolder = "user@mail.com",
        isError = true,
        isEnabled = true,
        supportingText = "Please enter valid email",
        onEmailChange = { }
    )
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return email.matches(emailRegex)
}
