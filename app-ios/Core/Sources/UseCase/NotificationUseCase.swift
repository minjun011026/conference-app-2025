import Dependencies
import DependenciesMacros
import Model

@DependencyClient
public struct NotificationUseCase: Sendable {
    /// Load current notification settings
    public var load: @Sendable () async -> NotificationSettings = { NotificationSettings() }

    /// Save notification settings
    public var save: @Sendable (NotificationSettings) async -> Void = { _ in }

    /// Request notification permission from user
    public var requestPermission: @Sendable () async -> Bool = { false }

    /// Check current notification authorization status
    public var checkAuthorizationStatus: @Sendable () async -> NotificationAuthorizationStatus = { .notDetermined }

    /// Schedule notification for a specific timetable item
    public var scheduleNotification: @Sendable (TimetableItemWithFavorite, NotificationSettings) async -> Bool = {
        _, _ in false
    }

    /// Cancel notification for a specific timetable item
    public var cancelNotification: @Sendable (TimetableItemId) async -> Void = { _ in }

    /// Reschedule all notifications based on current favorites and settings
    public var rescheduleAllNotifications: @Sendable ([TimetableItemWithFavorite], NotificationSettings) async -> Void =
        { _, _ in }

    /// Cancel all pending notifications
    public var cancelAllNotifications: @Sendable () async -> Void = {}
}

/// Notification authorization status
public enum NotificationAuthorizationStatus: Sendable {
    case notDetermined
    case denied
    case authorized
    case provisional
}

public enum NotificationUseCaseKey: TestDependencyKey {
    public static let testValue = NotificationUseCase()
}

extension DependencyValues {
    public var notificationUseCase: NotificationUseCase {
        get { self[NotificationUseCaseKey.self] }
        set { self[NotificationUseCaseKey.self] = newValue }
    }
}
