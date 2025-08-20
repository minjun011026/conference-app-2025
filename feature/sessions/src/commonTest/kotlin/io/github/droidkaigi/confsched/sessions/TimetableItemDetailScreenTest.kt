package io.github.droidkaigi.confsched.sessions

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.testing.annotations.ComposeTest
import io.github.droidkaigi.confsched.testing.annotations.RunWith
import io.github.droidkaigi.confsched.testing.annotations.UiTestRunner
import io.github.droidkaigi.confsched.testing.behavior.describeBehaviors
import io.github.droidkaigi.confsched.testing.behavior.execute
import io.github.droidkaigi.confsched.testing.di.createTimetableItemDetailScreenTestGraph
import io.github.droidkaigi.confsched.testing.robot.sessions.TimetableItemDetailScreenRobot
import io.github.droidkaigi.confsched.testing.robot.sessions.TimetableServerRobot.ServerStatus

@RunWith(UiTestRunner::class)
class TimetableItemDetailScreenTest {
    val testAppGraph = createTimetableItemDetailScreenTestGraph()

    @ComposeTest
    fun runTest() {
        describedBehaviors.forEach { behavior ->
            val robot = testAppGraph.timetableItemDetailScreenRobotProvider()
            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<TimetableItemDetailScreenRobot>("TimetableItemDetailScreen") {
        describe("when server is operational") {
            doIt {
                setupTimetableServer(ServerStatus.Operational)
            }
            describe("when launch") {
                doIt {
                    setupTimetableItemDetailScreenContent()
                }
                itShould("show session detail title") {
                    captureScreenWithChecks(
                        checks = {
                            checkSessionDetailTitle()
                        },
                    )
                }
                // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//                describe("click bookmark") {
//                    doIt {
//                        clickBookmarkButton()
//                    }
//                    itShould("show bookmarked session") {
//                        captureScreenWithChecks(
//                            checks = {
//                                checkBookmarkedSession()
//                            },
//                        )
//                    }
//                    describe("click bookmark again") {
//                        doIt {
//                            clickBookmarkButton()
//                        }
//                        itShould("show unBookmarked session") {
//                            waitFor5Seconds()
//                            captureScreenWithChecks(
//                                checks = {
//                                    checkUnBookmarkSession()
//                                },
//                            )
//                        }
//                    }
//                }
                describe("scroll") {
                    doIt {
                        scroll()
                    }
                    itShould("show scrolled session detail") {
                        captureScreenWithChecks(
                            checks = {
                                checkTargetAudience()
                            },
                        )
                    }
                }
                // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//                describe("when scroll to bottom") {
//                    doIt {
//                        scrollToTargetAudienceSectionBottom()
//                    }
//                    itShould("not display both assets") {
//                        captureScreenWithChecks {
//                            checkAssetSectionDoesNotDisplayed()
//                        }
//                    }
//                }
            }
            describe("when the description is lengthy") {
                doIt {
                    setupTimetableItemDetailScreenContentWithLongDescription()
                    scrollToReadMoreButton()
                    waitUntilIdle()
                }
                itShould("display a more button") {
                    captureScreenWithChecks {
                        checkDisplayingMoreButton()
                    }
                }
            }
            describe("when font scale is small") {
                doIt {
                    setFontScale(0.5f)
                    setupTimetableItemDetailScreenContent()
                }
                itShould("show small font session detail") {
                    captureScreenWithChecks(
                        checks = {
                            checkSummaryCardTexts()
                        },
                    )
                }
            }
            describe("when font scale is large") {
                doIt {
                    setFontScale(1.5f)
                    setupTimetableItemDetailScreenContent()
                    scrollLazyColumnByIndex(1)
                }
                itShould("show large font session detail") {
                    captureScreenWithChecks(
                        checks = {
                            checkSummaryCardTexts()
                        },
                    )
                }
            }
            describe("when font scale is huge") {
                doIt {
                    setFontScale(2.0f)
                    setupTimetableItemDetailScreenContent()
                    scrollLazyColumnByIndex(1)
                }
                itShould("show huge font session detail") {
                    captureScreenWithChecks(
                        checks = {
                            checkSummaryCardTexts()
                        },
                    )
                }
            }
        }
        // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//        describe("when server is operational available both asset") {
//            doIt {
//                setupTimetableServer(ServerStatus.OperationalBothAssetAvailable)
//            }
//            describe("when launch") {
//                doIt {
//                    setupTimetableItemDetailScreenContent()
//                    scrollToAssetSection()
//                }
//                itShould("display both assets") {
//                    captureScreenWithChecks {
//                        checkBothAssetButtonDisplayed()
//                    }
//                }
//            }
//        }
//        describe("when server is operational available only slide asset") {
//            doIt {
//                setupTimetableServer(ServerStatus.OperationalOnlySlideAssetAvailable)
//            }
//            describe("when launch") {
//                doIt {
//                    setupTimetableItemDetailScreenContent()
//                    scrollToAssetSection()
//                }
//                itShould("display only slide assets") {
//                    captureScreenWithChecks {
//                        checkOnlySlideAssetButtonDisplayed()
//                    }
//                }
//            }
//        }
//        describe("when server is operational available only video asset") {
//            doIt {
//                setupTimetableServer(ServerStatus.OperationalOnlyVideoAssetAvailable)
//            }
//            describe("when launch") {
//                doIt {
//                    setupTimetableItemDetailScreenContent()
//                    scrollToAssetSection()
//                }
//                itShould("only display video assets") {
//                    captureScreenWithChecks {
//                        checkOnlyVideoAssetButtonDisplayed()
//                    }
//                }
//            }
//        }
        describe("when server is operational exists message") {
            doIt {
                setupTimetableServer(ServerStatus.OperationalMessageExists)
            }
            describe("when launch") {
                doIt {
                    setupTimetableItemDetailScreenContent()
                    scrollToMessageRow()
                }
                itShould("display message") {
                    captureScreenWithChecks {
                        checkMessageDisplayed()
                    }
                }
            }
        }
    }
}
