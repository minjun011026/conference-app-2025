package io.github.droidkaigi.confsched.sessions

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import soil.query.compose.rememberQuery
import soil.query.compose.rememberSubscription

@Composable
context(screenContext: SearchScreenContext)
fun SearchScreenRoot(
    onBackClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
) {
    SoilDataBoundary(
        state1 = rememberQuery(screenContext.timetableQueryKey),
        state2 = rememberSubscription(screenContext.favoriteTimetableIdsSubscriptionKey),
    ) { timetable, favoriteIds ->
        val eventFlow = rememberEventFlow<SearchScreenEvent>()

        val uiState = searchScreenPresenter(
            eventFlow = eventFlow,
            timetable = timetable.copy(bookmarks = favoriteIds),
        )

        SearchScreen(
            uiState = uiState,
            onBackClick = onBackClick,
            onTimetableItemClick = onTimetableItemClick,
            onEvent = { eventFlow.tryEmit(it) },
        )
    }
}
