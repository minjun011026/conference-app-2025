package io.github.droidkaigi.confsched.about

import androidx.compose.ui.test.runComposeUiTest
import io.github.droidkaigi.confsched.testing.annotations.ComposeTest
import io.github.droidkaigi.confsched.testing.annotations.RunWith
import io.github.droidkaigi.confsched.testing.annotations.UiTestRunner
import io.github.droidkaigi.confsched.testing.behavior.describeBehaviors
import io.github.droidkaigi.confsched.testing.behavior.execute
import io.github.droidkaigi.confsched.testing.di.createAboutScreenTestGraph
import io.github.droidkaigi.confsched.testing.robot.about.AboutScreenRobot

@RunWith(UiTestRunner::class)
class AboutScreenTest {

    val testAppGraph = createAboutScreenTestGraph()

    @ComposeTest
    fun runTest() {
        describedBehaviors.forEach { behavior ->
            val robot = testAppGraph.aboutScreenRobotProvider()
            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<AboutScreenRobot>("AboutScreen") {
        describe("when launch") {
            doIt {
                setupAboutScreenContent()
            }
            itShould("show header section") {
                captureScreenWithChecks {
                    checkHeaderSectionDisplayed()
                }
            }
            describe("when scroll to credits section") {
                doIt {
                    scrollToCreditsSection()
                }
                itShould("show credits contents") {
                    captureScreenWithChecks {
                        checkCreditsSectionTitleDisplayed()
                        checkCreditsSectionContentsDisplayed()
                    }
                }
            }
            describe("when scroll to others section") {
                doIt {
                    scrollToOthersSection()
                }
                itShould("show others contents") {
                    captureScreenWithChecks {
                        checkOthersSectionTitleDisplayed()
                        checkOthersSectionContentsDisplayed()
                    }
                }
            }
            describe("when scroll to footer section") {
                doIt {
                    scrollToFooterSection()
                }
                itShould("show footer contents") {
                    captureScreenWithChecks {
                        checkFooterSectionDisplayed()
                    }
                }
            }
        }
    }
}
