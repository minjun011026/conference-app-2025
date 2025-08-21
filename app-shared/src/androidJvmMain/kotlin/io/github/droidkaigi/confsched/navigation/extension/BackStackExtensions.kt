package io.github.droidkaigi.confsched.navigation.extension

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import io.github.droidkaigi.confsched.component.MainScreenTab
import io.github.droidkaigi.confsched.navkey.AboutNavKey
import io.github.droidkaigi.confsched.navkey.EventMapNavKey
import io.github.droidkaigi.confsched.navkey.FavoritesNavKey
import io.github.droidkaigi.confsched.navkey.ProfileNavKey
import io.github.droidkaigi.confsched.navkey.TimetableNavKey

/**
 * Returns the corresponding NavKey for this MainScreenTab
 */
val MainScreenTab.navKey: NavKey
    get() = when (this) {
        MainScreenTab.Timetable -> TimetableNavKey
        MainScreenTab.EventMap -> EventMapNavKey
        MainScreenTab.Favorite -> FavoritesNavKey
        MainScreenTab.About -> AboutNavKey
        MainScreenTab.Profile -> ProfileNavKey
    }

/**
 * All main tab navigation keys that should be preserved in the backstack
 */
val mainTabNavKeys = MainScreenTab.entries.map { it.navKey }.toSet()

/**
 * Safely removes the last item from the backstack while preserving root navigation keys.
 * Root keys include main tab navigation keys that should never be removed to prevent empty backstack.
 * Prevents "java.lang.IllegalArgumentException: NavDisplay backstack cannot be empty"
 * when rapidly tapping back buttons in navigation3.
 */
fun SnapshotStateList<NavKey>.safeRemoveLastPreservingRoot(): NavKey? {
    val isRootNavKey = lastOrNull() in mainTabNavKeys

    return if (size > 1 && !isRootNavKey) {
        removeLastOrNull()
    } else {
        null
    }
}
