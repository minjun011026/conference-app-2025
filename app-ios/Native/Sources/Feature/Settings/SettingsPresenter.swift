import Dependencies
import Foundation
import Model
import Observation
import UIKit
import UseCase

@MainActor
@Observable
final class SettingsPresenter {
    @ObservationIgnored
    @Dependency(\.notificationUseCase) private var notificationUseCase

    // Notification settings
    var notificationSettings = NotificationSettings()
    var authorizationStatus: NotificationAuthorizationStatus = .notDetermined
    var isLoading = false

    init() {
        Task {
            await load()
            await checkAuthorizationStatus()
        }
    }

    // MARK: - Public Methods

    func toggleNotifications() async {
        guard !isLoading else { return }

        if !notificationSettings.isEnabled {
            // Request permission when enabling notifications
            let granted = await notificationUseCase.requestPermission()

            if granted {
                await updateSettings(notificationSettings.copy(isEnabled: true))
                await checkAuthorizationStatus()
            } else {
                // Permission denied, keep notifications disabled
                await checkAuthorizationStatus()
            }
        } else {
            // Disable notifications
            await updateSettings(notificationSettings.copy(isEnabled: false))
            await notificationUseCase.cancelAllNotifications()
        }
    }

    func updateReminderTime(_ minutes: Int) async {
        await updateSettings(notificationSettings.copy(reminderMinutes: minutes))
    }

    func toggleCustomSound() async {
        await updateSettings(notificationSettings.copy(useCustomSound: !notificationSettings.useCustomSound))
    }

    func toggleFavoritesOnly() async {
        await updateSettings(notificationSettings.copy(favoritesOnly: !notificationSettings.favoritesOnly))
    }

    func openSystemSettings() {
        guard let settingsURL = URL(string: UIApplication.openSettingsURLString) else {
            return
        }

        if UIApplication.shared.canOpenURL(settingsURL) {
            UIApplication.shared.open(settingsURL)
        }
    }

    // MARK: - Private Methods

    private func load() async {
        isLoading = true
        notificationSettings = await notificationUseCase.load()
        isLoading = false
    }

    private func updateSettings(_ newSettings: NotificationSettings) async {
        notificationSettings = newSettings
        await notificationUseCase.save(newSettings)
    }

    private func checkAuthorizationStatus() async {
        authorizationStatus = await notificationUseCase.checkAuthorizationStatus()
    }
}

extension NotificationSettings {
    fileprivate func copy(
        isEnabled: Bool? = nil,
        reminderMinutes: Int? = nil,
        useCustomSound: Bool? = nil,
        favoritesOnly: Bool? = nil
    ) -> NotificationSettings {
        NotificationSettings(
            isEnabled: isEnabled ?? self.isEnabled,
            reminderMinutes: reminderMinutes ?? self.reminderMinutes,
            useCustomSound: useCustomSound ?? self.useCustomSound,
            favoritesOnly: favoritesOnly ?? self.favoritesOnly
        )
    }
}
