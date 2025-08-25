package io.github.droidkaigi.confsched.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val nickName: String = "",
    val occupation: String = "",
    val theme: ProfileCardTheme = ProfileCardTheme.DarkPill,
    // stored for editing in the soil form
    val link: String = "",
    val imagePath: String = "",
    // Actual image data used for display or sharing
    val imageByteArray: ByteArray = ByteArray(0),
    val qrCodeByteArray: ByteArray = ByteArray(0),
) {
    val isValid: Boolean
        get() = nickName.isNotEmpty()
            && occupation.isNotEmpty()
            && link.isNotEmpty()
            && imageByteArray.isNotEmpty()
            && qrCodeByteArray.isNotEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Profile

        if (nickName != other.nickName) return false
        if (occupation != other.occupation) return false
        if (theme != other.theme) return false
        if (link != other.link) return false
        if (imagePath != other.imagePath) return false
        if (!imageByteArray.contentEquals(other.imageByteArray)) return false
        if (!qrCodeByteArray.contentEquals(other.qrCodeByteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nickName.hashCode()
        result = 31 * result + occupation.hashCode()
        result = 31 * result + theme.hashCode()
        result = 31 * result + link.hashCode()
        result = 31 * result + imagePath.hashCode()
        result = 31 * result + imageByteArray.contentHashCode()
        result = 31 * result + qrCodeByteArray.contentHashCode()
        return result
    }
}
