package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import io.github.droidkaigi.confsched.about.AboutScreenContext
import io.github.droidkaigi.confsched.testing.robot.about.AboutScreenRobot

interface AboutScreenTestGraph : AboutScreenContext.Factory {
    val aboutScreenRobotProvider: Provider<AboutScreenRobot>

    @Provides
    fun provideAboutScreenContext(): AboutScreenContext {
        return createAboutScreenContext()
    }
}

fun createAboutScreenTestGraph(): AboutScreenTestGraph = createTestAppGraph()
