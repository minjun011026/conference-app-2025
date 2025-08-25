package io.github.confsched.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.profile.Profile
import io.github.takahirom.rin.rememberRetained
import soil.query.compose.rememberMutation

@Composable
context(screenContext: ProfileScreenContext)
fun profilePresenter(
    eventFlow: EventFlow<ProfileScreenEvent>,
    profile: Profile?,
): ProfileUiState = providePresenterDefaults {
    val isAllowedToShowCard = profile != null && profile.isValid

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

    if (!isAllowedToShowCard || isInEditMode) {
        ProfileUiState.Edit(profile = profile)
    } else {
        ProfileUiState.Card(profile = profile)
    }
}
