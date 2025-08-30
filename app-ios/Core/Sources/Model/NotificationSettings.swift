import Foundation

public struct NotificationSettings: Equatable, Sendable {
    public let isEnabled: Bool

    public let reminderMinutes: Int

    public let useCustomSound: Bool

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

public enum NotificationReminderTime: Int, CaseIterable, Identifiable {
    case fiveMinutes = 5
    case tenMinutes = 10

    public var id: Int { rawValue }

    public var displayText: String {
        switch self {
        case .fiveMinutes:
            return "5 minutes before"
        case .tenMinutes:
            return "10 minutes before"
        }
    }
}
