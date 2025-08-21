package io.github.droidkaigi.confsched.testing.robot.search

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.sessions.SearchScreenContext
import io.github.droidkaigi.confsched.sessions.SearchScreenRoot
import io.github.droidkaigi.confsched.testing.compose.TestDefaultsProvider
import io.github.droidkaigi.confsched.testing.robot.core.CaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultCaptureScreenRobot
import io.github.droidkaigi.confsched.testing.robot.core.DefaultWaitRobot
import io.github.droidkaigi.confsched.testing.robot.core.WaitRobot
import io.github.droidkaigi.confsched.testing.robot.sessions.DefaultTimetableServerRobot
import io.github.droidkaigi.confsched.testing.robot.sessions.TimetableServerRobot
import kotlinx.coroutines.test.TestDispatcher

private const val DemoSearchWord = "Demo"

@Inject
class SearchScreenRobot(
    private val screenContext: SearchScreenContext,
    private val testDispatcher: TestDispatcher,
    timetableServerRobot: DefaultTimetableServerRobot,
    captureScreenRobot: DefaultCaptureScreenRobot,
    waitRobot: DefaultWaitRobot,
) : TimetableServerRobot by timetableServerRobot,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot {

    enum class ConferenceDay(
        val day: Int,
        val dateText: String,
    ) {
        Day1(1, "9/12"),
        Day2(2, "9/13"),
    }

    enum class Category(
        val categoryName: String,
    ) {
        AppArchitecture("App Architecture en"),
        JetpackCompose("Jetpack Compose en"),
        Other("Other en"),
    }

    context(composeUiTest: ComposeUiTest)
    fun setupTimetableScreenContent() {
        composeUiTest.setContent {
            with(screenContext) {
                TestDefaultsProvider(testDispatcher) {
                    SearchScreenRoot(
                        onTimetableItemClick = {},
                        onBackClick = {},
                    )
                }
            }
        }
    }

    context(composeUiTest: ComposeUiTest)
    fun inputDemoSearchWord() {
        inputSearchWord(DemoSearchWord)
    }

    context(composeUiTest: ComposeUiTest)
    private fun inputSearchWord(text: String) {
        composeTestRule
            .onNodeWithTag(SearchTextFieldAppBarTextFieldTestTag)
            .performTextInput(text)
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun scrollToFilterLanguageChip() {
        composeTestRule
            .onNode(hasTestTag(SearchFiltersLazyRowTestTag))
            .performScrollToNode(hasTestTag(SearchFiltersFilterLanguageChipTestTag))
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickFilterDayChip() {
        composeTestRule
            .onNode(hasTestTag(SearchFiltersFilterDayChipTestTag))
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickFilterCategoryChip() {
        composeTestRule
            .onNode(hasTestTag(SearchFiltersFilterCategoryChipTestTag))
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickFilterLanguageChip() {
        composeTestRule
            .onNode(hasTestTag(SearchFiltersFilterLanguageChipTestTag))
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickConferenceDay(
        clickDay: ConferenceDay,
    ) {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .filter(matcher = hasText(clickDay.dateText))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickCategory(
        category: Category,
    ) {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .filter(matcher = hasText(category.categoryName))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun clickLanguage(
        language: Language,
    ) {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .filter(matcher = hasText(language.tagName))
            .onFirst()
            .performClick()
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDisplayedFilterDayChip() {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .assertCountAtLeast(ConferenceDay.entries.size)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDisplayedFilterCategoryChip() {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .assertCountAtLeast(Category.entries.size)
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDisplayedFilterLanguageChip() {
        composeTestRule
            .onAllNodes(
                hasTestTag(
                    testTag = DropdownFilterChipTestTagPrefix,
                    substring = true,
                ),
            )
            .assertCountAtLeast(Language.entries.size)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemByConferenceDay(
        checkDay: ConferenceDay,
    ) {
        val (contain, doesNotContain) = if (checkDay == ConferenceDay.Day1) {
            listOf(ConferenceDay.Day1, ConferenceDay.Day2)
        } else {
            listOf(ConferenceDay.Day2, ConferenceDay.Day1)
        }

        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertTextContains("Demo Welcome Talk ${contain.day}")
            .assertTextDoesNotContain("Demo Welcome Talk ${doesNotContain.day}")
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemByCategory(
        category: Category,
    ) {
        val containText = if (category == Category.Other) {
            "Demo Welcome Talk"
        } else {
            category.categoryName
        }

        composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertTextContains(
                value = containText,
                substring = true,
            )
        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkDemoSearchWordDisplayed() {
        checkSearchWordDisplayed(DemoSearchWord)
    }

    context(composeUiTest: ComposeUiTest)
    private fun checkSearchWordDisplayed(text: String) {
        composeTestRule
            .onNodeWithTag(SearchTextFieldAppBarTextFieldTestTag)
            .assertTextEquals(text)
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemsHasDemoText() {
        checkTimetableListItemsHasText(DemoSearchWord)
    }

    context(composeUiTest: ComposeUiTest)
    private fun checkTimetableListItemsHasText(searchWord: String) {
        composeTestRule
            .onAllNodesWithTag(TimetableItemCardTitleTextTestTag)
            .assertAll(hasText(text = searchWord, ignoreCase = true))
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListExists() {
        composeTestRule
            .onNode(hasTestTag(TimetableListTestTag))
            .assertExists()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListDisplayed() {
        composeTestRule
            .onNode(hasTestTag(TimetableListTestTag))
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemsDisplayed() {
        composeTestRule
            .onAllNodesWithTag(TimetableItemCardTestTag)
            .onFirst()
            .assertIsDisplayed()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkTimetableListItemsNotDisplayed() {
        composeTestRule
            .onAllNodesWithTag(TimetableItemCardTestTag)
            .onFirst()
            .assertIsNotDisplayed()
    }
}
