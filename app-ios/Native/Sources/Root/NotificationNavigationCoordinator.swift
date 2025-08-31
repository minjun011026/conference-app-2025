import Foundation
import Handler
import Model
import SwiftUI
import UseCase
import os.log

@MainActor
final class NotificationNavigationCoordinator: NotificationNavigationHandler {
    private let logger = Logger(subsystem: "io.github.droidkaigi.dk2025", category: "NotificationNavigation")

    private let navigateToTimetableDetail: (String) -> Void

    init(navigateToTimetableDetail: @escaping (String) -> Void) {
        self.navigateToTimetableDetail = navigateToTimetableDetail
        logger.debug("NotificationNavigationCoordinator initialized")
    }

    func navigateToSession(itemId: String) async {
        logger.info("Navigating to session with ID: \(itemId)")

        guard !itemId.isEmpty else {
            logger.error("Cannot navigate: empty item ID")
            return
        }

        // Delegate navigation to RootScreen which handles the actual item finding and navigation
        await MainActor.run {
            navigateToTimetableDetail(itemId)
            logger.info("Initiated navigation to session with ID: \(itemId)")
        }
    }
}
