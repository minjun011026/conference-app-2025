import Foundation

@MainActor
public protocol NotificationNavigationHandler: AnyObject {
    func navigateToSession(itemId: String) async
}
