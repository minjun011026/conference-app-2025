package io.github.confsched.profile.card

import androidx.compose.ui.graphics.ImageBitmap

sealed interface ProfileCardScreenEvent {
    data class ReadyToShare(val imageBitmap: ImageBitmap) : ProfileCardScreenEvent
}
