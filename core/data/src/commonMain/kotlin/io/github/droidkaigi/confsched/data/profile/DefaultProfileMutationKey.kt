package io.github.droidkaigi.confsched.data.profile

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.model.profile.ProfileMutationKey
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import qrcode.QRCode
import soil.query.MutationId
import soil.query.buildMutationKey

@ContributesBinding(DataScope::class)
@Inject
public class DefaultProfileMutationKey(
    private val dataStore: ProfileDataStore,
) : ProfileMutationKey by buildMutationKey(
    id = MutationId("profile_mutation_key"),
    mutate = {
        val qrImageByteArray = QRCode.ofSquares()
            .build(it.link)
            .renderToBytes()

        val profileImageByteArray = PlatformFile(it.imagePath).readBytes()

        dataStore.saveProfile(
            it.copy(
                imageByteArray = profileImageByteArray,
                qrCodeByteArray = qrImageByteArray,
            )
        )
    },
)
