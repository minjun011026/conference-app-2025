package io.github.confsched.profile.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import io.github.confsched.profile.ProfileScreenContext
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.model.profile.Profile

@Composable
context(screenContext: ProfileScreenContext)
fun ProfileCardScreenRoot(
    profile: Profile,
    onEditClick: () -> Unit,
    onShareClick: (ImageBitmap) -> Unit,
) {
    val eventFlow = rememberEventFlow<ProfileCardScreenEvent>()

    val uiState = profileCardScreenPresenter(
        eventFlow = eventFlow,
        profile = profile,
    )

    ProfileCardScreen(
        uiState = uiState,
        onEditClick = onEditClick,
        onShareClick = { onShareClick(it) },
    )
}
