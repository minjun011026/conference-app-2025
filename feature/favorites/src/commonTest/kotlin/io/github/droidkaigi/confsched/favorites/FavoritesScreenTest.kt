package io.github.droidkaigi.confsched.favorites

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.testing.annotations.ComposeTest
import io.github.droidkaigi.confsched.testing.annotations.RunWith
import io.github.droidkaigi.confsched.testing.annotations.UiTestRunner
import io.github.droidkaigi.confsched.testing.behavior.describeBehaviors
import io.github.droidkaigi.confsched.testing.behavior.execute
import io.github.droidkaigi.confsched.testing.di.createFavoritesScreenTestGraph
import io.github.droidkaigi.confsched.testing.robot.favorites.FavoritesScreenRobot
import io.github.droidkaigi.confsched.testing.robot.sessions.TimetableServerRobot.ServerStatus

@RunWith(UiTestRunner::class)
class FavoritesScreenTest {
    val testAppGraph = createFavoritesScreenTestGraph()

    @ComposeTest
    fun runTest() {
        describedBehaviors.forEach { behavior ->
            val robot = testAppGraph.favoritesScreenRobotProvider()
            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<FavoritesScreenRobot>("FavoritesScreen") {
        describe("when server is operational") {
            doIt {
                setupTimetableServer(ServerStatus.Operational)
                setupFavoritesScreenContent()
            }
            itShould("show loading indicator") {
                captureScreenWithChecks {
                    checkLoadingIndicatorDisplayed()
                }
            }
            describe("with single favorite session") {
                doIt {
                    setupSingleFavoriteSession()
                }
                itShould("display single favorite session") {
                    captureScreenWithChecks {
                        checkTimetableListItemsDisplayed()
                    }
                }
                describe("click first session bookmark") {
                    doIt {
                        // TODO: UserDataStore data cannot be inherited, so definition is also required here
                        setupSingleFavoriteSession()
                        clickFirstSessionBookmark()
                    }
                    itShould("display empty view") {
                        captureScreenWithChecks {
                            checkEmptyViewDisplayed()
                        }
                    }
                }
            }
            describe("with many favorite sessions") {
                doIt {
                    setupFavoriteSessions()
                }
                itShould("display multiple favorite sessions") {
                    captureScreenWithChecks {
                        checkTimetableListItemsDisplayed()
                    }
                }
                describe("click first session bookmark") {
                    doIt {
                        // TODO: UserDataStore data cannot be inherited, so definition is also required here
                        setupFavoriteSessions()
                        clickFirstSessionBookmark()
                    }
                    itShould("display remaining favorite sessions") {
                        captureScreenWithChecks {
                            checkTimetableListItemsDisplayed()
                        }
                    }
                }
                describe("scroll to see more sessions") {
                    doIt {
                        // TODO: UserDataStore data cannot be inherited, so definition is also required here
                        setupFavoriteSessions()
                        scrollFavorites()
                    }
                    itShould("display scrolled content") {
                        captureScreenWithChecks {
                            checkTimetableListFirstItemNotDisplayed()
                        }
                    }
                }
            }
        }
        describe("when server is error") {
            doIt {
                setupTimetableServer(ServerStatus.Error)
                setupFavoritesScreenContent()
            }
            itShould("show loading indicator") {
                captureScreenWithChecks {
                    checkLoadingIndicatorDisplayed()
                }
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
            }
        }
    }
}
