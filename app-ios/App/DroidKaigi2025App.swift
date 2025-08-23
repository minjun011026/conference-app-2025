//
//  DroidKaigi2025App.swift
//  DroidKaigi2025
//
//  Created by Ryoya Ito on 2025/06/28.
//

import Root
import SwiftUI
import Theme

@main
struct DroidKaigi2025App: App {
    init() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithTransparentBackground()

        let inlineTitleBase = UIFont(name: changoFontName, size: 20) ?? .systemFont(ofSize: 20)
        let titleBase  = UIFont(name: changoFontName, size: 28) ?? .systemFont(ofSize: 28)

        let inlineTitleFont = UIFontMetrics(forTextStyle: .headline).scaledFont(for: inlineTitleBase)
        let titleFont  = UIFontMetrics(forTextStyle: .headline).scaledFont(for: titleBase)

        appearance.titleTextAttributes = [
            .font: inlineTitleFont,
        ]
        appearance.largeTitleTextAttributes = [
            .font: titleFont,
        ]

        let bar = UINavigationBar.appearance()
        bar.standardAppearance = appearance
        bar.compactAppearance = appearance
        bar.scrollEdgeAppearance = appearance
    }

    var body: some Scene {
        WindowGroup {
            RootScreen()
        }
    }
}
