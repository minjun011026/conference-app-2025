package io.github.droidkaigi.confsched.settings.section

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.changoFontFamily
import io.github.droidkaigi.confsched.designsystem.theme.robotoMediumFontFamily
import io.github.droidkaigi.confsched.designsystem.theme.robotoRegularFontFamily
import io.github.droidkaigi.confsched.model.settings.KaigiFontFamily
import io.github.droidkaigi.confsched.settings.SettingsRes
import io.github.droidkaigi.confsched.settings.SettingsUiState
import io.github.droidkaigi.confsched.settings.component.SelectableItemColumn
import io.github.droidkaigi.confsched.settings.component.SettingsItemRow
import io.github.droidkaigi.confsched.settings.ic_brand_family
import io.github.droidkaigi.confsched.settings.section_item_title_font
import io.github.droidkaigi.confsched.settings.section_title_accessibility
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

const val SettingsAccessibilityUseFontFamilySettingsItemRowTestTag = "SettingsAccessibilityUseFontFamilySettingsItemRowTestTag"
const val SettingsAccessibilityUseFontFamilySelectableItemColumnTestTagPrefix =
    "SettingsAccessibilityUseFontFamilySelectableItemColumnTestTagPrefix:"

fun LazyListScope.accessibility(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onSelectUseFontFamily: (KaigiFontFamily) -> Unit,
) {
    item {
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(SettingsRes.string.section_title_accessibility),
            style = MaterialTheme.typography.titleMedium,
        )
    }
    item {
        SettingsItemRow(
            modifier = modifier.testTag(SettingsAccessibilityUseFontFamilySettingsItemRowTestTag),
            leadingIcon = vectorResource(SettingsRes.drawable.ic_brand_family),
            itemName = stringResource(SettingsRes.string.section_item_title_font),
            currentValue = uiState.useKaigiFontFamily.displayName,
            selectableItems = {
                KaigiFontFamily.entries.forEach { fontFamily ->
                    val itemFont = when (fontFamily) {
                        KaigiFontFamily.ChangoRegular -> changoFontFamily()
                        KaigiFontFamily.RobotoRegular -> robotoRegularFontFamily()
                        KaigiFontFamily.RobotoMedium -> robotoMediumFontFamily()
                        KaigiFontFamily.SystemDefault -> FontFamily.Default
                    }
                    SelectableItemColumn(
                        modifier = Modifier.testTag(
                            SettingsAccessibilityUseFontFamilySelectableItemColumnTestTagPrefix
                                .plus(fontFamily.displayName),
                        ),
                        fontFamily = itemFont,
                        currentValue = fontFamily.displayName,
                        onClickItem = {
                            onSelectUseFontFamily(fontFamily)
                        },
                    )
                }
            },
        )
    }

    item {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}
