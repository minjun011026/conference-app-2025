import Foundation
import Handler
import Model
import UseCase
import UserNotifications
import os.log

final class NotificationUseCaseImpl: NSObject, @unchecked Sendable {
    private let notificationCenter = UNUserNotificationCenter.current()
    private let userDefaults = UserDefaults.standard
    private let logger = Logger(subsystem: "io.github.droidkaigi.dk2025", category: "Notifications")

    private static let maxNotificationLimit = 64

    private weak var navigationHandler: NotificationNavigationHandler?

    private enum StorageKeys {
        static let enabled = "notification_enabled"
        static let reminderMinutes = "notification_reminder_minutes"
        static let customSound = "notification_custom_sound"
        static let favoritesOnly = "notification_favorites_only"
        static let lastScheduledVersion = "notification_last_scheduled_version"
    }

    override init() {
        super.init()
        setupNotificationDelegate()
        setupNotificationCategories()
    }

    private func setupNotificationDelegate() {
        // Only set delegate if not already set or if it's not us
        if notificationCenter.delegate == nil || notificationCenter.delegate !== self {
            notificationCenter.delegate = self
            logger.debug("Set notification center delegate")
        }
    }

    private func setupNotificationCategories() {
        let viewAction = UNNotificationAction(
            identifier: "VIEW_SESSION",
            title: NSLocalizedString("View Session", comment: "Notification action to view session"),
            options: [.foreground]
        )

        let dismissAction = UNNotificationAction(
            identifier: "DISMISS",
            title: NSLocalizedString("Dismiss", comment: "Notification action to dismiss"),
            options: []
        )

        let sessionCategory = UNNotificationCategory(
            identifier: "SESSION_REMINDER",
            actions: [viewAction, dismissAction],
            intentIdentifiers: [],
            options: [.customDismissAction]
        )

        notificationCenter.setNotificationCategories([sessionCategory])
        logger.debug("Set up notification categories")
    }

    func setNavigationHandler(_ handler: NotificationNavigationHandler?) {
        self.navigationHandler = handler
        logger.debug("Navigation handler \(handler != nil ? "set" : "cleared")")
    }

    deinit {
        // Clear delegate if it's still us to avoid dangling reference
        if notificationCenter.delegate === self {
            notificationCenter.delegate = nil
            logger.debug("Cleared notification center delegate on deinit")
        }
    }

    func load() async -> NotificationSettings {
        logger.info("Loading notification settings")

        let isEnabled = userDefaults.bool(forKey: StorageKeys.enabled)
        let reminderMinutes = userDefaults.object(forKey: StorageKeys.reminderMinutes) as? Int ?? 10
        let useCustomSound = userDefaults.bool(forKey: StorageKeys.customSound)
        let favoritesOnly = userDefaults.object(forKey: StorageKeys.favoritesOnly) as? Bool ?? true

        let settings = NotificationSettings(
            isEnabled: isEnabled,
            reminderMinutes: reminderMinutes,
            useCustomSound: useCustomSound,
            favoritesOnly: favoritesOnly
        )

        logger.debug(
            "Loaded settings: enabled=\(settings.isEnabled), reminderMinutes=\(settings.reminderMinutes), favoritesOnly=\(settings.favoritesOnly)"
        )
        return settings
    }

    func save(_ settings: NotificationSettings) async {
        logger.info("Saving notification settings")

        userDefaults.set(settings.isEnabled, forKey: StorageKeys.enabled)
        userDefaults.set(settings.reminderMinutes, forKey: StorageKeys.reminderMinutes)
        userDefaults.set(settings.useCustomSound, forKey: StorageKeys.customSound)
        userDefaults.set(settings.favoritesOnly, forKey: StorageKeys.favoritesOnly)

        // Increment version to trigger re-scheduling
        let currentVersion = userDefaults.integer(forKey: StorageKeys.lastScheduledVersion)
        userDefaults.set(currentVersion + 1, forKey: StorageKeys.lastScheduledVersion)

        logger.debug("Settings saved successfully")
    }

    func requestPermission() async -> Bool {
        logger.info("Requesting notification permission")

        do {
            let granted = try await notificationCenter.requestAuthorization(
                options: [.alert, .sound, .badge]
            )
            logger.info("Permission request result: \(granted ? "granted" : "denied")")
            return granted
        } catch {
            logger.error("Error requesting notification permission: \(error.localizedDescription)")
            return false
        }
    }

    func checkAuthorizationStatus() async -> NotificationAuthorizationStatus {
        let settings = await notificationCenter.notificationSettings()

        let status: NotificationAuthorizationStatus
        switch settings.authorizationStatus {
        case .notDetermined:
            status = .notDetermined
        case .denied:
            status = .denied
        case .authorized:
            status = .authorized
        case .provisional:
            status = .provisional
        case .ephemeral:
            status = .authorized
        @unknown default:
            status = .notDetermined
        }

        logger.debug("Authorization status: \(String(describing: status))")
        return status
    }

    func scheduleNotification(for item: TimetableItemWithFavorite, with settings: NotificationSettings) async -> Bool {
        guard settings.isEnabled else {
            logger.debug("Notifications disabled, skipping schedule for \(item.id.value)")
            return false
        }

        guard !settings.favoritesOnly || item.isFavorited else {
            logger.debug("Not favorited and favorites-only enabled, skipping \(item.id.value)")
            return false
        }

        // Use Japan Standard Time (JST) for conference events
        let jstTimeZone = TimeZone(identifier: "Asia/Tokyo") ?? TimeZone.current
        var jstCalendar = Calendar(identifier: .gregorian)
        jstCalendar.timeZone = jstTimeZone

        // Calculate notification time in JST
        let sessionStartTime = item.timetableItem.startsAt
        let reminderInterval = TimeInterval(settings.reminderMinutes * 60)
        let notificationTime = sessionStartTime.addingTimeInterval(-reminderInterval)

        // Don't schedule notifications for past events (compare in JST timezone)
        let currentTimeInJST = Date()

        guard notificationTime > currentTimeInJST else {
            logger.debug("Notification time is in the past for session \(item.id.value), skipping (JST comparison)")
            return false
        }

        // Create notification content with localized strings
        let content = UNMutableNotificationContent()
        content.title = NSLocalizedString("DroidKaigi Session Reminder", comment: "Notification title")

        let bodyFormat = NSLocalizedString("%@ starts in %d minutes", comment: "Notification body format")
        content.body = String(format: bodyFormat, item.timetableItem.title.currentLangTitle, settings.reminderMinutes)

        // Add custom sound if enabled and available
        if settings.useCustomSound {
            // Check if custom sound file exists in bundle
            if let soundPath = Bundle.main.path(forResource: "droidkaigi_notification", ofType: "wav"),
                FileManager.default.fileExists(atPath: soundPath)
            {
                content.sound = UNNotificationSound(named: UNNotificationSoundName("droidkaigi_notification.wav"))
                logger.debug("Using custom notification sound")
            } else {
                logger.warning(
                    "Custom sound file 'droidkaigi_notification.wav' not found in bundle, using default sound")
                content.sound = .default
            }
        } else {
            content.sound = .default
        }

        // Rich notification data
        content.userInfo = [
            "itemId": item.id.value,
            "sessionTitle": item.timetableItem.title.currentLangTitle,
            "room": item.timetableItem.room.name.currentLangTitle,
            "startTime": sessionStartTime.timeIntervalSince1970,
            "reminderMinutes": settings.reminderMinutes,
        ]

        // Add category for actionable notifications
        content.categoryIdentifier = "SESSION_REMINDER"

        // Create date trigger using JST timezone
        var dateComponents = jstCalendar.dateComponents([.year, .month, .day, .hour, .minute], from: notificationTime)
        dateComponents.timeZone = jstTimeZone
        let trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: false)

        // Create request
        let identifier = "session-\(item.id.value)"
        let request = UNNotificationRequest(
            identifier: identifier,
            content: content,
            trigger: trigger
        )

        do {
            try await notificationCenter.add(request)
            logger.info(
                "Scheduled notification for session '\(item.timetableItem.title.currentLangTitle)' at \(notificationTime)"
            )
            return true
        } catch {
            logger.error("Error scheduling notification for \(item.id.value): \(error.localizedDescription)")
            return false
        }
    }

    func cancelNotification(for itemId: TimetableItemId) async {
        let identifier = "session-\(itemId.value)"
        notificationCenter.removePendingNotificationRequests(withIdentifiers: [identifier])
        logger.info("Cancelled notification for session ID: \(itemId.value)")
    }

    func rescheduleAllNotifications(for items: [TimetableItemWithFavorite], with settings: NotificationSettings) async {
        logger.info("Rescheduling all notifications for \(items.count) items")

        // Cancel all existing notifications
        await cancelAllNotifications()

        // Schedule new notifications if enabled
        guard settings.isEnabled else {
            logger.info("Notifications disabled, not scheduling any notifications")
            return
        }

        let authStatus = await checkAuthorizationStatus()
        guard authStatus == .authorized || authStatus == .provisional else {
            logger.warning("Notifications not authorized, status: \(String(describing: authStatus))")
            return
        }

        // Filter eligible items
        let eligibleItems = items.filter { item in
            !settings.favoritesOnly || item.isFavorited
        }

        // Apply 64-notification limit with priority system
        let prioritizedItems = prioritizeNotifications(eligibleItems, limit: Self.maxNotificationLimit)

        logger.info("Scheduling \(prioritizedItems.count) notifications (limited by iOS constraint)")

        // Schedule notifications for prioritized items
        var successCount = 0
        for item in prioritizedItems {
            if await scheduleNotification(for: item, with: settings) {
                successCount += 1
            }
        }

        logger.info("Successfully scheduled \(successCount) out of \(prioritizedItems.count) notifications")
    }

    func cancelAllNotifications() async {
        logger.info("Cancelling all notifications")
        notificationCenter.removeAllPendingNotificationRequests()
    }

    private func prioritizeNotifications(
        _ items: [TimetableItemWithFavorite], limit: Int
    ) -> [TimetableItemWithFavorite] {
        let sortedItems = items.sorted { item1, item2 in
            // First priority: favorited status
            if item1.isFavorited != item2.isFavorited {
                return item1.isFavorited && !item2.isFavorited
            }

            // Second priority: earlier sessions first
            return item1.timetableItem.startsAt < item2.timetableItem.startsAt
        }

        // Filter out past sessions (using JST timezone for consistency)
        _ = TimeZone(identifier: "Asia/Tokyo") ?? TimeZone.current
        let currentTime = Date()
        let futureItems = sortedItems.filter { item in
            // Compare session start time with current time
            item.timetableItem.startsAt > currentTime
        }

        // Apply limit
        return Array(futureItems.prefix(limit))
    }
}

extension NotificationUseCaseImpl: UNUserNotificationCenterDelegate {
    nonisolated func userNotificationCenter(
        _ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        let userInfo = response.notification.request.content.userInfo

        Logger(subsystem: "io.github.droidkaigi.dk2025", category: "Notifications")
            .info("Notification response received with action: \(response.actionIdentifier)")

        if let itemIdString = userInfo["itemId"] as? String {
            Logger(subsystem: "io.github.droidkaigi.dk2025", category: "Notifications")
                .info("User tapped notification for session: \(itemIdString)")

            // Handle different action types
            switch response.actionIdentifier {
            case "VIEW_SESSION", UNNotificationDefaultActionIdentifier:
                // Navigate to session detail
                // Extract needed values to avoid capturing the entire dictionary
                let sessionTitle = userInfo["sessionTitle"] as? String ?? "Unknown Session"
                let room = userInfo["room"] as? String ?? "Unknown Room"
                let itemId = itemIdString  // Create local copy to avoid sending parameter race
                Task { @MainActor in
                    await handleNotificationTap(itemId: itemId, sessionTitle: sessionTitle, room: room)
                }
            case "DISMISS", UNNotificationDismissActionIdentifier:
                Logger(subsystem: "io.github.droidkaigi.dk2025", category: "Notifications")
                    .debug("User dismissed notification for session: \(itemIdString)")
            default:
                Logger(subsystem: "io.github.droidkaigi.dk2025", category: "Notifications")
                    .warning("Unknown notification action: \(response.actionIdentifier)")
            }
        } else {
            Logger(subsystem: "io.github.droidkaigi.dk2025", category: "Notifications")
                .warning("Notification response missing itemId")
        }

        completionHandler()
    }

    nonisolated func userNotificationCenter(
        _ center: UNUserNotificationCenter, willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        Logger(subsystem: "io.github.droidkaigi.dk2025", category: "Notifications")
            .debug("Presenting notification in foreground")
        // Show notification even when app is in foreground
        completionHandler([.banner, .sound, .badge])
    }

    private func handleNotificationTap(itemId: String, sessionTitle: String, room: String) async {
        guard let navigationHandler = self.navigationHandler else {
            logger.warning("No navigation handler available for notification tap")
            return
        }

        logger.info("Navigating to session: \(sessionTitle) in \(room)")

        // Navigate to session detail
        await navigationHandler.navigateToSession(itemId: itemId)
    }
}

enum NotificationError: Error, LocalizedError {
    case permissionDenied
    case soundFileNotFound(String)
    case schedulingFailed(String)
    case invalidItemId(String)
    case navigationHandlerMissing

    var errorDescription: String? {
        switch self {
        case .permissionDenied:
            return NSLocalizedString("Notification permission was denied", comment: "Permission error")
        case .soundFileNotFound(let filename):
            return NSLocalizedString("Sound file '\(filename)' not found in app bundle", comment: "Sound file error")
        case .schedulingFailed(let reason):
            return NSLocalizedString("Failed to schedule notification: \(reason)", comment: "Scheduling error")
        case .invalidItemId(let itemId):
            return NSLocalizedString("Invalid item ID: \(itemId)", comment: "Invalid ID error")
        case .navigationHandlerMissing:
            return NSLocalizedString("Navigation handler not available", comment: "Navigation error")
        }
    }
}
