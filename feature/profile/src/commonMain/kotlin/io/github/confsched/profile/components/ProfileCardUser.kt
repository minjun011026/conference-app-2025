package io.github.confsched.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileCardUser(
    isDarkTheme: Boolean,
    profileImageBitmap: ImageBitmap,
    userName: String,
    occupation: String,
    profileShape: ProfileShape,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            bitmap = profileImageBitmap,
            contentDescription = "User Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(profileShape.polygon.toShape()),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = occupation,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDarkTheme) Color.White else Color.Black,
        )
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall,
            color = if (isDarkTheme) Color.White else Color.Black,
        )
    }
}

@Preview
@Composable
private fun ProfileCardUser() {
    KaigiPreviewContainer {
        ProfileCardUser(
            isDarkTheme = true,
            profileImageBitmap = ImageBitmap(0, 0),
            profileShape = ProfileShape.Pill,
            userName = "name",
            occupation = "Software Engineer",
        )
    }
}
