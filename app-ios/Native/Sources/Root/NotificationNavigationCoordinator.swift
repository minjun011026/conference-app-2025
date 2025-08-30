import Foundation
import SwiftUI
import Model
import UseCase
import os.log

/// Concrete implementation of NotificationNavigationHandler that coordinates navigation from notifications
@MainActor
final class NotificationNavigationCoordinator: NotificationNavigationHandler {
    private let logger = Logger(subsystem: "io.github.droidkaigi.dk2025", category: "NotificationNavigation")

    /// Closure to handle navigation from root level
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

        // Find the session by ID and navigate
        do {
            let timetableItem = try await findTimetableItem(by: itemId)

            // Switch to timetable tab and navigate to detail
            await MainActor.run {
                navigateToTimetableDetail(itemId)
                logger.info("Successfully navigated to session: \(timetableItem.title.currentLangTitle)")
            }
        } catch {
            logger.error("Failed to navigate to session \(itemId): \(error.localizedDescription)")
        }
    }

    /// Find a timetable item by its ID
    private func findTimetableItem(by itemId: String) async throws -> any TimetableItem {
        // This method is currently not used as RootScreen handles the actual navigation
        // and item finding logic. Keeping this as a placeholder for potential future use.
        logger.debug("Finding timetable item with ID: \(itemId)")

        throw NotificationError.invalidItemId(itemId)
    }
}

// MARK: - Helper Extension
// currentLangTitle extension is already defined in KMPConverters.swift
