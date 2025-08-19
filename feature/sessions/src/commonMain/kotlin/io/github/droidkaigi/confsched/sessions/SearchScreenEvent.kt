package io.github.droidkaigi.confsched.sessions

import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.core.Lang
import io.github.droidkaigi.confsched.model.sessions.TimetableCategory
import io.github.droidkaigi.confsched.model.sessions.TimetableSessionType

sealed interface SearchScreenEvent {
    data class Search(val query: String) : SearchScreenEvent
    data class ToggleFilter(val filter: Filter) : SearchScreenEvent
    data class Bookmark(val sessionId: String) : SearchScreenEvent
    data object ClearFilters : SearchScreenEvent

    sealed interface Filter {
        data class Day(val day: DroidKaigi2025Day) : Filter
        data class Category(val category: TimetableCategory) : Filter
        data class SessionType(val sessionType: TimetableSessionType) : Filter
        data class Language(val language: Lang) : Filter
    }
}
