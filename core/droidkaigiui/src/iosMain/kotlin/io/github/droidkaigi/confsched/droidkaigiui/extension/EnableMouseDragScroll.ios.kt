package io.github.droidkaigi.confsched.droidkaigiui.extension

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun Modifier.enableMouseDragScroll(lazyListState: LazyListState): Modifier = this // NOOP
