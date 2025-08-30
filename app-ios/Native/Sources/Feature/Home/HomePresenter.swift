import Foundation
import Model
import Observation
import Presentation

@MainActor
@Observable
final class HomePresenter {
    let timetable = TimetableProvider()
    let notificationProvider = NotificationProvider()

    init() {
        // Connect notification provider to timetable provider for automatic scheduling
        timetable.setNotificationProvider(notificationProvider)
    }

    func loadInitial() {
        // Initialize notification provider first
        notificationProvider.initialize()
        // Then load timetable data
        timetable.subscribeTimetableIfNeeded()
    }

    func timetableItemTapped(_ item: TimetableItemWithFavorite) {
        // print("Tapped: \(item.timetableItem.title)")
    }

    func searchTapped() {
        // print("Search tapped")
    }
}
