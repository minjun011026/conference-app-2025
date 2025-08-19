package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * Why this exists:
 * - `rememberSaveable<T>` relies on a reified type parameter (T) and must be inlined.
 * - In some call paths (e.g., context-receiver extension composables, function references,
 *   mixed AndroidX/JB Compose toolchains, or preview classpaths), the call may not be inlined,
 *   triggering `UnsupportedOperationException: This function has a reified type parameter...`.
 * - Providing an explicit `Saver<MutableState<Boolean>, Boolean>` avoids the reified `autoSaver<T>()`
 *   path entirely, while preserving process-death persistence semantics.
 * - Prefer this helper over `rememberSaveable { mutableStateOf(false) }` when used inside extension
 *   composables or if you hit the above crash. If persistence is not required, use `remember { ... }`.
 * Implementation notes:
 * - Ensure AndroidX imports (`androidx.compose.runtime.saveable.*`).
 */
@Composable
fun rememberBooleanSaveable(initial: Boolean = false): MutableState<Boolean> {
    val saver: Saver<MutableState<Boolean>, Boolean> = Saver(
        save = { it.value },
        restore = { mutableStateOf(it) },
    )
    return rememberSaveable(saver = saver) { mutableStateOf(initial) }
}

@Composable
fun rememberIntSaveable(initial: Int = 0): MutableState<Int> {
    val saver: Saver<MutableState<Int>, Int> = Saver(
        save = { it.value },
        restore = { mutableStateOf(it) },
    )
    return rememberSaveable(saver = saver) { mutableStateOf(initial) }
}
