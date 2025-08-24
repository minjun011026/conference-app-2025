package io.github.confsched.profile.components

import androidx.compose.ui.graphics.ImageBitmap

data class ProfileCardUiState(
    val nickName: String,
    val occupation: String,
    val profileImageBitmap: ImageBitmap,
    val qrImageBitmap: ImageBitmap,
    val theme: ProfileCardTheme,
)
