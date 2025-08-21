package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import coil3.compose.rememberAsyncImagePainter as coilRememberAsyncImagePainter

@Composable
fun rememberAsyncImagePainter(
    model: Any?,
    placeholder: Painter = painterResource(DroidkaigiuiRes.drawable.error_mascot),
    error: Painter = painterResource(DroidkaigiuiRes.drawable.error_mascot),
): Painter {
    return coilRememberAsyncImagePainter(
        model = model,
        placeholder = placeholder,
        error = error,
    )
}
