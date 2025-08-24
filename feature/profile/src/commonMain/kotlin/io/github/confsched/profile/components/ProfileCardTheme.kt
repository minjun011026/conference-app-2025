package io.github.confsched.profile.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.ui.graphics.Color
import androidx.graphics.shapes.RoundedPolygon

private val LightBaseColor = Color(0xFFF2F4FB)
private val DarkBaseColor = Color(0xFF280F83)

enum class ProfileCardTheme(
    val isDark: Boolean,
    val profileShape: ProfileShape,
    val baseColor: Color,
    val themeKey: String,
) {
    DarkPill(true, ProfileShape.Pill, DarkBaseColor, "dark_pill"),
    LightPill(false, ProfileShape.Pill, LightBaseColor, "light_pill"),
    DarkDiamond(true, ProfileShape.Diamond, DarkBaseColor, "dark_diamond"),
    LightDiamond(false, ProfileShape.Diamond, LightBaseColor, "light_diamond"),
    DarkFlower(true, ProfileShape.Flower, DarkBaseColor, "dark_flower"),
    LightFlower(false, ProfileShape.Flower, LightBaseColor, "light_flower");

    companion object Companion {
        fun fromThemeKey(themeKey: String): ProfileCardTheme {
            return entries.firstOrNull { it.themeKey == themeKey } ?: LightPill
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
enum class ProfileShape(val polygon: RoundedPolygon) {
    Pill(MaterialShapes.Pill),
    Diamond(MaterialShapes.Diamond),
    Flower(MaterialShapes.Flower);
}
