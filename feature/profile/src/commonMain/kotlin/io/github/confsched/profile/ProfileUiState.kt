package io.github.confsched.profile

import androidx.compose.ui.graphics.ImageBitmap
import io.github.confsched.profile.components.ProfileCardTheme
import io.github.droidkaigi.confsched.model.profile.Profile

sealed interface ProfileUiState {
    data class Card(
        val profile: Profile,
        val profileImageBitmap: ImageBitmap,
        val qrImageBitmap: ImageBitmap,
    ) : ProfileUiState {
        val theme: ProfileCardTheme get() = ProfileCardTheme.fromThemeKey(profile.themeKey)
    }

    data class Edit(
        val baseProfile: Profile?,
    ) : ProfileUiState
}
