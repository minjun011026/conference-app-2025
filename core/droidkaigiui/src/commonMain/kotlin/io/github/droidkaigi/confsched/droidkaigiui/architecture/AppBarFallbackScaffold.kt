package io.github.droidkaigi.confsched.droidkaigiui.architecture

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedMediumTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class AppBarSize {
    Default,
    Medium,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
context(_: SoilFallbackContext)
fun AppBarFallbackScaffold(
    title: String,
    onBackClick: (() -> Unit)? = null,
    appBarSize: AppBarSize = AppBarSize.Default,
    appBarContainerColor: Color = MaterialTheme.colorScheme.surface,
    windowInsets: WindowInsets = WindowInsets.safeDrawingWithBottomNavBar,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            when (appBarSize) {
                AppBarSize.Default -> {
                    AnimatedTextTopAppBar(
                        title = title,
                        containerColor = appBarContainerColor,
                        onBackClick = onBackClick,
                    )
                }

                AppBarSize.Medium -> {
                    AnimatedMediumTopAppBar(
                        title = title,
                        containerColor = appBarContainerColor,
                        onBackClick = { onBackClick?.invoke() },
                    )
                }
            }
        },
        contentWindowInsets = windowInsets,
        content = content,
    )
}

@Preview
@Composable
private fun AppBarFallbackScaffoldPreview() {
    KaigiPreviewContainer {
        AppBarFallbackScaffold(
            title = "Title",
            onBackClick = {},
            appBarSize = AppBarSize.Default,
        ) { innerPadding ->
            // Content goes here
        }
    }
}

@Preview
@Composable
private fun AppBarFallbackScaffoldMediumPreview() {
    KaigiPreviewContainer {
        AppBarFallbackScaffold(
            title = "Title",
            onBackClick = {},
            appBarSize = AppBarSize.Medium,
        ) { innerPadding ->
            // Content goes here
        }
    }
}
