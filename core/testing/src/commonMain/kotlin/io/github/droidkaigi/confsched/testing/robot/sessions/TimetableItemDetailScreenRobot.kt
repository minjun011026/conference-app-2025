package io.github.droidkaigi.confsched.testing.robot.sessions

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenContext
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenLazyColumnTestTag
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenRoot
import io.github.droidkaigi.confsched.sessions.components.DescriptionMoreButtonTestTag
import io.github.droidkaigi.confsched.sessions.components.SummaryCardTextTag
import io.github.droidkaigi.confsched.sessions.components.TargetAudienceSectionTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailContentTargetAudienceSectionBottomTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailHeadlineTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailMessageRowTestTag
import io.github.droidkaigi.confsched.sessions.components.TimetableItemDetailMessageRowTextTestTag
import io.github.droidkaigi.confsched.testing.compose.TestDefaultsProvider
import io.github.droidkaigi.confsched.testing.robot.core.CaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultCaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultFontScaleRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.robot.core.FontScaleRobot
import io.github.droidkaigi.confsched.testing.robot.core.WaitRobot
import kotlinx.coroutines.test.TestDispatcher

@Inject
class TimetableItemDetailScreenRobot(
    private val contextFactory: TimetableItemDetailScreenContext.Factory,
    private val testDispatcher: TestDispatcher,
    timetableServerRobot: DefaultTimetableServerRobot,
    captureScreenRobot: DefaultCaptureScreenRobot,
    waitRobot: DefaultWaitRobot,
    fontScaleRobot: DefaultFontScaleRobot,
) : TimetableServerRobot by timetableServerRobot,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot,
    FontScaleRobot by fontScaleRobot {

    context(composeUiTest: ComposeUiTest)
    fun setupTimetableItemDetailScreenContent(
        timetableItemId: TimetableItemId = TimetableItemId(FakeSessionsApiClient.defaultSessionId),
    ) {
        val screenContext = with(contextFactory) {
            createTimetableDetailScreenContext(timetableItemId)
        }
        composeUiTest.setContent {
            with(screenContext) {
                TestDefaultsProvider(testDispatcher) {
                    TimetableItemDetailScreenRoot(
                        onBackClick = {},
                        onAddCalendarClick = {},
                        onShareClick = {},
                        onLinkClick = {},
                    )
                }
            }
        }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun setupTimetableItemDetailScreenContentWithLongDescription() = setupTimetableItemDetailScreenContent(TimetableItemId(FakeSessionsApiClient.defaultSessionIdWithLongDescription))

    // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//    context(composeUiTest: ComposeUiTest)
//    suspend fun clickBookmarkButton() {
//        composeUiTest
//            .onNodeWithTag(TimetableItemDetailBookmarkIconTestTag)
//            .performClick()
//        waitUntilIdle()
//    }

    context(composeUiTest: ComposeUiTest)
    fun scroll() {
        composeUiTest
            .onRoot()
            .performTouchInput {
                swipeUp(
                    startY = visibleSize.height * 3F / 4,
                    endY = visibleSize.height / 4F,
                )
            }
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollLazyColumnByIndex(
        index: Int,
    ) {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
            .performScrollToIndex(index)
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToReadMoreButton() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
            .performScrollToNode(hasTestTag(DescriptionMoreButtonTestTag))
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToTargetAudienceSectionBottom() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
            .performScrollToNode(
                hasTestTag(
                    TimetableItemDetailContentTargetAudienceSectionBottomTestTag,
                ),
            )
    }

    // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//    context(composeUiTest: ComposeUiTest)
//    fun scrollToAssetSection() {
//        composeUiTest
//            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
//            .performScrollToNode(hasTestTag(TimetableItemDetailContentArchiveSectionTestTag))
//    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToMessageRow() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailScreenLazyColumnTestTag)
            .performScrollToNode(hasTestTag(TimetableItemDetailMessageRowTestTag))

        // FIXME Without this, you won't be able to scroll to the exact middle of the message section.
        composeUiTest.onRoot().performTouchInput {
            swipeUp(startY = centerY, endY = centerY - 100)
        }
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkSessionDetailTitle() {
        composeUiTest
            .onNodeWithTag(TimetableItemDetailHeadlineTestTag)
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals(FakeSessionsApiClient.defaultSession.title.ja)
    }

    // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//    context(composeUiTest: ComposeUiTest)
//    fun checkBookmarkedSession() {
//        composeUiTest
//            .onNodeWithTag(TimetableItemDetailBookmarkIconTestTag)
//            .assertContentDescriptionEquals("Bookmarked")
//    }
//
//    context(composeUiTest: ComposeUiTest)
//    fun checkUnBookmarkSession() {
//        composeUiTest
//            .onNodeWithTag(TimetableItemDetailBookmarkIconTestTag)
//            .assertContentDescriptionEquals("Not Bookmarked")
//    }

    context(composeUiTest: ComposeUiTest)
    fun checkTargetAudience() {
        composeUiTest
            .onNodeWithTag(TargetAudienceSectionTestTag)
            .onChildren()
            .onFirst()
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("Target Audience")
    }

    context(composeUiTest: ComposeUiTest)
    fun checkSummaryCardTexts() {
        val titles = listOf(
            "Date/Time",
            "Location",
            "Supported Languages",
            "Category",
        )
        titles.forEach { title ->
            composeUiTest
                .onNodeWithTag(SummaryCardTextTag.plus(title))
                .assertExists()
                .assertIsDisplayed()
                .assertTextContains(
                    value = title,
                    substring = true,
                )
        }
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDisplayingMoreButton() {
        composeUiTest
            .onNodeWithTag(DescriptionMoreButtonTestTag)
            .assertExists()
            .assertIsDisplayed()
    }

    // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//    context(composeUiTest: ComposeUiTest)
//    fun checkBothAssetButtonDisplayed() {
//        checkSlideAssetButtonDisplayed()
//        checkVideoAssetButtonDisplayed()
//    }

    // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//    context(composeUiTest: ComposeUiTest)
//    fun checkAssetSectionDoesNotDisplayed() {
//        composeUiTest
//            .onAllNodesWithTag(TimetableItemDetailContentArchiveSectionTestTag)
//            .onFirst()
//            .assertIsNotDisplayed()
//    }

    // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//    context(composeUiTest: ComposeUiTest)
//    fun checkOnlySlideAssetButtonDisplayed() {
//        checkSlideAssetButtonDisplayed()
//        checkVideoAssetButtonDoesNotDisplayed()
//    }
//
//    context(composeUiTest: ComposeUiTest)
//    fun checkOnlyVideoAssetButtonDisplayed() {
//        checkSlideAssetButtonDoesNotDisplayed()
//        checkVideoAssetButtonDisplayed()
//    }

    // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//    context(composeUiTest: ComposeUiTest)
//    private fun checkSlideAssetButtonDisplayed() {
//        composeUiTest
//            .onAllNodesWithTag(TimetableItemDetailContentArchiveSectionSlideButtonTestTag)
//            .onFirst()
//            .assertExists()
//            .assertIsDisplayed()
//            .assertHasClickAction()
//    }
//
//    context(composeUiTest: ComposeUiTest)
//    private fun checkVideoAssetButtonDisplayed() {
//        composeUiTest
//            .onAllNodesWithTag(TimetableItemDetailContentArchiveSectionVideoButtonTestTag)
//            .onFirst()
//            .assertExists()
//            .assertIsDisplayed()
//            .assertHasClickAction()
//    }

    // TODO https://github.com/DroidKaigi/conference-app-2025/issues/218
//    context(composeUiTest: ComposeUiTest)
//    private fun checkSlideAssetButtonDoesNotDisplayed() {
//        composeUiTest
//            .onAllNodesWithTag(TimetableItemDetailContentArchiveSectionSlideButtonTestTag)
//            .onFirst()
//            .assertIsNotDisplayed()
//            .assertDoesNotExist()
//    }
//
//    context(composeUiTest: ComposeUiTest)
//    private fun checkVideoAssetButtonDoesNotDisplayed() {
//        composeUiTest
//            .onAllNodesWithTag(TimetableItemDetailContentArchiveSectionVideoButtonTestTag)
//            .onFirst()
//            .assertIsNotDisplayed()
//            .assertDoesNotExist()
//    }

    context(composeUiTest: ComposeUiTest)
    fun checkMessageDisplayed() {
        composeUiTest
            .onAllNodesWithTag(TimetableItemDetailMessageRowTextTestTag)
            .onFirst()
            .assertExists()
            .assertIsDisplayed()
            .assertTextEquals("This session has been canceled.")
    }
}
