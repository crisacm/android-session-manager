@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.crisacm.sessionmanager.ui.feature.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme

@Composable
fun AppBar(title: String, actions: (@Composable RowScope.() -> Unit)? = null) {
    TopAppBar(
        title = { Text(text = title) },
        actions = actions ?: {}
    )
}

@Composable
fun AppBarAction(
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun AppBarPreview() {
    SessionManagerTheme {
        AppBar(
            "Preview",
            actions = {
                AppBarAction(
                    imageVector = Icons.Default.Search,
                    onClick = {}
                )
            }
        )
    }
}
