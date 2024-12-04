package com.github.crisacm.sessionmanager.ui.feature.home.composables

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.crisacm.sessionmanager.R
import com.github.crisacm.sessionmanager.ui.theme.SessionManagerTheme

@Suppress("FunctionNaming")
@Composable
fun SectionUserInfo(
  photoUrl: Uri?,
  name: String,
  email: String,
  phone: String,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(24.dp)
  ) {
    Card(
      modifier = Modifier.size(84.dp),
      shape = CircleShape
    ) {
      if (photoUrl == null) {
        Image(
          painter = painterResource(R.drawable.github),
          contentDescription = null,
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
        )
      } else {
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(photoUrl)
            .crossfade(true)
            .build(),
          placeholder = painterResource(R.drawable.github),
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
        )
      }
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 24.dp)
        .align(Alignment.CenterVertically)
    ) {
      Row(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Name:",
          fontWeight = FontWeight.Bold
        )
        Text(
          text = name.ifEmpty { "<Nothing>" },
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
          modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 0.dp)
        )
      }
      Row {
        Text(
          text = "Email:",
          fontWeight = FontWeight.Bold,
        )
        Text(
          text = email.ifEmpty { "<Nothing>" },
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
          modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 0.dp)
        )
      }
      Row {
        Text(
          text = "Phone:",
          fontWeight = FontWeight.Bold
        )
        Text(
          text = phone.ifEmpty { "<Nothing>" },
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
          modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 0.dp)
        )
      }
    }
  }
}

@Suppress("FunctionNaming")
@Preview
@Composable
fun SectionUserInfoPreview() {
  SessionManagerTheme {
    SectionUserInfo(
      photoUrl = null,
      name = "Cristian Acosta",
      email = "mail@gmail.com",
      phone = "+57 3205719967"
    )
  }
}
