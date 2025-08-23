package io.github.droidkaigi.confsched.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.rememberHazeState
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalBottomNavigationBarsPadding
import io.github.droidkaigi.confsched.droidkaigiui.extension.Empty
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun KaigiNavigationScaffold(
    currentTab: MainScreenTab?,
    hazeState: HazeState,
    onTabSelected: (MainScreenTab) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val animatedSelectedTabIndex by animateFloatAsState(
        targetValue = currentTab?.ordinal?.toFloat() ?: 0f,
        label = "animatedSelectedTabIndex",
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy,
        ),
    )
    val animatedColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.primaryFixed,
        label = "animatedColor",
        animationSpec = spring(stiffness = Spring.StiffnessLow),
    )

    KaigiNavigationScaffold(
        currentTab = currentTab,
        hazeState = hazeState,
        onTabSelected = onTabSelected,
        animatedSelectedTabIndex = animatedSelectedTabIndex,
        animatedColor = animatedColor,
        modifier = modifier,
        content = content,
    )
}

@Composable
private fun KaigiNavigationScaffold(
    currentTab: MainScreenTab?,
    hazeState: HazeState,
    onTabSelected: (MainScreenTab) -> Unit,
    animatedSelectedTabIndex: Float,
    animatedColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        bottomBar = {
            AnimatedVisibility(currentTab != null) {
                GlassLikeBottomNavigationBar(
                    currentTab = currentTab ?: MainScreenTab.Timetable,
                    hazeState = hazeState,
                    onTabSelected = onTabSelected,
                    animatedSelectedTabIndex = animatedSelectedTabIndex,
                    animatedColor = animatedColor,
                    modifier = Modifier
                        .padding(
                            start = 55.dp,
                            end = 55.dp,
                            top = 16.dp,
                            bottom = 32.dp,
                        )
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
                            ),
                        ),
                )
            }
        },
        contentWindowInsets = WindowInsets.Empty,
        modifier = modifier,
    ) { bottomNavigationBarsPadding ->
        CompositionLocalProvider(LocalBottomNavigationBarsPadding provides bottomNavigationBarsPadding) {
            content()
        }
    }
}

@Preview
@Composable
private fun KaigiNavigationScaffoldPreview() {
    KaigiPreviewContainer {
        KaigiNavigationScaffold(
            currentTab = MainScreenTab.Timetable,
            onTabSelected = {},
            hazeState = rememberHazeState(),
        ) {
            Text("Content goes here")
        }
    }
}
