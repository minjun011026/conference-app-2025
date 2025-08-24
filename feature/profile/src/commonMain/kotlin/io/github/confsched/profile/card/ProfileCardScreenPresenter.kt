package io.github.confsched.profile.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.decodeToImageBitmap
import io.github.confsched.profile.ProfileScreenContext
import io.github.confsched.profile.components.ProfileCardTheme
import io.github.confsched.profile.components.ProfileCardUiState
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.profile.Profile
import qrcode.QRCode

@Composable
context(screenContext: ProfileScreenContext)
fun profileCardScreenPresenter(
    eventFlow: EventFlow<ProfileCardScreenEvent>,
    profile: Profile,
): ProfileCardUiState = providePresenterDefaults {
    // maybe we should generate QR code in data module, but for now, let's keep it here.
    val qrImageBitmap = remember(profile.link) {
        QRCode.ofSquares()
            .build(profile.link)
            .renderToBytes()
            .decodeToImageBitmap()
    }

    EventEffect(eventFlow) { event ->
    }

    ProfileCardUiState(
        nickName = profile.name,
        profileImagePath = profile.imagePath,
        theme = ProfileCardTheme.fromThemeKey(profile.themeKey),
        occupation = profile.occupation,
        qrImageBitmap = qrImageBitmap,
    )
}
