package io.github.droidkaigi.confsched.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.github.droidkaigi.confsched.AppGraph
import io.github.droidkaigi.confsched.about.AboutScreenRoot
import io.github.droidkaigi.confsched.about.LicensesScreenRoot
import io.github.droidkaigi.confsched.about.rememberAboutScreenContextRetained
import io.github.droidkaigi.confsched.about.rememberLicensesScreenContextRetained
import io.github.droidkaigi.confsched.model.about.AboutItem
import io.github.droidkaigi.confsched.navigation.route.AboutRoute
import io.github.droidkaigi.confsched.navigation.route.AboutTabRoute
import io.github.droidkaigi.confsched.navigation.route.LicensesRoute
import io.github.droidkaigi.confsched.navigation.route.SettingsRoute
import io.github.droidkaigi.confsched.settings.SettingsScreenRoot
import io.github.droidkaigi.confsched.settings.rememberSettingsScreenContextRetained

context(appGraph: AppGraph)
fun NavGraphBuilder.aboutTabNavGraph(
    onAboutItemClick: (AboutItem) -> Unit,
    onBackClick: () -> Unit,
) {
    navigation<AboutTabRoute>(
        startDestination = AboutRoute,
    ) {
        aboutNavGraph(onAboutItemClick = onAboutItemClick)
        licensesNavGraph(onBackClick = onBackClick)
        settingsNavGraph(onBackClick = onBackClick)
    }
}

context(appGraph: AppGraph)
fun NavGraphBuilder.aboutNavGraph(
    onAboutItemClick: (AboutItem) -> Unit,
) {
    composable<AboutRoute> {
        with(rememberAboutScreenContextRetained()) {
            AboutScreenRoot(
                onAboutItemClick = onAboutItemClick,
            )
        }
    }
}

context(appGraph: AppGraph)
fun NavGraphBuilder.licensesNavGraph(
    onBackClick: () -> Unit,
) {
    composable<LicensesRoute> {
        with(rememberLicensesScreenContextRetained()) {
            LicensesScreenRoot(
                onBackClick = onBackClick,
            )
        }
    }
}

context(appGraph: AppGraph)
fun NavGraphBuilder.settingsNavGraph(
    onBackClick: () -> Unit,
) {
    composable<SettingsRoute> {
        with(rememberSettingsScreenContextRetained()) {
            SettingsScreenRoot(
                onBackClick = onBackClick,
            )
        }
    }
}
