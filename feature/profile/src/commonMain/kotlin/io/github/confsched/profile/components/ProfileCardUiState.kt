package io.github.confsched.profile.components

import androidx.compose.ui.graphics.ImageBitmap

data class ProfileCardUiState(
    val profileImagePath: String,
    val nickName: String,
    val occupation: String,
    val qrImageBitmap: ImageBitmap,
    val theme: ProfileCardTheme,
)
