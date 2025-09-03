package io.github.droidkaigi.confsched.navigation.route

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data object AboutTabRoute : MainTabRoute {
    override val rootRouteClass: KClass<*> get() = AboutRoute::class
}

@Serializable
data object AboutRoute

@Serializable
data object SponsorsRoute

@Serializable
data object LicensesRoute

@Serializable
data object SettingsRoute

@Serializable
data object StaffsRoute

@Serializable
data object ContributorsRoute
