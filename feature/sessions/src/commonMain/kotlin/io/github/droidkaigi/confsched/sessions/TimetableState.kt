package io.github.droidkaigi.confsched.sessions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberTimetableState(
    timetableScrollState: TimetableScrollState = rememberTimetableScrollState(),
    timetableScaleState: TimetableScaleState = rememberScreenScaleState(),
): TimetableState = remember {
    TimetableState(timetableScrollState, timetableScaleState)
}

data class TimetableState(
    val scrollState: TimetableScrollState,
    val scaleState: TimetableScaleState,
)
