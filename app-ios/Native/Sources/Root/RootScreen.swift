import AboutFeature
import ContributorFeature
import Dependencies
import EventMapFeature
import FavoriteFeature
import HomeFeature
import Model
import ProfileCardFeature
import SearchFeature
import SettingsFeature
import SponsorFeature
import StaffFeature
import SwiftUI
import Theme
import TimetableDetailFeature
import UseCase

private enum TabType: CaseIterable, Hashable {
    case timetable
    case map
    case favorite
    case info
    case profileCard

    func tabImage(_ selectedTab: TabType) -> ImageAsset {
        switch self {
        case .timetable:
            return selectedTab == self ? AssetImages.icTimetableFill : AssetImages.icTimetable
        case .map:
            return selectedTab == self ? AssetImages.icMapFill : AssetImages.icMap
        case .favorite:
            return selectedTab == self ? AssetImages.icFavFill : AssetImages.icFav
        case .info:
            return selectedTab == self ? AssetImages.icInfoFill : AssetImages.icInfo
        case .profileCard:
            return selectedTab == self ? AssetImages.icProfileCardFill : AssetImages.icProfileCard
        }
    }
}

public struct RootScreen: View {
    @Environment(\.scenePhase) private var scenePhase
    @State private var selectedTab: TabType = .timetable
    @State private var navigationPath = NavigationPath()
    @State private var aboutNavigationPath = NavigationPath()
    @State private var favoriteNavigationPath = NavigationPath()
    @State private var composeMultiplatformEnabled = false
    @State private var favoriteScreenUiMode: FavoriteScreenUiModePicker.UiMode = .swiftui
    private let presenter = RootPresenter()
    private let notificationUseCase = NotificationUseCaseImpl()
    @State private var notificationCoordinator: NotificationNavigationCoordinator?

    public init() {
        UITabBar.appearance().unselectedItemTintColor = UIColor(named: "tab_inactive")
    }

    public var body: some View {
        Group {
            if composeMultiplatformEnabled {
                KmpAppComposeViewControllerWrapper()
                    .ignoresSafeArea(.all)
            } else {
                ZStack(alignment: .bottom) {
                    tabContent
                    tabBar
                }
            }
        }
        .environment(\.colorScheme, .dark)
        .onAppear {
            // Register custom fonts from Theme bundle so Font.custom can resolve them.
            ThemeFonts.registerAll()
            presenter.prepareWindow()

            // Initialize notification coordinator if not already done
            if notificationCoordinator == nil {
                notificationCoordinator = NotificationNavigationCoordinator(
                    navigateToTimetableDetail: { itemId in
                        // Handle notification navigation
                        // Note: Since this is a struct, we don't need weak references
                        Task { @MainActor in
                            await navigateToSessionFromNotification(itemId: itemId)
                        }
                    }
                )
            }

            // Set up notification navigation handler
            if let coordinator = notificationCoordinator {
                notificationUseCase.setNavigationHandler(coordinator)
            }
        }
        .onChange(of: scenePhase) { _, newPhase in
            ScenePhaseHandler.handle(newPhase)
        }
    }

    @ViewBuilder
    private var tabContent: some View {
        switch selectedTab {
        case .timetable:
            timetableTab
        case .map:
            mapTab
        case .favorite:
            favoriteTab
        case .info:
            infoTab
        case .profileCard:
            profileCardTab
        }
    }

    private var timetableTab: some View {
        NavigationStack(path: $navigationPath) {
            HomeScreen(onNavigate: handleHomeNavigation)
                .navigationDestination(for: NavigationDestination.self) { destination in
                    let navigationHandler = NavigationHandler(
                        handleSearchNavigation: handleSearchNavigation
                    )
                    destination.view(with: navigationHandler)
                }
        }
    }

    private var mapTab: some View {
        NavigationStack {
            EventMapScreen()
        }
    }

    private var favoriteTab: some View {
        NavigationStack(path: $favoriteNavigationPath) {
            ZStack(alignment: .top) {
                switch favoriteScreenUiMode {
                case .swiftui:
                    FavoriteScreen(onNavigate: handleFavoriteNavigation)
                case .kmpPresenter:
                    FavoriteScreen(
                        presenter: KMPFavoriteScreenPresenter(),
                        onNavigate: handleFavoriteNavigation,
                    )
                case .cmp:
                    KMPFavoritesScreenViewControllerWrapper(onNavigate: handleFavoriteNavigation)
                        .ignoresSafeArea(.all)
                }

                HStack {
                    Spacer()
                    FavoriteScreenUiModePicker(uiMode: $favoriteScreenUiMode)
                }
            }
            .navigationDestination(for: FavoriteNavigationDestination.self) { destination in
                switch destination {
                case .timetableDetail(let item):
                    TimetableDetailScreen(timetableItem: item)
                }
            }
        }
    }

    private var infoTab: some View {
        NavigationStack(path: $aboutNavigationPath) {
            AboutScreen(
                onNavigate: handleAboutNavigation,
                onEnableComposeMultiplatform: handleEnableComposeMultiplatform
            )
            .navigationDestination(for: AboutNavigationDestination.self) { destination in
                aboutDestinationView(for: destination)
            }
        }
    }

    @ViewBuilder
    private func aboutDestinationView(for destination: AboutNavigationDestination) -> some View {
        switch destination {
        case .contributors:
            ContributorScreen()
        case .staff:
            StaffScreen()
        case .sponsors:
            SponsorScreen()
        case .licenses:
            Text("Licenses")
                .navigationTitle("Licenses")
        case .settings:
            SettingsScreen()
        }
    }

    private var profileCardTab: some View {
        NavigationStack {
            ProfileCardScreen()
        }
    }

    private func handleHomeNavigation(_ destination: HomeNavigationDestination) {
        switch destination {
        case .timetableDetail(let item):
            navigationPath.append(NavigationDestination.timetableDetail(item))
        case .search:
            navigationPath.append(NavigationDestination.search)
        }
    }

    private func handleAboutNavigation(_ destination: AboutNavigationDestination) {
        aboutNavigationPath.append(destination)
    }

    private func handleFavoriteNavigation(_ destination: FavoriteNavigationDestination) {
        favoriteNavigationPath.append(destination)
    }

    private func handleSearchNavigation(_ destination: SearchNavigationDestination) {
        switch destination {
        case .timetableDetail(let item):
            navigationPath.append(NavigationDestination.timetableDetail(item))
        }
    }

    private func handleEnableComposeMultiplatform() {
        composeMultiplatformEnabled = true
    }

    /// Navigate to a specific session from notification
    @MainActor
    private func navigateToSessionFromNotification(itemId: String) async {
        // Switch to timetable tab first
        selectedTab = .timetable

        // Find the timetable item with the matching ID
        do {
            let timetableItem = try await findTimetableItemById(itemId)

            // Clear existing navigation path and navigate to detail
            navigationPath = NavigationPath()

            // Add a small delay to ensure tab switch is complete
            try await Task.sleep(nanoseconds: 100_000_000)  // 0.1 seconds

            // Navigate to the timetable detail
            navigationPath.append(NavigationDestination.timetableDetail(timetableItem))

        } catch {
            print("Failed to navigate to session \(itemId): \(error)")
            // At least we switched to the timetable tab
        }
    }

    /// Find a timetable item by its ID from the current timetable data
    private func findTimetableItemById(_ itemId: String) async throws -> TimetableItemWithFavorite {
        @Dependency(\.timetableUseCase) var timetableUseCase

        // Get the latest timetable data with timeout to avoid infinite waiting
        let timetableSequence = timetableUseCase.load()

        // Use AsyncSequence.first to get only the first result and avoid infinite loop
        guard let timetable = await timetableSequence.first(where: { @Sendable _ in true }) else {
            throw NotificationNavigationError.navigationFailed("No timetable data available")
        }

        // Use first(where:) for more efficient search instead of manual loop
        guard let item = timetable.timetableItems.first(where: { $0.id.value == itemId }) else {
            throw NotificationNavigationError.itemNotFound(itemId)
        }

        let isFavorited = timetable.bookmarks.contains(item.id)
        return TimetableItemWithFavorite(timetableItem: item, isFavorited: isFavorited)
    }

    @ViewBuilder
    private var tabBar: some View {
        GeometryReader { geometry in
            HStack(spacing: 0) {
                ForEach(TabType.allCases, id: \.self) { item in
                    let isSelected = selectedTab == item
                    Button {
                        selectedTab = item
                    } label: {
                        item.tabImage(selectedTab).swiftUIImage
                            .renderingMode(.template)
                            .tint(
                                isSelected
                                    ? AssetColors.primary40.swiftUIColor : AssetColors.onSurfaceVariant.swiftUIColor
                            )
                            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                            .contentShape(Rectangle())
                    }
                    .frame(
                        maxWidth: geometry.size.width / CGFloat(TabType.allCases.count),
                        maxHeight: .infinity,
                        alignment: .center
                    )
                }
            }
            .frame(width: geometry.size.width, height: geometry.size.height)
        }
        .frame(height: 64)
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 12)
        .background(.ultraThinMaterial, in: Capsule())
        .overlay(Capsule().stroke(AssetColors.outline.swiftUIColor, lineWidth: 1))
        .environment(\.colorScheme, .dark)
        .padding(.horizontal, 48)
    }
}

#Preview {
    RootScreen()
}

// MARK: - Error Types

/// Errors that can occur during notification navigation
enum NotificationNavigationError: Error, LocalizedError {
    case itemNotFound(String)
    case navigationFailed(String)

    var errorDescription: String? {
        switch self {
        case .itemNotFound(let itemId):
            return "Session with ID '\(itemId)' not found"
        case .navigationFailed(let reason):
            return "Navigation failed: \(reason)"
        }
    }
}
