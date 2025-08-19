package io.github.droidkaigi.confsched.sessions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun rememberScreenScaleState(): TimetableScaleState = rememberSaveable(
    saver = TimetableScaleState.Saver,
) {
    TimetableScaleState()
}

@Stable
class TimetableScaleState(
    initialVerticalScale: Float = 1f,
    initialVerticalScaleLowerBound: Float = 1f,
) {
    private var verticalScaleLowerBound = initialVerticalScaleLowerBound
    private val verticalScaleUpperBound = 1f
    private val verticalScaleState = mutableFloatStateOf(
        initialVerticalScale.coerceIn(verticalScaleLowerBound, verticalScaleUpperBound),
    )

    val verticalScale: Float
        get() = verticalScaleState.value

    fun updateVerticalScale(newScale: Float) {
        verticalScaleState.value =
            newScale.coerceIn(verticalScaleLowerBound, verticalScaleUpperBound)
    }

    fun updateVerticalScaleLowerBound(newLowerBound: Float) {
        if (newLowerBound < verticalScaleLowerBound) {
            verticalScaleLowerBound = newLowerBound
            updateVerticalScale(verticalScale)
        }
    }

    companion object {
        val Saver: Saver<TimetableScaleState, *> = listSaver(
            save = {
                listOf(
                    it.verticalScale,
                    it.verticalScaleLowerBound,
                )
            },
            restore = {
                TimetableScaleState(
                    initialVerticalScale = it[0],
                    initialVerticalScaleLowerBound = it[1],
                )
            },
        )
    }
}
