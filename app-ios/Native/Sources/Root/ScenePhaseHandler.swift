import BackgroundTasks
import Dependencies
import Model
import SwiftUI
import UseCase
@preconcurrency import UserNotifications
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
            do {
                try await UNUserNotificationCenter.current().setBadgeCount(0)
            } catch {
                logger.error("Failed to set badge count: \(error.localizedDescription)")
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

    private static func refreshNotificationStatusIfNeeded() async {
        // This could be called when the app returns from Settings where user might have changed notification permissions
        logger.debug("Refreshing notification authorization status")

        // Check current authorization status
        let notificationCenter = UNUserNotificationCenter.current()
        let settings = await notificationCenter.notificationSettings()

        logger.info("Current notification authorization: \(settings.authorizationStatus.rawValue)")

        // Get current app settings
        let notificationUseCase = NotificationUseCaseImpl()
        let currentSettings = await notificationUseCase.load()

        // If notifications are enabled in app but not authorized by system,
        // consider updating the app settings or showing a prompt
        if currentSettings.isEnabled && settings.authorizationStatus == .denied {
            logger.warning("Notifications enabled in app but denied by system - user may need to re-enable in Settings")

            // Here you could post a notification to update UI or show an alert
            await MainActor.run {
                NotificationCenter.default.post(
                    name: Notification.Name("NotificationPermissionStatusChanged"),
                    object: nil,
                    userInfo: ["status": "denied", "appEnabled": currentSettings.isEnabled]
                )
            }
        }

        // If authorization status changed to authorized and we have pending settings
        if settings.authorizationStatus == .authorized && currentSettings.isEnabled {
            logger.info("Notifications are authorized and enabled - scheduling pending notifications")

            // Trigger a refresh of scheduled notifications
            await MainActor.run {
                NotificationCenter.default.post(
                    name: Notification.Name("RefreshNotificationSchedules"),
                    object: nil
                )
            }
        }
    }

    private static func scheduleBackgroundRefreshIfNeeded() async {
        logger.debug("Scheduling background refresh if needed")

        // Check if we have notification permissions and notifications are enabled
        let notificationUseCase = NotificationUseCaseImpl()
        let settings = await notificationUseCase.load()

        guard settings.isEnabled else {
            logger.debug("Notifications disabled, skipping background refresh scheduling")
            return
        }

        let authStatus = await notificationUseCase.checkAuthorizationStatus()
        guard authStatus == .authorized else {
            logger.debug("Notifications not authorized, skipping background refresh scheduling")
            return
        }

        // Check if we need to refresh notification schedules based on timetable changes
        // This helps handle cases where conference schedules change while app is in background

        @Dependency(\.timetableUseCase) var timetableUseCase

        // Schedule a background task to refresh notifications if data changes
        let backgroundTaskName = "io.github.droidkaigi.dk2025.notification-refresh"

        // Use BGAppRefreshTask for iOS 13+
        if #available(iOS 13.0, *) {
            let request = BGAppRefreshTaskRequest(identifier: backgroundTaskName)
            request.earliestBeginDate = Date(timeIntervalSinceNow: 15 * 60)  // 15 minutes from now

            do {
                try BGTaskScheduler.shared.submit(request)
                logger.info("Successfully scheduled background refresh task")
            } catch {
                logger.error("Failed to schedule background refresh: \(error.localizedDescription)")
            }
        } else {
            // Fallback for older iOS versions using background app refresh
            logger.debug("Using legacy background app refresh (iOS < 13)")
        }

        // Also check if we should reschedule notifications due to potential data changes
        await checkAndRescheduleNotificationsIfNeeded(settings: settings)
    }

    private static func checkAndRescheduleNotificationsIfNeeded(settings: NotificationSettings) async {
        @Dependency(\.timetableUseCase) var timetableUseCase
        let notificationUseCase = NotificationUseCaseImpl()

        // Get current timetable to check for changes with improved error handling
        let timetableSequence = timetableUseCase.load()

        // Use first(where:) to get only the first result efficiently
        guard let timetable = await timetableSequence.first(where: { @Sendable _ in true }) else {
            logger.warning("No timetable data available for notification rescheduling")
            return
        }

        // Convert timetable items to TimetableItemWithFavorite
        let allItems = timetable.timetableItems.map { item in
            let isFavorited = timetable.bookmarks.contains(item.id)
            return TimetableItemWithFavorite(timetableItem: item, isFavorited: isFavorited)
        }

        // Reschedule notifications based on current data
        await notificationUseCase.rescheduleAllNotifications(for: allItems, with: settings)
        logger.info("Rescheduled notifications for \(allItems.count) timetable items in background")
    }
}
