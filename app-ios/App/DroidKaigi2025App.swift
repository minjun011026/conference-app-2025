//
//  DroidKaigi2025App.swift
//  DroidKaigi2025
//
//  Created by Ryoya Ito on 2025/06/28.
//

import Root
import SwiftUI

@main
struct DroidKaigi2025App: App {
    init() {
        let nav = UINavigationBarAppearance()
        nav.configureWithTransparentBackground()

        let inlineBase = UIFont(name: "Chango-Regular", size: 20) ?? .systemFont(ofSize: 20, weight: .bold)
        let largeBase  = UIFont(name: "Chango-Regular", size: 34) ?? .systemFont(ofSize: 34, weight: .bold)

        let inlineFont = UIFontMetrics(forTextStyle: .headline).scaledFont(for: inlineBase)
        let largeFont  = UIFontMetrics(forTextStyle: .largeTitle).scaledFont(for: largeBase)

        nav.titleTextAttributes = [
            .font: inlineFont,
        ]
        nav.largeTitleTextAttributes = [
            .font: largeFont,
        ]

        let bar = UINavigationBar.appearance()
        bar.standardAppearance = nav
        bar.compactAppearance = nav
        bar.scrollEdgeAppearance = nav
    }

    var body: some Scene {
        WindowGroup {
            RootScreen()
        }
    }
}
