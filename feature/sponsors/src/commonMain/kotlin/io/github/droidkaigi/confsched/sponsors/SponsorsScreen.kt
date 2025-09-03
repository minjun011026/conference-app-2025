package io.github.droidkaigi.confsched.sponsors

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedMediumTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.extension.enableMouseDragScroll
import io.github.droidkaigi.confsched.droidkaigiui.rememberAsyncImagePainter
import io.github.droidkaigi.confsched.model.sponsors.Sponsor
import io.github.droidkaigi.confsched.model.sponsors.SponsorList
import io.github.droidkaigi.confsched.model.sponsors.SponsorPlan
import io.github.droidkaigi.confsched.model.sponsors.fakes
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SponsorsScreen(
    sponsors: PersistentList<Sponsor>,
    onSponsorsItemClick: (url: String) -> Unit,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val gridState = rememberLazyGridState()
    val sponsorList = SponsorList(
        platinumSponsors = sponsors.filter { it.plan == SponsorPlan.PLATINUM },
        goldSponsors = sponsors.filter { it.plan == SponsorPlan.GOLD },
        supporters = sponsors.filter { it.plan == SponsorPlan.SUPPORTER },
    )

    Scaffold(
        topBar = {
            AnimatedMediumTopAppBar(
                title = stringResource(SponsorsRes.string.sponsors_title),
                onBackClick = onBackClick,
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets(),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(6),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 48.dp,
                ),
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .enableMouseDragScroll(gridState),
            ) {
                sponsorsByPlanSection(
                    headerStringResource = SponsorsRes.string.platinum_sponsors_title,
                    sponsors = sponsorList.platinumSponsors,
                    onSponsorsItemClick = onSponsorsItemClick,
                    contentPadding = contentPadding,
                    sponsorItemSpan = { GridItemSpan(maxLineSpan) },
                    sponsorItemHeight = 110.dp,
                )

                sponsorsByPlanSection(
                    headerStringResource = SponsorsRes.string.gold_sponsors_title,
                    sponsors = sponsorList.goldSponsors,
                    onSponsorsItemClick = onSponsorsItemClick,
                    contentPadding = contentPadding,
                    sponsorItemSpan = { GridItemSpan(3) },
                    sponsorItemHeight = 77.dp,
                )

                sponsorsByPlanSection(
                    headerStringResource = SponsorsRes.string.supporters_title,
                    sponsors = sponsorList.supporters,
                    onSponsorsItemClick = onSponsorsItemClick,
                    contentPadding = contentPadding,
                    sponsorItemSpan = { GridItemSpan(2) },
                    sponsorItemHeight = 77.dp,
                    isLastSection = true,
                )
            }
        }
    }
}

private fun LazyGridScope.sponsorsByPlanSection(
    headerStringResource: StringResource,
    sponsors: List<Sponsor>,
    contentPadding: PaddingValues,
    sponsorItemSpan: LazyGridItemSpanScope.() -> GridItemSpan,
    sponsorItemHeight: Dp,
    onSponsorsItemClick: (url: String) -> Unit,
    isLastSection: Boolean = false,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        val headerText = stringResource(headerStringResource)
        Text(
            text = headerText,
            color = MaterialTheme.colorScheme.primaryFixed,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 6.dp),
        )
    }

    if (sponsors.size > 0) {
        items(
            items = sponsors,
            span = { sponsorItemSpan() },
        ) { sponsor ->
            SponsorItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sponsorItemHeight),
                sponsor = sponsor,
                onSponsorsItemClick = onSponsorsItemClick,
            )
        }
    }

    if (isLastSection.not()) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SponsorItem(
    sponsor: Sponsor,
    modifier: Modifier = Modifier,
    onSponsorsItemClick: (url: String) -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = sponsor.logo,
        placeholder = ColorPainter(Color.White),
    )
    Card(
        modifier = modifier.clickable { onSponsorsItemClick(sponsor.link) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Image(
            painter = painter,
            contentDescription = stringResource(
                SponsorsRes.string.content_description_sponsor_logo_format,
                sponsor.name,
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp,
                ),
        )
    }
}

@Preview
@Composable
fun SponsorScreenPreview() {
    KaigiPreviewContainer {
        SponsorsScreen(
            sponsors = Sponsor.fakes(),
            onSponsorsItemClick = {},
            onBackClick = {},
            contentPadding = PaddingValues(0.dp),
        )
    }
}
