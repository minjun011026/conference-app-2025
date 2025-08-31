import Dependencies
import Model
import SwiftUI
@preconcurrency import UserNotifications
import os.log

public enum ScenePhaseHandler {
    private static let logger = Logger(subsystem: "io.github.droidkaigi.dk2025", category: "ScenePhase")

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
        logger.info("Scene became active")

        Task {
            // Clear badge count when app becomes active
            await clearBadgeCount()

            // Check if notification permission status changed while app was inactive
            await refreshNotificationStatusIfNeeded()
        }
    }

    private static func handleAppWillResignActive() {
        logger.info("Scene will resign active")
        // Scene is about to lose focus (could be due to notifications, calls, etc.)
    }

    private static func handleAppDidEnterBackground() {
        logger.info("Scene entered background")

        Task {
            // Schedule background app refresh if needed
            await BackgroundTaskHandler.scheduleBackgroundRefreshIfNeeded()
        }
    }

    private static func clearBadgeCount() async {
        do {
            try await UNUserNotificationCenter.current().setBadgeCount(0)
            logger.debug("Badge count cleared")
        } catch {
            logger.error("Failed to set badge count: \(error.localizedDescription)")
        }
    }

    private static func refreshNotificationStatusIfNeeded() async {
        logger.debug("Refreshing notification authorization status")

        // Check current authorization status
        let notificationCenter = UNUserNotificationCenter.current()
        let settings = await notificationCenter.notificationSettings()

        logger.debug("Current notification authorization: \(settings.authorizationStatus.rawValue)")

        // Get current app settings
        @Dependency(\.notificationUseCase) var notificationUseCase
        let currentSettings = await notificationUseCase.load()

        // Post notifications for UI updates based on permission changes
        await handleNotificationPermissionChanges(
            systemStatus: settings.authorizationStatus,
            appSettings: currentSettings
        )
    }

    private static func handleNotificationPermissionChanges(
        systemStatus: UNAuthorizationStatus,
        appSettings: NotificationSettings
    ) async {
        await MainActor.run {
            // If notifications are enabled in app but denied by system
            if appSettings.isEnabled && systemStatus == .denied {
                logger.warning("Notifications enabled in app but denied by system")

                NotificationCenter.default.post(
                    name: Notification.Name("NotificationPermissionStatusChanged"),
                    object: nil,
                    userInfo: ["status": "denied", "appEnabled": appSettings.isEnabled]
                )
            }

            // If authorization status changed to authorized and we have enabled settings
            if systemStatus == .authorized && appSettings.isEnabled {
                logger.info("Notifications are authorized and enabled - triggering refresh")

                NotificationCenter.default.post(
                    name: Notification.Name("RefreshNotificationSchedules"),
                    object: nil
                )
            }
        }
    }
}
