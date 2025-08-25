package io.github.confsched.profile.components

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.droidkaigi.confsched.model.profile.ProfileCardTheme
import qrcode.QRCode

object CardPreviewResources {
    val profileImage = ImageBitmap(160, 160).apply {
        val canvas = Canvas(this)
        canvas.drawRect(
            left = 0f,
            top = 0f,
            right = width.toFloat(),
            bottom = height.toFloat(),
            paint = Paint().apply {
                color = Color.Red
            },
        )
    }

    val frontCardImage = ImageBitmap(300, 380).apply {
        val canvas = Canvas(this)
        canvas.drawRect(
            left = 0f,
            top = 0f,
            right = width.toFloat(),
            bottom = height.toFloat(),
            paint = Paint().apply {
                color = Color.Green
            },
        )
    }

    val backCardImage = ImageBitmap(300, 380).apply {
        val canvas = Canvas(this)
        canvas.drawRect(
            left = 0f,
            top = 0f,
            right = width.toFloat(),
            bottom = height.toFloat(),
            paint = Paint().apply {
                color = Color.Blue
            },
        )
    }

    val qrImageByteArray = QRCode.ofSquares()
        .build("https://2025.droidkaigi.jp/")
        .renderToBytes()

    val profileImageByteArray = ByteArray(0)

    val dummyProfile: Profile = Profile(
        nickName = "DroidKaigi",
        occupation = "Software Engineer",
        theme = ProfileCardTheme.DarkPill,
        link = "https://2025.droidkaigi.jp/",
        imagePath = "dummy_path",
        imageByteArray = profileImageByteArray,
        qrCodeByteArray = qrImageByteArray,
    )
}
