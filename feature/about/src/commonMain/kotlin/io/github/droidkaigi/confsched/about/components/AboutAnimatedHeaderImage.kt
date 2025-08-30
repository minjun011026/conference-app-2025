package io.github.droidkaigi.confsched.about.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.about_header_bg_only
import io.github.droidkaigi.confsched.about.about_header_mascot1
import io.github.droidkaigi.confsched.about.about_header_mascot2
import io.github.droidkaigi.confsched.about.about_header_mascot3
import io.github.droidkaigi.confsched.about.about_header_title_bg_title
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

@Composable
fun AboutAnimatedHeaderImage(
    modifier: Modifier = Modifier,
) {
    val titleAlpha = remember { Animatable(0f) }
    val mascot1Alpha = remember { Animatable(0f) }
    val mascot2Alpha = remember { Animatable(0f) }

    val density = LocalDensity.current
    val backgroundPainter = painterResource(AboutRes.drawable.about_header_bg_only)

    val dynamicAspect = remember(backgroundPainter) {
        val s = backgroundPainter.intrinsicSize
        val hasSize = s != Size.Unspecified && s.width > 0f && s.height > 0f
        if (hasSize) s.width / s.height else 1648f / 550f
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(dynamicAspect)
            .clipToBounds(),
    ) {
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val maxHeightPx = with(density) { maxHeight.toPx() }

        val mascot3OffsetY = remember(maxHeightPx) { Animatable(maxHeightPx) }
        val mascot3OffsetX = remember { Animatable(0f) }

        LaunchedEffect(maxWidth, maxHeight) {
            playHeaderIntro(
                titleAlpha = titleAlpha,
                mascot1Alpha = mascot1Alpha,
                mascot2Alpha = mascot2Alpha,
                mascot3OffsetY = mascot3OffsetY,
            )
            loopMascot3Peek(
                offsetY = mascot3OffsetY,
                offsetX = mascot3OffsetX,
                containerWidth = maxWidthPx,
                containerHeight = maxHeightPx,
            )
        }

        Image(
            painter = backgroundPainter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize().zIndex(0f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_mascot3),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    translationX = mascot3OffsetX.value
                    translationY = mascot3OffsetY.value
                }
                .zIndex(1.5f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_title_bg_title),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = titleAlpha.value }
                .zIndex(1f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_mascot1),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = mascot1Alpha.value }
                .zIndex(2.0f),
        )
        Image(
            painter = painterResource(AboutRes.drawable.about_header_mascot2),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = mascot2Alpha.value }
                .zIndex(2.1f),
        )
    }
}

private suspend fun playHeaderIntro(
    titleAlpha: Animatable<Float, *>,
    mascot1Alpha: Animatable<Float, *>,
    mascot2Alpha: Animatable<Float, *>,
    mascot3OffsetY: Animatable<Float, *>,
) {
    delay(180)

    titleAlpha.animateTo(1f, tween(durationMillis = 800))
    delay(80)

    coroutineScope {
        val jobMascot1 = launch {
            mascot1Alpha.animateTo(1f, tween(durationMillis = 500))
        }
        val jobMascot2 = launch {
            while (mascot1Alpha.value < 0.5f) {
                yield()
                delay(16)
            }
            mascot2Alpha.animateTo(1f, tween(durationMillis = 500))
        }
        jobMascot1.join()
        jobMascot2.join()
    }

    delay(40)
    mascot3OffsetY.animateTo(
        targetValue = 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
    )
    delay(1500)
}

private suspend fun loopMascot3Peek(
    offsetY: Animatable<Float, *>,
    offsetX: Animatable<Float, *>,
    containerWidth: Float,
    containerHeight: Float,
) {
    val maxRightShift = containerWidth * 0.20f

    while (true) {
        offsetY.animateTo(
            targetValue = containerHeight,
            animationSpec = tween(
                durationMillis = Random.nextInt(700, 1100),
                easing = FastOutSlowInEasing,
            ),
        )

        val nextX = Random.nextFloat() * maxRightShift
        offsetX.snapTo(nextX)

        delay(Random.nextLong(250, 600))

        val revealRatio = Random.nextFloat() * 0.1f + 0.9f
        val targetYForReveal = (1f - revealRatio) * containerHeight

        offsetY.animateTo(
            targetValue = targetYForReveal,
            animationSpec = tween(
                durationMillis = Random.nextInt(900, 1400),
                easing = FastOutSlowInEasing,
            ),
        )

        delay(Random.nextLong(2000, 3000))
    }
}
