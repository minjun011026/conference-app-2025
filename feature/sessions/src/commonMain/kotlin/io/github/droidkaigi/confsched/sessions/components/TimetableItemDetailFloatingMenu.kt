package io.github.droidkaigi.confsched.sessions.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.extension.roomTheme
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.add_to_bookmark
import io.github.droidkaigi.confsched.sessions.add_to_calendar
import io.github.droidkaigi.confsched.sessions.remove_from_bookmark
import io.github.droidkaigi.confsched.sessions.share_link
import io.github.droidkaigi.confsched.sessions.slide
import io.github.droidkaigi.confsched.sessions.video
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimetableItemDetailFloatingActionButtonMenu(
    isBookmarked: Boolean,
    slideUrl: String?,
    videoUrl: String?,
    onBookmarkClick: (isBookmarked: Boolean) -> Unit,
    onAddCalendarClick: () -> Unit,
    onShareClick: () -> Unit,
    onViewSlideClick: (url: String) -> Unit,
    onWatchVideoClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    TimetableItemDetailFloatingActionButtonMenu(
        expanded = expanded,
        isBookmarked = isBookmarked,
        slideUrl = slideUrl,
        videoUrl = videoUrl,
        onExpandedChange = { expanded = it },
        onBookmarkClick = onBookmarkClick,
        onAddCalendarClick = onAddCalendarClick,
        onShareClick = onShareClick,
        onViewSlideClick = onViewSlideClick,
        onWatchVideoClick = onWatchVideoClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TimetableItemDetailFloatingActionButtonMenu(
    expanded: Boolean,
    isBookmarked: Boolean,
    slideUrl: String?,
    videoUrl: String?,
    onExpandedChange: (Boolean) -> Unit,
    onBookmarkClick: (isBookmarked: Boolean) -> Unit,
    onAddCalendarClick: () -> Unit,
    onShareClick: () -> Unit,
    onViewSlideClick: (url: String) -> Unit,
    onWatchVideoClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var height by remember { mutableIntStateOf(0) }
    var childMenuIsBookmarked by remember { mutableStateOf(isBookmarked) } // local copy to update after transition
    val latestIsBookmarked by rememberUpdatedState(isBookmarked) // to ensure the latest value is used in recomposition

    // Recompose child menu items only after the view size has settled.
    // Recomposing them during the transition may cause a brief flicker that makes the menu look flaky.
    LaunchedEffect(height) {
        delay(100) // small debounce delay to wait until transition stabilizes
        childMenuIsBookmarked = latestIsBookmarked
    }
    val haptic = LocalHapticFeedback.current

    val roomTheme = LocalRoomTheme.current
    // TODO: This color is temporary. We should define a proper color once the official Figma definitions are available.
    val menuItemContainerColor = roomTheme.primaryColor.copy(alpha = 0.5f).compositeOver(Color.Black)
    FloatingActionButtonMenu(
        expanded = expanded,
        button = {
            ToggleFloatingActionButton(
                checked = expanded,
                onCheckedChange = onExpandedChange,
                containerColor = { _ -> menuItemContainerColor },
            ) {
                if (expanded) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                    )
                } else {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                    )
                }
            }
        },
        modifier = modifier.onSizeChanged { size ->
            height = size.height
        },
        horizontalAlignment = Alignment.End,
    ) {
        FloatingActionButtonMenuItem(
            onClick = {
                if (!isBookmarked) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                onBookmarkClick(!isBookmarked)
                onExpandedChange(false)
            },
            text = {
                Text(
                    if (childMenuIsBookmarked) {
                        stringResource(SessionsRes.string.remove_from_bookmark)
                    } else {
                        stringResource(SessionsRes.string.add_to_bookmark)
                    },
                )
            },
            icon = {
                Icon(
                    if (childMenuIsBookmarked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                )
            },
            containerColor = menuItemContainerColor,
        )
        FloatingActionButtonMenuItem(
            onClick = {
                onAddCalendarClick()
                onExpandedChange(false)
            },
            text = { Text(stringResource(SessionsRes.string.add_to_calendar)) },
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
            containerColor = menuItemContainerColor,
        )
        FloatingActionButtonMenuItem(
            onClick = {
                onShareClick()
                onExpandedChange(false)
            },
            text = { Text(stringResource(SessionsRes.string.share_link)) },
            icon = { Icon(Icons.Default.Share, contentDescription = null) },
            containerColor = menuItemContainerColor,
        )
        slideUrl?.let { url ->
            FloatingActionButtonMenuItem(
                onClick = {
                    onViewSlideClick(url)
                    onExpandedChange(false)
                },
                text = { Text(stringResource(SessionsRes.string.slide)) },
                icon = { Icon(Icons.Outlined.Description, contentDescription = null) },
                containerColor = menuItemContainerColor,
            )
        }
        videoUrl?.let { url ->
            FloatingActionButtonMenuItem(
                onClick = {
                    onWatchVideoClick(url)
                    onExpandedChange(false)
                },
                text = { Text(stringResource(SessionsRes.string.video)) },
                icon = { Icon(Icons.Outlined.PlayCircle, contentDescription = null) },
                containerColor = menuItemContainerColor,
            )
        }
    }
}

@Preview
@Composable
private fun TimetableItemDetailFloatingMenuPreview() {
    val session = TimetableItem.Session.fake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailFloatingActionButtonMenu(
                expanded = false,
                isBookmarked = false,
                onExpandedChange = {},
                onBookmarkClick = {},
                onAddCalendarClick = {},
                onShareClick = {},
                slideUrl = session.asset.slideUrl,
                videoUrl = session.asset.videoUrl,
                onViewSlideClick = {},
                onWatchVideoClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun TimetableItemDetailFloatingMenuExpandedPreview() {
    val session = TimetableItem.Session.fake()
    KaigiPreviewContainer {
        ProvideRoomTheme(session.room.roomTheme) {
            TimetableItemDetailFloatingActionButtonMenu(
                expanded = true,
                isBookmarked = false,
                onExpandedChange = {},
                onBookmarkClick = {},
                onAddCalendarClick = {},
                onShareClick = {},
                slideUrl = session.asset.slideUrl,
                videoUrl = session.asset.videoUrl,
                onViewSlideClick = {},
                onWatchVideoClick = {},
            )
        }
    }
}
