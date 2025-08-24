package io.github.confsched.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.profile.ProfileWithImageBitmaps
import io.github.takahirom.rin.rememberRetained
import soil.query.compose.rememberMutation

@Composable
context(screenContext: ProfileScreenContext)
fun profilePresenter(
    eventFlow: EventFlow<ProfileScreenEvent>,
    profileWithImageBitmaps: ProfileWithImageBitmaps,
): ProfileUiState = providePresenterDefaults {
    val isAllowedToShowCard = profileWithImageBitmaps.profile != null
        && profileWithImageBitmaps.profileImageBitmap != null
        && profileWithImageBitmaps.qrImageBitmap != null

    val profileMutation = rememberMutation(screenContext.profileMutationKey)
    var isInEditMode by rememberRetained { mutableStateOf(!isAllowedToShowCard) }

    EventEffect(eventFlow) { event ->
        when (event) {
            is ProfileScreenEvent.EnterEditMode -> {
                isInEditMode = true
            }

            is ProfileScreenEvent.CreateProfile -> {
                profileMutation.mutate(event.profile)
                isInEditMode = false
            }
        }
    }

    if (!isAllowedToShowCard || isInEditMode) return@providePresenterDefaults ProfileUiState.Edit(baseProfile = profileWithImageBitmaps.profile)

    ProfileUiState.Card(
        profile = requireNotNull(profileWithImageBitmaps.profile),
        profileImageBitmap = requireNotNull(profileWithImageBitmaps.profileImageBitmap),
        qrImageBitmap = requireNotNull(profileWithImageBitmaps.qrImageBitmap),
    )
}
