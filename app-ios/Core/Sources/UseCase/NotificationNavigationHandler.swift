import Foundation

/// Protocol for handling notification navigation
@MainActor
public protocol NotificationNavigationHandler: AnyObject {
    func navigateToSession(itemId: String) async
}
