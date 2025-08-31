import Component
import Model
import SwiftUI
import Theme

public struct SearchScreen: View {
    @State private var presenter = SearchPresenter()
    @FocusState private var isSearchFieldFocused: Bool
    let onNavigate: (SearchNavigationDestination) -> Void

    public init(onNavigate: @escaping (SearchNavigationDestination) -> Void = { _ in }) {
        self.onNavigate = onNavigate
    }

    public var body: some View {
        VStack(spacing: 0) {
            searchBar
            filterAndResultsScrollView
        }
        .background(AssetColors.surface.swiftUIColor)
        .navigationTitle(String(localized: "Search", bundle: .module))
        #if os(iOS)
            .navigationBarTitleDisplayMode(.large)
        #endif
        .onAppear {
            presenter.loadInitial()
            isSearchFieldFocused = true
        }
    }

    private var searchBar: some View {
        HStack(spacing: 12) {
            Image(systemName: "magnifyingglass")
                .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
                .accessibilityLabel(String(localized: "Search icon", bundle: .module))

            TextField(String(localized: "Search sessions", bundle: .module), text: $presenter.searchWord)
                .font(.body)
                .focused($isSearchFieldFocused)
                .submitLabel(.search)
                .onSubmit {
                    // Search is automatic with binding
                }

            if !presenter.searchWord.isEmpty {
                clearButton
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(AssetColors.surfaceVariant.swiftUIColor)
                .stroke(AssetColors.outline.swiftUIColor.opacity(0.2), lineWidth: 1)
        )
        .padding(.horizontal, 16)
        .padding(.vertical, 8)
    }

    private var clearButton: some View {
        Button(
            action: {
                presenter.searchWord = ""
            },
            label: {
                Image(systemName: "xmark.circle.fill")
                    .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
                    .accessibilityLabel(String(localized: "Clear search", bundle: .module))
            })
    }

    private var filterAndResultsScrollView: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                categorySection
                languageSection
                daySection
                roomSection
                searchResultsSection
            }
            .padding(.top, 8)
        }
    }

    @ViewBuilder
    private var searchResultsSection: some View {
        if shouldShowResults {
            VStack(alignment: .leading, spacing: 16) {
                Divider()
                    .padding(.vertical, 4)

                HStack {
                    Text("Results")
                        .font(.headline)
                        .fontWeight(.semibold)
                        .foregroundColor(AssetColors.onSurface.swiftUIColor)
                    
                    Spacer()
                    
                    Text("\(presenter.filteredTimetableItems.count)")
                        .font(.subheadline)
                        .foregroundColor(AssetColors.onSurfaceVariant.swiftUIColor)
                }
                .padding(.horizontal, 16)

                searchResultsList
            }
        }
    }

    private var shouldShowResults: Bool {
        !presenter.searchWord.isEmpty || presenter.selectedDay != nil
            || presenter.selectedCategory != nil || presenter.selectedLanguage != nil
    }

    private var searchResultsList: some View {
        LazyVStack(spacing: 16) {
            ForEach(presenter.filteredTimetableItems) { item in
                TimetableCard(
                    timetableItem: item.timetableItem,
                    isFavorite: item.isFavorited,
                    onTap: { _ in
                        onNavigate(.timetableDetail(item))
                    },
                    onTapFavorite: { _, _ in
                        presenter.toggleFavorite(item.timetableItem.id)
                    }
                )
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .fill(AssetColors.surface.swiftUIColor)
                        .shadow(color: AssetColors.onSurface.swiftUIColor.opacity(0.05), radius: 2, x: 0, y: 1)
                )
            }
        }
        .padding(.horizontal, 16)
        .padding(.bottom, 80)  // Tab bar padding
    }

    @ViewBuilder
    private var categorySection: some View {
        if let timetable = presenter.timetable.timetable {
            let uniqueCategories = Set(timetable.timetableItems.map { $0.category })
            let categories = Array(uniqueCategories).sorted { $0.id < $1.id }

            VStack(alignment: .leading, spacing: 12) {
                Text("Category")
                    .font(.subheadline)
                    .fontWeight(.medium)
                    .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                    .padding(.horizontal, 16)

                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 12) {
                        SearchFilterChip<TimetableCategory>(
                            title: "All",
                            isSelected: presenter.selectedCategory == nil,
                            onTap: {
                                presenter.selectedCategory = nil
                            }
                        )

                        ForEach(categories) { category in
                            SearchFilterChip<TimetableCategory>(
                                title: category.title.jaTitle,
                                isSelected: presenter.selectedCategory == category,
                                onTap: {
                                    presenter.selectedCategory = presenter.selectedCategory == category ? nil : category
                                }
                            )
                        }
                    }
                    .padding(.horizontal, 16)
                }
            }
        }
    }

    @ViewBuilder
    private var languageSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Language")
                .font(.subheadline)
                .fontWeight(.medium)
                .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                .padding(.horizontal, 16)

            languageFilterChips
        }
    }

    private var languageFilterChips: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 12) {
                allLanguagesChip
                japaneseChip
                englishChip
                mixedChip
            }
            .padding(.horizontal, 16)
        }
    }

    private var allLanguagesChip: some View {
        SearchFilterChip<TimetableLanguage>(
            title: "All",
            isSelected: presenter.selectedLanguage == nil,
            onTap: {
                presenter.selectedLanguage = nil
            }
        )
    }

    private var japaneseChip: some View {
        SearchFilterChip<TimetableLanguage>(
            title: "Japanese",
            isSelected: presenter.selectedLanguage?.langOfSpeaker == "JA",
            onTap: {
                toggleLanguage("JA")
            }
        )
    }

    private var englishChip: some View {
        SearchFilterChip<TimetableLanguage>(
            title: "English",
            isSelected: presenter.selectedLanguage?.langOfSpeaker == "EN",
            onTap: {
                toggleLanguage("EN")
            }
        )
    }

    private var mixedChip: some View {
        SearchFilterChip<TimetableLanguage>(
            title: "Mixed",
            isSelected: presenter.selectedLanguage?.langOfSpeaker == "MIXED",
            onTap: {
                toggleLanguage("MIXED")
            }
        )
    }

    private func toggleLanguage(_ lang: String) {
        if presenter.selectedLanguage?.langOfSpeaker == lang {
            presenter.selectedLanguage = nil
        } else {
            presenter.selectedLanguage = TimetableLanguage(
                langOfSpeaker: lang, isInterpretationTarget: false)
        }
    }

    @ViewBuilder
    private var daySection: some View {
        if presenter.timetable.timetable != nil {
            VStack(alignment: .leading, spacing: 12) {
                Text("Day")
                    .font(.subheadline)
                    .fontWeight(.medium)
                    .foregroundStyle(AssetColors.onSurface.swiftUIColor)
                    .padding(.horizontal, 16)

                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 12) {
                        SearchFilterChip<DroidKaigi2025Day>(
                            title: "All",
                            isSelected: presenter.selectedDay == nil,
                            onTap: {
                                presenter.selectedDay = nil
                            }
                        )

                        ForEach([DroidKaigi2025Day.conferenceDay1, .conferenceDay2], id: \.self) { day in
                            SearchFilterChip<DroidKaigi2025Day>(
                                title: day == .conferenceDay1 ? "Day 1" : "Day 2",
                                isSelected: presenter.selectedDay == day,
                                onTap: {
                                    presenter.selectedDay = presenter.selectedDay == day ? nil : day
                                }
                            )
                        }
                    }
                    .padding(.horizontal, 16)
                }
            }
        }
    }

    @ViewBuilder
    private var roomSection: some View {
        EmptyView()  // TODO: Implement room filter when room data is available
    }
}

#Preview {
    SearchScreen()
}
