package io.github.droidkaigi.confsched.testing.robot.core

/**
 * Kotlin enum transcription of Android's `Sensor.java` TYPE_XXX / STRING_TYPE_XXX constants
 *
 * Notes:
 * - Integer IDs (`id`) and string types (`stringType`) mirror AOSP `android.hardware.Sensor`.
 * - Deprecated items are kept to match the original API surface.
 * - Some @hide items (e.g., TILT_DETECTOR) are included for convenience; remove if undesired.
 * - Type 33 is intentionally skipped per AOSP comments (framework-internal additional info).
 * - `ALL` and `DEVICE_PRIVATE_BASE` are special values retained for parity/convenience.
 */
@Suppress("DEPRECATION")
enum class SensorType(
    /** Integer ID corresponding to Java's TYPE_XXX. */
    val id: Int,
    /** Corresponding STRING_TYPE_XXX from Java (null if not defined). */
    val stringType: String?
) {
    ACCELEROMETER(1, "android.sensor.accelerometer"),
    MAGNETIC_FIELD(2, "android.sensor.magnetic_field"),
    @Deprecated("Use SensorManager.getOrientation instead")
    ORIENTATION(3, "android.sensor.orientation"),
    GYROSCOPE(4, "android.sensor.gyroscope"),
    LIGHT(5, "android.sensor.light"),
    PRESSURE(6, "android.sensor.pressure"),
    @Deprecated("Use TYPE_AMBIENT_TEMPERATURE instead")
    TEMPERATURE(7, "android.sensor.temperature"),
    PROXIMITY(8, "android.sensor.proximity"),
    GRAVITY(9, "android.sensor.gravity"),
    LINEAR_ACCELERATION(10, "android.sensor.linear_acceleration"),
    ROTATION_VECTOR(11, "android.sensor.rotation_vector"),
    RELATIVE_HUMIDITY(12, "android.sensor.relative_humidity"),
    AMBIENT_TEMPERATURE(13, "android.sensor.ambient_temperature"),
    MAGNETIC_FIELD_UNCALIBRATED(14, "android.sensor.magnetic_field_uncalibrated"),
    GAME_ROTATION_VECTOR(15, "android.sensor.game_rotation_vector"),
    GYROSCOPE_UNCALIBRATED(16, "android.sensor.gyroscope_uncalibrated"),
    SIGNIFICANT_MOTION(17, "android.sensor.significant_motion"),
    STEP_DETECTOR(18, "android.sensor.step_detector"),
    STEP_COUNTER(19, "android.sensor.step_counter"),
    GEOMAGNETIC_ROTATION_VECTOR(20, "android.sensor.geomagnetic_rotation_vector"),
    HEART_RATE(21, "android.sensor.heart_rate"),
    // @hide in AOSP, included here for convenience
    TILT_DETECTOR(22, "android.sensor.tilt_detector"),
    WAKE_GESTURE(23, "android.sensor.wake_gesture"),
    GLANCE_GESTURE(24, "android.sensor.glance_gesture"),
    PICK_UP_GESTURE(25, "android.sensor.pick_up_gesture"),
    WRIST_TILT_GESTURE(26, "android.sensor.wrist_tilt_gesture"),
    DEVICE_ORIENTATION(27, "android.sensor.device_orientation"),
    POSE_6DOF(28, "android.sensor.pose_6dof"),
    STATIONARY_DETECT(29, "android.sensor.stationary_detect"),
    MOTION_DETECT(30, "android.sensor.motion_detect"),
    HEART_BEAT(31, "android.sensor.heart_beat"),
    DYNAMIC_SENSOR_META(32, "android.sensor.dynamic_sensor_meta"),
    // 33 is skipped (framework-internal additional info type)
    LOW_LATENCY_OFFBODY_DETECT(34, "android.sensor.low_latency_offbody_detect"),
    ACCELEROMETER_UNCALIBRATED(35, "android.sensor.accelerometer_uncalibrated"),
    HINGE_ANGLE(36, "android.sensor.hinge_angle"),
    HEAD_TRACKER(37, "android.sensor.head_tracker"),
    ACCELEROMETER_LIMITED_AXES(38, "android.sensor.accelerometer_limited_axes"),
    GYROSCOPE_LIMITED_AXES(39, "android.sensor.gyroscope_limited_axes"),
    ACCELEROMETER_LIMITED_AXES_UNCALIBRATED(40, "android.sensor.accelerometer_limited_axes_uncalibrated"),
    GYROSCOPE_LIMITED_AXES_UNCALIBRATED(41, "android.sensor.gyroscope_limited_axes_uncalibrated"),
    HEADING(42, "android.sensor.heading"),

    /** Special value included for convenience. */
    ALL(-1, null),

    /** Base value for vendor-defined sensors (special value). */
    DEVICE_PRIVATE_BASE(0x10000, null);

    companion object {
        /** Returns enum from TYPE_XXX integer ID (null if not found). */
        fun fromId(id: Int): SensorType? = entries.firstOrNull { it.id == id }

        /** Returns enum from STRING_TYPE_XXX (case-sensitive; null if not found). */
        fun fromStringType(stringType: String): SensorType? =
            entries.firstOrNull { it.stringType == stringType }
    }
}
