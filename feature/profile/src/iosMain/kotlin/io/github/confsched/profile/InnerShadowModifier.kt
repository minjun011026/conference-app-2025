package io.github.confsched.profile

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import org.jetbrains.skia.BlendMode
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter

actual fun Modifier.innerShadow(
    color: Color,
    shape: Shape,
    blur: Dp,
    offsetY: Dp,
    offsetX: Dp,
    spread: Dp
): Modifier = this.drawWithContent {
    drawContent()

    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()

        val spreadPx = spread.toPx()
        val blurPx = blur.toPx()

        val shadowSize = Size(size.width + spreadPx, size.height + spreadPx)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        canvas.saveLayer(size.toRect(), paint)

        canvas.translate(-spreadPx, -spreadPx)
        paint.color = color
        canvas.drawOutline(shadowOutline, paint)

        frameworkPaint.blendMode = BlendMode.DST_OUT
        if (blurPx > 0) {
            frameworkPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, blurPx)
        }

        canvas.translate(spreadPx + offsetX.toPx(), spreadPx + offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)

        canvas.restore()
    }
}
