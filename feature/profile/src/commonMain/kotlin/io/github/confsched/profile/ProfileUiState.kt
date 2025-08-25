package io.github.confsched.profile

import androidx.compose.ui.graphics.decodeToImageBitmap
import io.github.droidkaigi.confsched.model.profile.Profile

sealed interface ProfileUiState {
    val profile: Profile?

    data class Card(override val profile: Profile) : ProfileUiState {
        val qrImageBitmap = profile.qrCodeByteArray.decodeToImageBitmap()
        val profileImageBitmap = profile.imageByteArray.decodeToImageBitmap()
    }

    data class Edit(override val profile: Profile?) : ProfileUiState
}
