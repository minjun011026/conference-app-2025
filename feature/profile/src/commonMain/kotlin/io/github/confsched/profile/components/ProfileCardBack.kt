package io.github.confsched.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.card_back_background_frame_day
import io.github.droidkaigi.confsched.profile.card_back_background_frame_night
import io.github.droidkaigi.confsched.profile.card_back_foreground_frame_day
import io.github.droidkaigi.confsched.profile.card_back_foreground_frame_night
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileCardBack(
    theme: ProfileCardTheme,
    qrImageBitmap: ImageBitmap,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(300.dp)
            .height(380.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(theme.baseColor),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = if (theme.isDark) {
                painterResource(ProfileRes.drawable.card_back_background_frame_day)
            } else {
                painterResource(ProfileRes.drawable.card_back_background_frame_night)
            },
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
        )
        Image(
            bitmap = qrImageBitmap,
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )
        Image(
            painter = if (theme.isDark) {
                painterResource(ProfileRes.drawable.card_back_foreground_frame_day)
            } else {
                painterResource(ProfileRes.drawable.card_back_foreground_frame_night)
            },
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
        )
    }
}

@Preview
@Composable
private fun ProfileCardBackPreview() {
    val imageBitmap = ImageBitmap(
        width = 160,
        height = 160,
    ).apply {
        val canvas = Canvas(this)
        canvas.drawRect(
            rect = Rect(0f, 0f, 160f, 160f),
            paint = Paint().apply { color = Color.Red }
        )
    }

    ProfileCardBack(
        theme = ProfileCardTheme.DarkPill,
        qrImageBitmap = imageBitmap,
    )
}
