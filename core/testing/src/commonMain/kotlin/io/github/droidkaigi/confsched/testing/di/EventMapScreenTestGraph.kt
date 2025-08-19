package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import io.github.droidkaigi.confsched.eventmap.EventMapScreenContext
import io.github.droidkaigi.confsched.testing.robot.eventmap.EventMapScreenRobot

interface EventMapScreenTestGraph : EventMapScreenContext.Factory {
    val eventMapScreenRobotProvider: Provider<EventMapScreenRobot>

    @Provides
    fun provideEventMapScreenContext(): EventMapScreenContext {
        return create()
    }
}

fun createEventMapScreenTestGraph(): EventMapScreenTestGraph = createTestAppGraph()
