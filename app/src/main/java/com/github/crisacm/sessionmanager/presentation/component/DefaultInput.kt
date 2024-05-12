package com.github.crisacm.sessionmanager.presentation.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DefaultInput(
    text: String,
    label: String,
    placeHolder: String,
    isRequired: Boolean = false,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportedErrorText: String = "",
    isEnable: Boolean = true,
    onTextInput: (String) -> Unit
) {
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
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { onTextInput(it) },
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
            trailingIcon = {
                if (isError) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "error",
                        tint = Color.Red
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultInputPreview() {
    DefaultInput(text = "", placeHolder = "Email Address", label = "Username") {
        Log.i("Input", it)
    }
}
