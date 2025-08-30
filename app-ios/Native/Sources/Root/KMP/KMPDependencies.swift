import Dependencies

enum KMPDependencies {
    static func prepareKMPDependencies(_ dependencyValues: inout DependencyValues) {
        let timetableUseCaseImpl = TimetableUseCaseImpl()
        dependencyValues.timetableUseCase = .init(
            load: timetableUseCaseImpl.load,
            toggleFavorite: timetableUseCaseImpl.toggleFavorite
        )

        let sponsorsUseCaseImpl = SponsorsUseCaseImpl()
        dependencyValues.sponsorsUseCase = .init(
            load: sponsorsUseCaseImpl.load
        )

        let staffUseCaseImpl = StaffUseCaseImpl()
        dependencyValues.staffUseCase = .init(
            load: staffUseCaseImpl.load
        )

        let contributorsUseCaseImpl = ContributorsUseCaseImpl()
        dependencyValues.contributorsUseCase = .init(
            load: contributorsUseCaseImpl.load
        )

        let eventMapUseCaseImpl = EventMapUseCaseImpl()
        dependencyValues.eventMapUseCase = .init(
            load: eventMapUseCaseImpl.load
        )

        let profileUseCaseImpl = ProfileUseCaseImpl()
        dependencyValues.profileUseCase = .init(
            load: profileUseCaseImpl.load,
            save: profileUseCaseImpl.save
        )

        let notificationUseCaseImpl = NotificationUseCaseImpl()
        dependencyValues.notificationUseCase = .init(
            load: notificationUseCaseImpl.load,
            save: notificationUseCaseImpl.save,
            requestPermission: notificationUseCaseImpl.requestPermission,
            checkAuthorizationStatus: notificationUseCaseImpl.checkAuthorizationStatus,
            scheduleNotification: notificationUseCaseImpl.scheduleNotification,
            cancelNotification: notificationUseCaseImpl.cancelNotification,
            rescheduleAllNotifications: notificationUseCaseImpl.rescheduleAllNotifications,
            cancelAllNotifications: notificationUseCaseImpl.cancelAllNotifications
        )
    }
}
