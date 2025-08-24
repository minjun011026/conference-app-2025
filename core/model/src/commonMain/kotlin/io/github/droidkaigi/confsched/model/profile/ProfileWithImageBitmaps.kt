package io.github.droidkaigi.confsched.model.profile

import androidx.compose.ui.graphics.ImageBitmap

data class ProfileWithImageBitmaps(
    val profile: Profile? = null,
    val profileImageBitmap: ImageBitmap? = null,
    val qrImageBitmap: ImageBitmap? = null,
)
