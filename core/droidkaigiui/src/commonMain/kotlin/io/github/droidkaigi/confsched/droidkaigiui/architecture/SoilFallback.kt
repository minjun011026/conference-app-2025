@file:Suppress("UNUSED")

package io.github.droidkaigi.confsched.droidkaigiui.architecture

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar

sealed interface SoilFallback {
    val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit
    val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit
}

object SoilFallbackDefaults {
    @Composable
    fun appBar(
        title: String,
        onBackClick: (() -> Unit)? = null,
        appBarSize: AppBarSize = AppBarSize.Default,
        appBarContainerColor: Color = MaterialTheme.colorScheme.surface,
        // Allowing WindowInsets to be overridden to prevent layout jump/glitches
        // when navigating between screens with and without a bottom navigation bar.
        windowInsets: WindowInsets = WindowInsets.safeDrawingWithBottomNavBar,
        contentBackground: (@Composable (innerPadding: PaddingValues) -> Unit)? = null,
    ): SoilFallback = AppBar(
        title = title,
        onBackClick = onBackClick,
        size = appBarSize,
        containerColor = appBarContainerColor,
        windowInsets = windowInsets,
        contentBackground = contentBackground,
    )

    fun default(): SoilFallback = Default

    fun custom(
        suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit,
        errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit,
    ): SoilFallback = Custom(
        suspenseFallback = suspenseFallback,
        errorFallback = errorFallback,
    )
}

private object Default : SoilFallback {
    override val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit
        get() = { DefaultSuspenseFallbackContent() }
    override val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit
        get() = { DefaultErrorFallbackContent() }
}

private class AppBar(
    val title: String,
    val onBackClick: (() -> Unit)?,
    val size: AppBarSize,
    val containerColor: Color,
    val windowInsets: WindowInsets,
    val contentBackground: (@Composable (innerPadding: PaddingValues) -> Unit)?,
) : SoilFallback {
    override val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit = {
        AppBarFallbackScaffold(
            title = title,
            onBackClick = onBackClick,
            appBarSize = size,
            appBarContainerColor = containerColor,
            windowInsets = windowInsets,
        ) { innerPadding ->
            contentBackground?.invoke(innerPadding)
            DefaultSuspenseFallbackContent(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }

    override val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit = {
        AppBarFallbackScaffold(
            title = title,
            onBackClick = onBackClick,
            appBarSize = size,
            appBarContainerColor = containerColor,
            windowInsets = windowInsets,
        ) { innerPadding ->
            contentBackground?.invoke(innerPadding)
            DefaultErrorFallbackContent(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

private class Custom(
    override val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit,
    override val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit,
) : SoilFallback
