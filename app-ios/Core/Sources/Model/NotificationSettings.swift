import Foundation

/// Settings model for managing notification preferences
public struct NotificationSettings: Equatable, Sendable {
    /// Whether notifications are enabled globally
    public let isEnabled: Bool

    /// How many minutes before the session to send notification
    public let reminderMinutes: Int

    /// Whether to use custom notification sound
    public let useCustomSound: Bool

    /// Whether notifications should only be sent for favorite sessions
    public let favoritesOnly: Bool

    public init(
        isEnabled: Bool = false,
        reminderMinutes: Int = 10,
        useCustomSound: Bool = false,
        favoritesOnly: Bool = true
    ) {
        self.isEnabled = isEnabled
        self.reminderMinutes = reminderMinutes
        self.useCustomSound = useCustomSound
        self.favoritesOnly = favoritesOnly
    }
}

/// Available reminder time options
public enum NotificationReminderTime: Int, CaseIterable, Identifiable {
    case fiveMinutes = 5
    case tenMinutes = 10
    case fifteenMinutes = 15
    case thirtyMinutes = 30
    case oneHour = 60

    public var id: Int { rawValue }

    public var displayText: String {
        switch self {
        case .fiveMinutes:
            return "5 minutes before"
        case .tenMinutes:
            return "10 minutes before"
        case .fifteenMinutes:
            return "15 minutes before"
        case .thirtyMinutes:
            return "30 minutes before"
        case .oneHour:
            return "1 hour before"
        }
    }
}
