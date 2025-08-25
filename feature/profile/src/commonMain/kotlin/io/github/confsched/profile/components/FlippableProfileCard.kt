package io.github.confsched.profile.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import io.github.confsched.profile.ProfileUiState
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.model.profile.Profile
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val ChangeFlipCardDeltaThreshold = 20f

@Composable
fun FlippableProfileCard(
    uiState: ProfileUiState.Card,
    modifier: Modifier = Modifier,
) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
    )
    val isFront by remember { derivedStateOf { rotation < 90f } }

    Card(
        modifier = modifier
            .clickable {
                isFlipped = !isFlipped
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    if (isFlipped && delta > ChangeFlipCardDeltaThreshold) {
                        isFlipped = false
                    }
                    if (!isFlipped && delta < -ChangeFlipCardDeltaThreshold) {
                        isFlipped = true
                    }
                },
            )
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
    ) {
        if (isFront) {
            ProfileCardFront(
                theme = uiState.profile.theme,
                profileImageBitmap = uiState.profileImageBitmap,
                nickName = uiState.profile.nickName,
                occupation = uiState.profile.occupation,
            )
        } else {
            ProfileCardBack(
                theme = uiState.profile.theme,
                qrImageBitmap = uiState.qrImageBitmap,
                modifier = Modifier.graphicsLayer {
                    rotationY = 180f
                },
            )
        }
    }
}

@Preview
@Composable
private fun FlippableProfileCardPreview() {
    KaigiPreviewContainer {
        FlippableProfileCard(
            uiState = ProfileUiState.Card(
                profile = CardPreviewResources.dummyProfile,
            ),
        )
    }
}
