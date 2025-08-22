import AppIntents

struct ConferenceAppShortcutsProvider: AppShortcutsProvider {
    @AppShortcutsBuilder static var appShortcuts: [AppShortcut] {
        AppShortcut(
            intent: GetSessionsAtDateIntent(),
            phrases: [
                "\(.applicationName)で指定日のセッションを取得"
            ],
            shortTitle: "Get sessions",
            systemImageName: "swift",
        )
        AppShortcut(
            intent: GetOngoingSessionsIntent(),
            phrases: [
                "\(.applicationName)で現在のセッションを取得"
            ],
            shortTitle: "Get ongoing sessions",
            systemImageName: "swift",
        )
    }

    static var shortcutTileColor: ShortcutTileColor { .orange }
}
