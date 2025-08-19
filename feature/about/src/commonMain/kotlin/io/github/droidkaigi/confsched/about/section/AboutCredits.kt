package io.github.droidkaigi.confsched.about.section

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Diversity1
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.components.AboutContentColumn
import io.github.droidkaigi.confsched.about.contributor
import io.github.droidkaigi.confsched.about.credits_title
import io.github.droidkaigi.confsched.about.sponsor
import io.github.droidkaigi.confsched.about.staff
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val AboutCreditsTitleTestTag = "AboutCreditsTitleTestTag"
const val AboutCreditsStaffItemTestTag = "AboutCreditsStaffItemTestTag"
const val AboutCreditsContributorsItemTestTag = "AboutCreditsContributorsItemTestTag"
const val AboutCreditsSponsorsItemTestTag = "AboutCreditsSponsorsItemTestTag"

fun LazyListScope.aboutCredits(
    onStaffItemClick: () -> Unit,
    onContributorsItemClick: () -> Unit,
    onSponsorsItemClick: () -> Unit,
) {
    item {
        Text(
            text = stringResource(AboutRes.string.credits_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .testTag(AboutCreditsTitleTestTag)
                .padding(
                    start = 16.dp,
                    top = 32.dp,
                    end = 16.dp,
                ),
        )
    }
    item {
        AboutContentColumn(
            leadingIcon = Outlined.Diversity1,
            label = stringResource(AboutRes.string.contributor),
            testTag = AboutCreditsContributorsItemTestTag,
            onClickAction = onContributorsItemClick,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                ),
        )
    }
    item {
        AboutContentColumn(
            leadingIcon = Outlined.SentimentVerySatisfied,
            label = stringResource(AboutRes.string.staff),
            testTag = AboutCreditsStaffItemTestTag,
            onClickAction = onStaffItemClick,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                ),
        )
    }
    item {
        AboutContentColumn(
            leadingIcon = Outlined.Apartment,
            label = stringResource(AboutRes.string.sponsor),
            testTag = AboutCreditsSponsorsItemTestTag,
            onClickAction = onSponsorsItemClick,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                ),
        )
    }
}

@Preview
@Composable
private fun AboutCreditsPreview() {
    KaigiPreviewContainer {
        LazyColumn {
            aboutCredits(
                onStaffItemClick = {},
                onContributorsItemClick = {},
                onSponsorsItemClick = {},
            )
        }
    }
}
