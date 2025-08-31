import Foundation
import os.log

public final class NotificationLaunchHandler {
    @MainActor public static let shared = NotificationLaunchHandler()

    private let logger = Logger(subsystem: "io.github.droidkaigi.dk2025", category: "NotificationLaunch")
    private var launchNotificationUserInfo: [AnyHashable: Any]?

    private init() {}

    public func storeLaunchNotification(_ userInfo: [AnyHashable: Any]) {
        logger.info("Storing launch notification info: \(userInfo)")
        launchNotificationUserInfo = userInfo
    }

    public func consumeLaunchNotification() -> [AnyHashable: Any]? {
        defer { launchNotificationUserInfo = nil }
        return launchNotificationUserInfo
    }

    public var hasLaunchNotification: Bool {
        return launchNotificationUserInfo != nil
    }
}
