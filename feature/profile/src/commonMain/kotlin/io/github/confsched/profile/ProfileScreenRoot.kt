package io.github.confsched.profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilFallbackDefaults
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.profile_card_title
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberSubscription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
context(screenContext: ProfileScreenContext)
fun ProfileScreenRoot(
    onShareClick: (ImageBitmap) -> Unit,
) {
    SoilDataBoundary(
        state = rememberSubscription(screenContext.profileSubscriptionKey),
        fallback = SoilFallbackDefaults.appBar(stringResource(ProfileRes.string.profile_card_title)),
    ) { profileWithImageBitmaps ->
        val eventFlow = rememberEventFlow<ProfileScreenEvent>()

        val uiState = profilePresenter(
            eventFlow = eventFlow,
            profileWithImageBitmaps = profileWithImageBitmaps,
        )

        when (uiState) {
            is ProfileUiState.Card -> {
                ProfileCardScreen(
                    uiState = uiState,
                    onEditClick = { eventFlow.tryEmit(ProfileScreenEvent.EnterEditMode) },
                    onShareClick = onShareClick,
                )
            }

            is ProfileUiState.Edit -> {
                ProfileEditScreen(
                    initialProfile = uiState.baseProfile,
                    onCreateClick = { eventFlow.tryEmit(ProfileScreenEvent.CreateProfile(it)) },
                )
            }
        }
    }
}
