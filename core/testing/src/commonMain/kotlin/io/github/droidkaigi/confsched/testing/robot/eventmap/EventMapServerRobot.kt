package io.github.droidkaigi.confsched.testing.robot.eventmap

import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.eventmap.FakeEventMapApiClient

interface EventMapServerRobot {
    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupEventMapServer(serverStatus: ServerStatus)
}

@Inject
class DefaultEventMapServerRobot(
    private val fakeEventMapApiClient: FakeEventMapApiClient,
) : EventMapServerRobot {
    override fun setupEventMapServer(
        serverStatus: EventMapServerRobot.ServerStatus,
    ) {
        fakeEventMapApiClient.setup(
            when (serverStatus) {
                EventMapServerRobot.ServerStatus.Operational -> FakeEventMapApiClient.Status.Operational
                EventMapServerRobot.ServerStatus.Error -> FakeEventMapApiClient.Status.Error
            },
        )
    }
}
