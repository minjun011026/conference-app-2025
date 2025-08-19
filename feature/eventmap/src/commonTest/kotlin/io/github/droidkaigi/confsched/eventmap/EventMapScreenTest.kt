package io.github.droidkaigi.confsched.eventmap

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.testing.annotations.ComposeTest
import io.github.droidkaigi.confsched.testing.annotations.RunWith
import io.github.droidkaigi.confsched.testing.annotations.UiTestRunner
import io.github.droidkaigi.confsched.testing.behavior.describeBehaviors
import io.github.droidkaigi.confsched.testing.behavior.execute
import io.github.droidkaigi.confsched.testing.di.createEventMapScreenTestGraph
import io.github.droidkaigi.confsched.testing.robot.eventmap.EventMapScreenRobot
import io.github.droidkaigi.confsched.testing.robot.eventmap.EventMapServerRobot.ServerStatus

@RunWith(UiTestRunner::class)
class EventMapScreenTest {

    val testAppGraph = createEventMapScreenTestGraph()

    @ComposeTest
    fun runTest() {
        describedBehaviors.forEach { behavior ->
            val robot = testAppGraph.eventMapScreenRobotProvider()
            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<EventMapScreenRobot>("EventMapScreen") {
        describe("when regardless of server status") {
            doIt {
                setupEventMapScreenContent()
            }
            describe("when click floor level ground") {
                doIt {
                    clickEventMapTabOnGround()
                }
                itShould("showed ground floor level map") {
                    captureScreenWithChecks {
                        checkEventMapOnGround()
                    }
                }
            }
            describe("when click floor level basement") {
                doIt {
                    clickEventMapTabOnBasement()
                }
                itShould("showed basement floor level map") {
                    captureScreenWithChecks {
                        checkEventMapOnBasement()
                    }
                }
            }
        }
        describe("when server is operational") {
            doIt {
                setupEventMapServer(ServerStatus.Operational)
                setupEventMapScreenContent()
                scrollToJellyfishRoomEvent()
            }
            itShould("ensure that the room types for Jellyfish are displayed.") {
                captureScreenWithChecks(
                    checks = {
                        checkEventMapItemJellyfish()
                    },
                )
            }
            doIt {
                scrollToKoalaRoomEvent()
            }
            itShould("ensure that the room types for Koala are displayed.") {
                captureScreenWithChecks(
                    checks = {
                        checkEventMapItemKoala()
                    },
                )
            }
            doIt {
                scrollToLadybugRoomEvent()
            }
            itShould("ensure that the room types for Ladybug are displayed.") {
                captureScreenWithChecks(
                    checks = {
                        checkEventMapItemLadybug()
                    },
                )
            }
            doIt {
                scrollToMeerkatRoomEvent()
            }
            itShould("ensure that the room types for Meerkat are displayed.") {
                captureScreenWithChecks(
                    checks = {
                        checkEventMapItemMeerkat()
                    },
                )
            }
            doIt {
                scrollToNarwhalRoomEvent()
            }
            itShould("ensure that the room types for Narwhal are displayed.") {
                captureScreenWithChecks(
                    checks = {
                        checkEventMapItemNarwhal()
                    },
                )
            }
        }
        describe("when server is error") {
            doIt {
                setupEventMapServer(ServerStatus.Error)
            }
            describe("when launch") {
                doIt {
                    setupEventMapScreenContent()
                }
                describe("after loading") {
                    doIt {
                        waitFor5Seconds()
                    }
                    itShould("show error message") {
                        captureScreenWithChecks {
                            checkErrorFallbackDisplayed()
                        }
                    }
                    describe("click retry after server gets operational") {
                        doIt {
                            setupEventMapServer(ServerStatus.Operational)
                            clickRetryButton()
                        }
                        describe("after waiting for 5 seconds") {
                            doIt {
                                waitFor5Seconds()
                            }
                            itShould("show event map") {
                                captureScreenWithChecks {
                                    checkEventMapDescriptionDisplayed()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
