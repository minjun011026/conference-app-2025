package io.github.confsched.profile

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import org.jetbrains.skia.BlendMode

actual fun Modifier.innerShadow(
    color: Color,
    shape: Shape,
    blur: Dp,
    offsetY: Dp,
    offsetX: Dp,
    spread: Dp
) = drawWithContent {
    drawContent()

    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            this.color = color
            this.asFrameworkPaint().also {
                it.isAntiAlias = true
                if (blur.toPx() > 0) {
                    // Skia의 MaskFilter
                    it.maskFilter = org.jetbrains.skia.MaskFilter.makeBlur(
                        org.jetbrains.skia.FilterBlurMode.NORMAL,
                        blur.toPx()
                    )
                }
            }
        }

        val outline = shape.createOutline(size, layoutDirection, this)

        paint.asFrameworkPaint().blendMode = BlendMode.DST_OUT

        canvas.save()
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(outline, paint)
        canvas.restore()
    }
}
