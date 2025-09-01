package io.github.droidkaigi.confsched.testing.robot.core

/**
 * Kotlin enum transcription of Android's `Sensor.java` TYPE_XXX / STRING_TYPE_XXX constants
 *
 * Notes:
 * - Integer IDs (`id`) and string types (`stringType`) mirror AOSP `android.hardware.Sensor`.
 * - Deprecated items are kept to match the original API surface.
 * - For now, only a subset of sensors actually in use are defined.
 */
@Suppress("DEPRECATION")
enum class SensorType(
    /** Integer ID corresponding to Java's TYPE_XXX. */
    val id: Int,
    /** Corresponding STRING_TYPE_XXX from Java (null if not defined). */
    val stringType: String?,
) {
    ACCELEROMETER(1, "android.sensor.accelerometer"),
    MAGNETIC_FIELD(2, "android.sensor.magnetic_field"),
    ;

    companion object {
        /** Returns enum from TYPE_XXX integer ID (null if not found). */
        fun fromId(id: Int): SensorType? = entries.firstOrNull { it.id == id }

        /** Returns enum from STRING_TYPE_XXX (case-sensitive; null if not found). */
        fun fromStringType(stringType: String): SensorType? = entries.firstOrNull { it.stringType == stringType }
    }
}
