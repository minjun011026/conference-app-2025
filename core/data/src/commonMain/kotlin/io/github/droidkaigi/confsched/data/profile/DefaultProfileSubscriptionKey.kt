package io.github.droidkaigi.confsched.data.profile

import androidx.compose.ui.graphics.decodeToImageBitmap
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.model.profile.ProfileSubscriptionKey
import io.github.droidkaigi.confsched.model.profile.ProfileWithImageBitmaps
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.flow.map
import qrcode.QRCode
import soil.query.SubscriptionId
import soil.query.buildSubscriptionKey

@ContributesBinding(DataScope::class)
@Inject
public class DefaultProfileSubscriptionKey(
    private val dataStore: ProfileDataStore,
) : ProfileSubscriptionKey by buildSubscriptionKey(
    id = SubscriptionId("profile"),
    subscribe = {
        dataStore.getProfileOrNull().map { profile ->
            if (profile == null) return@map ProfileWithImageBitmaps()

            if (profile.imagePath.isEmpty() || profile.link.isEmpty()) return@map ProfileWithImageBitmaps(profile)

            val qrImageBitmap = QRCode.ofSquares()
                .build(profile.link)
                .renderToBytes()
                .decodeToImageBitmap()

            val profileImageBitmap = PlatformFile(profile.imagePath)
                .readBytes()
                .decodeToImageBitmap()

            ProfileWithImageBitmaps(
                profile = profile,
                profileImageBitmap = profileImageBitmap,
                qrImageBitmap = qrImageBitmap,
            )
        }
    },
)
