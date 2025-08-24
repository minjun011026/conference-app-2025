package io.github.confsched.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.card_front_background_day
import io.github.droidkaigi.confsched.profile.card_front_background_night
import io.github.droidkaigi.confsched.profile.card_front_bottom_curve
import io.github.droidkaigi.confsched.profile.card_front_logo_frame_day
import io.github.droidkaigi.confsched.profile.card_front_logo_frame_night
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileCardFront(
    theme: ProfileCardTheme,
    nickName: String,
    occupation: String,
    profileImagePath: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(300.dp)
            .height(380.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(
                if (theme.isDark) {
                    ProfileRes.drawable.card_front_background_night
                } else {
                    ProfileRes.drawable.card_front_background_day
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )
        ProfileCardUser(
            isDarkTheme = theme.isDark,
            profileImageUrl = profileImagePath,
            userName = nickName,
            profileShape = theme.profileShape,
            occupation = occupation,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    translationY = 110.dp.toPx()
                }
        )
        Image(
            painter = painterResource(ProfileRes.drawable.card_front_bottom_curve),
            contentDescription = null,
            colorFilter = ColorFilter.tint(theme.baseColor),
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .graphicsLayer {
                    translationY = 36.18.dp.toPx()
                }
                .align(Alignment.BottomCenter)
        )
        Image(
            painter = if (theme.isDark) {
                painterResource(ProfileRes.drawable.card_front_logo_frame_day)
            } else {
                painterResource(ProfileRes.drawable.card_front_logo_frame_night)
            },
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
        )
    }
}

@Preview
@Composable
private fun ProfileCardFrontPreview() {
    KaigiPreviewContainer {
        ProfileCardFront(
            theme = ProfileCardTheme.DarkPill,
            profileImagePath = "",
            nickName = "DroidKaigi",
            occupation = "Software Engineer",
        )
    }
}
