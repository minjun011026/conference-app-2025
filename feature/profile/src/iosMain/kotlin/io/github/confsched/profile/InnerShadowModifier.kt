package io.github.confsched.profile

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

actual fun Modifier.innerShadow(
    color: Color,
    shape: Shape,
    blur: Dp,
    offsetY: Dp,
    offsetX: Dp,
    spread: Dp
) : Modifier = this
