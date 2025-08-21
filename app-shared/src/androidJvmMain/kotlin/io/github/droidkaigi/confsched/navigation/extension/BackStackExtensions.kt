package io.github.droidkaigi.confsched.navigation.extension

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

/**
 * Safely removes the last item from the backstack only if it matches the expected type.
 * Prevents "java.lang.IllegalArgumentException: NavDisplay backstack cannot be empty"
 * when rapidly tapping back buttons in navigation3.
 */

inline fun <reified T : NavKey> SnapshotStateList<NavKey>.safeRemoveLastIfType(): NavKey? {
    return if (isNotEmpty() && lastOrNull() is T) {
        removeLastOrNull()
    } else {
        null
    }
}
