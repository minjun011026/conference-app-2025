import SwiftUI
import UserNotifications
import Dependencies
import UseCase
import os.log

enum ScenePhaseHandler {
    private static let logger = Logger(subsystem: "io.github.droidkaigi.dk2025", category: "AppLifecycle")

    static func handle(_ scenePhase: ScenePhase) {
        switch scenePhase {
        case .active:
            handleAppDidBecomeActive()

        case .inactive:
            handleAppWillResignActive()

        case .background:
            handleAppDidEnterBackground()

        @unknown default:
            break
        }
    }

    private static func handleAppDidBecomeActive() {
        logger.info("App became active")

        Task {
            // Check if notification permission status changed while app was inactive
            await refreshNotificationStatusIfNeeded()

            // Clear badge count when app becomes active
            await MainActor.run {
                UNUserNotificationCenter.current().setBadgeCount(0)
            }
        }
    }

    private static func handleAppWillResignActive() {
        logger.info("App will resign active")
        // App is about to lose focus (could be due to notifications, calls, etc.)
    }

    private static func handleAppDidEnterBackground() {
        logger.info("App entered background")

        Task {
            // Schedule background app refresh if needed
            await scheduleBackgroundRefreshIfNeeded()
        }
    }

    @MainActor
    private static func refreshNotificationStatusIfNeeded() async {
        // This could be called when the app returns from Settings where user might have changed notification permissions
        logger.debug("Refreshing notification authorization status")

        // If you have a global notification provider, refresh its status here
        // For now, we'll let individual screens handle this when they become visible
    }

    private static func scheduleBackgroundRefreshIfNeeded() async {
        logger.debug("Scheduling background refresh if needed")

        // Here you could schedule background app refresh to update notification schedules
        // when timetable data changes while app is in background
        // This is particularly useful for conference apps where schedules might change
    }
}
