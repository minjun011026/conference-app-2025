@preconcurrency import BackgroundTasks
import Dependencies
import Model
import SwiftUI
import UseCase
@preconcurrency import UserNotifications
import os.log

public enum BackgroundTaskHandler {
    private static let logger = Logger(subsystem: "io.github.droidkaigi.dk2025", category: "BackgroundTasks")
    private static let taskIdentifier = "io.github.droidkaigi.dk2025.notification-refresh"

    public static func registerBackgroundTasks() {
        if #available(iOS 13.0, *) {
            BGTaskScheduler.shared.register(
                forTaskWithIdentifier: taskIdentifier,
                using: nil
            ) { task in
                guard let refreshTask = task as? BGAppRefreshTask else {
                    logger.warning("Received task is not BGAppRefreshTask")
                    return
                }

                // Handle the background refresh task properly
                // Execute background task handling asynchronously without creating a new Task
                // to avoid data race warnings. The BGTaskScheduler callback is already designed
                // to handle concurrent operations safely.
                handleBackgroundRefreshTaskAsync(refreshTask)
            }
            logger.info("Registered background task: \(taskIdentifier)")
        }
    }

    @available(iOS 13.0, *)
    private static func handleBackgroundRefreshTaskAsync(_ task: BGAppRefreshTask) {
        // BGAppRefreshTask is managed by the system and is safe to use across task boundaries
        // We explicitly mark this as @Sendable since the task object is thread-safe in this context
        Task.detached(priority: .background) { @Sendable in
            // BGAppRefreshTask is safe to use here as it's managed by the system's background task scheduler
            await handleBackgroundRefreshTask(task)
        }
    }

    @available(iOS 13.0, *)
    public static func handleBackgroundRefreshTask(_ task: BGAppRefreshTask) async {
        logger.info("Handling background refresh task")

        // Set completion handler - default to success
        var taskCompleted = false
        defer {
            if !taskCompleted {
                task.setTaskCompleted(success: true)
            }
        }

        // Get notification settings
        @Dependency(\.notificationUseCase) var notificationUseCase
        let settings = await notificationUseCase.load()

        guard settings.isEnabled else {
            logger.debug("Notifications disabled, skipping background refresh")
            task.setTaskCompleted(success: true)
            taskCompleted = true
            return
        }

        let authStatus = await notificationUseCase.checkAuthorizationStatus()
        guard authStatus == .authorized else {
            logger.debug(
                "Notifications not authorized (\(String(describing: authStatus))), skipping background refresh")
            task.setTaskCompleted(success: true)
            taskCompleted = true
            return
        }

        // Refresh notifications with latest data
        await updateNotificationsWithLatestData(settings: settings)

        task.setTaskCompleted(success: true)
        taskCompleted = true
        logger.info("Background refresh task completed successfully")
    }

    public static func scheduleBackgroundRefreshIfNeeded() async {
        logger.debug("Scheduling background refresh if needed")

        // Check if notifications are enabled
        @Dependency(\.notificationUseCase) var notificationUseCase
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

        // Check Background App Refresh availability
        if #available(iOS 13.0, *) {
            let backgroundRefreshStatus = await UIApplication.shared.backgroundRefreshStatus

            switch backgroundRefreshStatus {
            case .denied:
                logger.info("Background App Refresh is disabled system-wide")
                return
            case .restricted:
                logger.info("Background App Refresh is restricted")
                return
            case .available:
                logger.debug("Background App Refresh is available")
            @unknown default:
                logger.warning("Unknown background refresh status")
            }

            // Schedule background task
            let request = BGAppRefreshTaskRequest(identifier: taskIdentifier)
            request.earliestBeginDate = Date(timeIntervalSinceNow: 15 * 60)  // 15 minutes from now

            do {
                try BGTaskScheduler.shared.submit(request)
                logger.info("Successfully scheduled background refresh task")
            } catch {
                handleBackgroundTaskSchedulingError(error)
            }
        } else {
            logger.debug("Background task scheduling not available (iOS < 13)")
        }
    }

    private static func handleBackgroundTaskSchedulingError(_ error: Error) {
        if let bgError = error as? NSError, bgError.domain == "BGTaskSchedulerErrorDomain" {
            switch bgError.code {
            case 1:  // BGTaskSchedulerErrorCodeUnavailable
                logger.info("Background app refresh is disabled or Low Power Mode is on: \(error.localizedDescription)")
            case 2:  // BGTaskSchedulerErrorCodeTooManyPendingTaskRequests
                logger.warning("Too many pending background tasks: \(error.localizedDescription)")
            case 3:  // BGTaskSchedulerErrorCodeNotPermitted
                logger.debug("Background refresh not permitted (likely in simulator): \(error.localizedDescription)")
            default:
                logger.error("Failed to schedule background refresh: \(error.localizedDescription)")
            }
        } else {
            logger.error("Failed to schedule background refresh: \(error.localizedDescription)")
        }
    }

    private static func updateNotificationsWithLatestData(settings: NotificationSettings) async {
        @Dependency(\.timetableUseCase) var timetableUseCase
        @Dependency(\.notificationUseCase) var notificationUseCase

        // Get current timetable data
        let timetableSequence = timetableUseCase.load()

        guard
            let timetable = await timetableSequence.first(where: { @Sendable _ in
                true
            })
        else {
            logger.warning("No timetable data available for notification update")
            return
        }

        // Convert to TimetableItemWithFavorite
        let allItems = timetable.timetableItems.map { item in
            let isFavorited = timetable.bookmarks.contains(item.id)
            return TimetableItemWithFavorite(timetableItem: item, isFavorited: isFavorited)
        }

        let favoriteItems = allItems.filter { $0.isFavorited }
        logger.info("Background refresh: \(allItems.count) total, \(favoriteItems.count) favorites")

        // Update notifications
        await notificationUseCase.rescheduleAllNotifications(allItems, settings)

        logger.info("Background refresh: Updated notifications for \(favoriteItems.count) favorites")
    }
}
