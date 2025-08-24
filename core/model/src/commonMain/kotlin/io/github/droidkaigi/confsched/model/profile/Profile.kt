package io.github.droidkaigi.confsched.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val name: String,
    val occupation: String,
    val link: String,
    val imagePath: String,
    val image: ByteArray,
    val themeKey: String,
) {
    override fun toString(): String {
        return """
            Profile(
                name='$name',
                occupation='$occupation',
                link='$link',
                imagePath='$imagePath',
                image=${image.contentToString()},
                themeKey='$themeKey'
            )
        """.trimIndent()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Profile

        if (name != other.name) return false
        if (occupation != other.occupation) return false
        if (link != other.link) return false
        if (imagePath != other.imagePath) return false
        if (!image.contentEquals(other.image)) return false
        if (themeKey != other.themeKey) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + occupation.hashCode()
        result = 31 * result + link.hashCode()
        result = 31 * result + imagePath.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + themeKey.hashCode()
        return result
    }
}
